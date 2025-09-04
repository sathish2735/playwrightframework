package io.sample.demo.qa.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.sample.demo.qa.constants.WebPortalConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private ExtentManager() {}

    public static void initReport() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/extent-reports/ExtentReport_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Web Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Browser", WebPortalConstants.BROWSER);
            extent.setSystemInfo("Run Mode", WebPortalConstants.RUN_MODE);
        }
    }

    public static ExtentTest createTest(String testName) {
        if (extent == null) {
            throw new IllegalStateException("Extent report not initialized. Call initReport() first.");
        }
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
