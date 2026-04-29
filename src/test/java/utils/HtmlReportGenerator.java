package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Generates an HTML report after all test scenarios are executed
public class HtmlReportGenerator {

    // Inner class to hold individual test result data
    public static class TestResult {
        String scenarioName;
        String actual;
        String expected;
        String status;

        public TestResult(String scenarioName, String actual, String expected, String status) {
            this.scenarioName = scenarioName;
            this.actual = actual;
            this.expected = expected;
            this.status = status;
        }
    }

    // List to store all test results
    private static List<TestResult> results = new ArrayList<>();

    // Add a test result to the list
    public static void addResult(String scenarioName, String actual, String expected, String status) {
        results.add(new TestResult(scenarioName, actual, expected, status));
    }

    // Generate the HTML report file
    public static void generateReport() {
        String filePath = "test-report.html";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n<head>\n");
            writer.write("<title>Selenium Test Automation Report</title>\n");
            writer.write("<style>\n");
            writer.write("body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }\n");
            writer.write("h1 { color: #333; text-align: center; }\n");
            writer.write("h3 { color: #666; text-align: center; }\n");
            writer.write("table { border-collapse: collapse; width: 90%; margin: 20px auto; background: white; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }\n");
            writer.write("th, td { border: 1px solid #ddd; padding: 12px 15px; text-align: left; }\n");
            writer.write("th { background-color: #cc0000; color: white; font-weight: bold; }\n");
            writer.write("tr:nth-child(even) { background-color: #f9f9f9; }\n");
            writer.write(".pass { color: green; font-weight: bold; }\n");
            writer.write(".fail { color: red; font-weight: bold; }\n");
            writer.write(".footer { text-align: center; margin-top: 20px; color: #999; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n<body>\n");
            writer.write("<h1>INFO6255 - Selenium Test Automation Report</h1>\n");
            writer.write("<h3>Northeastern University - Spring 2026</h3>\n");

            // Create the results table
            writer.write("<table>\n");
            writer.write("<tr><th>#</th><th>Test Scenario Name</th><th>Actual</th><th>Expected</th><th>Pass/Fail</th></tr>\n");

            int count = 1;
            for (TestResult result : results) {
                String statusClass = result.status.equalsIgnoreCase("Pass") ? "pass" : "fail";
                writer.write("<tr>");
                writer.write("<td>" + count + "</td>");
                writer.write("<td>" + result.scenarioName + "</td>");
                writer.write("<td>" + result.actual + "</td>");
                writer.write("<td>" + result.expected + "</td>");
                writer.write("<td class='" + statusClass + "'>" + result.status + "</td>");
                writer.write("</tr>\n");
                count++;
            }

            writer.write("</table>\n");
            writer.write("<p class='footer'>Report generated automatically after test execution</p>\n");
            writer.write("</body>\n</html>");

            System.out.println("HTML Report generated: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to generate report: " + e.getMessage());
        }
    }
}