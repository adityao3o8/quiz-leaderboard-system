# Quiz Leaderboard System

## Overview

This project is a simple Java application that processes quiz data from an external API and generates a leaderboard based on participant scores.

The main goal was to correctly handle repeated API responses and ensure that duplicate data does not affect the final result.

---

## Approach

The program makes 10 API calls (polls) with a 5-second delay between each request. All responses are collected and processed.

Since the same data can appear multiple times, duplicates are filtered using a combination of `roundId` and `participant`. Only unique entries are considered.

After removing duplicates, the scores are aggregated for each participant and sorted in descending order to generate the final leaderboard.

---

## Technologies Used

* Java (JDK 11)
* HttpClient for API requests
* org.json for JSON parsing

---

## How to Run

1. Clone the repository
2. Open the project in IntelliJ or VS Code
3. Update the `regNo` value in `App.java`
4. Run the program

---

## Output

The program prints the leaderboard along with the total score of all participants.

Example:
Diana → 470
Ethan → 455
Fiona → 440

Total Score: 1365

---

## Notes

* A 5-second delay is maintained between API calls as required
* Duplicate entries are properly handled
* The final leaderboard is submitted only once

---

## Conclusion

This project focuses on handling repeated API data correctly and ensuring accurate aggregation of results. It demonstrates basic backend processing and data handling using Java.
