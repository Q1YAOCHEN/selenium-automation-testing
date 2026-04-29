# Selenium Automation Testing

Automated UI testing project built with Java, Selenium WebDriver, Maven, and TestNG. The test suite validates several Northeastern University web workflows through browser automation, data-driven test inputs, screenshots, assertions, and an HTML execution report.

## My Contribution

This project was originally developed as a coursework/team project. I was responsible for designing and implementing the Selenium automation test cases, browser interaction workflow, validation logic, screenshot capture flow, and report generation support.

## Test Coverage

| Scenario | Purpose | Expected Result |
| --- | --- | --- |
| Download Transcript | Log in and download the latest transcript | Pass |
| Canvas Calendar Events | Add two event tasks on Canvas Calendar | Pass |
| Library Reservation | Reserve a spot in Snell Library | Pass |
| Dataset Download | Negative test for dataset download workflow | Fail |
| Academic Calendar Update | Update academic calendar information | Pass |

## Tech Stack

- Java 11+
- Selenium WebDriver 4
- TestNG
- Maven
- WebDriverManager
- Apache POI
- HTML reporting

## Project Structure

```text
src/test/java/base/      Shared WebDriver setup and teardown
src/test/java/tests/     End-to-end test scenarios
src/test/java/utils/     Excel reader, screenshots, and HTML report helpers
testng.xml               TestNG suite configuration
pom.xml                  Maven dependencies and test configuration
```

## Run Locally

Prerequisites:

- Java JDK 11 or newer
- Maven 3.6 or newer
- Google Chrome

Install dependencies:

```bash
mvn clean install -DskipTests
```

Run the test suite:

```bash
mvn clean test
```

After execution, open `test-report.html` to review the generated test results.

## Notes

- Test credentials and environment-specific data should stay outside Git.
- Duo two-factor authentication may require manual phone approval during login.
- Scenario 4 is intentionally designed as a negative test case.
