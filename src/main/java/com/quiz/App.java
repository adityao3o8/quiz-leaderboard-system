package com.quiz;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {

    public static void main(String[] args) throws Exception {

        String regNo = "RA2311003010136";
        String baseUrl = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";

        HttpClient client = HttpClient.newHttpClient();
        Set<String> seen = new HashSet<>();
        Map<String, Integer> scores = new LinkedHashMap<>();

        for (int i = 0; i <= 9; i++) {

            String url = baseUrl + "/quiz/messages?regNo=" + regNo + "&poll=" + i;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("poll " + i + " response: " + res.body());

            JSONObject data = new JSONObject(res.body());
            JSONArray events = data.getJSONArray("events");

            for (int j = 0; j < events.length(); j++) {
                JSONObject event = events.getJSONObject(j);
                String roundId = event.getString("roundId");
                String participant = event.getString("participant");
                int score = event.getInt("score");

                String key = roundId + "_" + participant;

                if (seen.contains(key)) {
                    System.out.println("already seen: " + key + ", skipping");
                    continue;
                }

                seen.add(key);
                scores.put(participant, scores.getOrDefault(participant, 0) + score);
            }

            if (i < 9) {
                System.out.println("waiting 5 seconds before next poll...");
                Thread.sleep(5000);
            }
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        JSONArray leaderboard = new JSONArray();
        int total = 0;

        for (Map.Entry<String, Integer> entry : list) {
            JSONObject p = new JSONObject();
            p.put("participant", entry.getKey());
            p.put("totalScore", entry.getValue());
            leaderboard.put(p);
            total += entry.getValue();
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("total score across all: " + total);

        JSONObject body = new JSONObject();
        body.put("regNo", regNo);
        body.put("leaderboard", leaderboard);

        HttpRequest submitReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/quiz/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> submitRes = client.send(submitReq, HttpResponse.BodyHandlers.ofString());
        System.out.println("submission result: " + submitRes.body());
    }
}