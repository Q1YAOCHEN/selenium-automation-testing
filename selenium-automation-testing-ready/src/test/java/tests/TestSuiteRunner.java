package tests;

import utils.HtmlReportGenerator;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuiteListener;
import org.testng.ISuite;

// Listener class that generates HTML report after entire suite finishes
public class TestSuiteRunner implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        System.out.println("******************************************");
        System.out.println("Starting Test Suite: " + suite.getName());
        System.out.println("******************************************");
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("******************************************");
        System.out.println("Test Suite Finished: " + suite.getName());
        System.out.println("Generating HTML Report...");
        System.out.println("******************************************");

        // Generate the HTML report after all scenarios are done
        HtmlReportGenerator.generateReport();
    }
}