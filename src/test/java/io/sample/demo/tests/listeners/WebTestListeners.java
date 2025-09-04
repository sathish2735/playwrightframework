package io.sample.demo.tests.listeners;

import com.aventstack.extentreports.*;
import com.microsoft.playwright.Page;
import io.sample.demo.qa.constants.WebPortalConstants;
import io.sample.demo.qa.reporting.ExtentManager;
import io.sample.demo.qa.screenshotsmanager.FullPageScreenshotStrategy;
import io.sample.demo.qa.screenshotsmanager.ScreenshotContext;
import lombok.extern.slf4j.Slf4j;
import org.testng.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class WebTestListeners implements ISuiteListener, ITestListener, IRetryAnalyzer {

    private int retryCount = 0;
    private Instant startDate;

    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    public static void setPage(Page page) {
        PAGE.set(page);
    }

    @Override
    public void onStart(ISuite suite) {
        startDate = Instant.now();
        ExtentManager.initReport();
        log.info("Test suite started: {}", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.createTest(result.getMethod().getMethodName());
//        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        takeScreenshot(result, Status.PASS);
        Optional.ofNullable(ExtentManager.getTest()).ifPresent(t -> t.pass("Test passed"));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        takeScreenshot(result, Status.FAIL);
        Optional.ofNullable(ExtentManager.getTest()).ifPresent(t -> t.fail(result.getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        takeScreenshot(result, Status.SKIP);
        Optional.ofNullable(ExtentManager.getTest()).ifPresent(t -> t.skip("Test skipped"));
    }

    private void takeScreenshot(ITestResult result, Status status) {
        Page currentPage = PAGE.get();
        if (currentPage == null || currentPage.isClosed()) {
            log.warn("Page object is null or already closed. Skipping screenshot.");
            return;
        }

        String testName = result.getName();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String param = extractSafeParameter(result);
        String fileName = formatScreenshotFileName(status.name(), testName, param, timestamp);

        try {
            String extentReportDir = System.getProperty("user.dir") + "/extent-reports";
            String screenshotSubPath = Paths.get("screenshots", status.name().toLowerCase(), fileName).toString();
            String screenshotFullPath = Paths.get(extentReportDir, screenshotSubPath).toString();

            Files.createDirectories(Paths.get(screenshotFullPath).getParent());

            ScreenshotContext context = new ScreenshotContext(new FullPageScreenshotStrategy());
            context.captureScreenshot(currentPage, screenshotFullPath);

            Optional.ofNullable(ExtentManager.getTest())
                    .ifPresent(t -> t.addScreenCaptureFromPath(screenshotSubPath.replace("\\", "/"), "Screenshot on " + status.name()));

            System.out.println("Screenshot added to the report");
        } catch (IOException e) {
            log.error("Failed to capture screenshot for {}: {}", testName, e.getMessage());
        }
    }

    private String extractSafeParameter(ITestResult result) {
        int maxParamLength = 30;
        if (result.getParameters().length == 0 || result.getParameters()[0] == null) return "NoParams";
        String param = result.getParameters()[0].toString().replaceAll("[^a-zA-Z0-9-_]", "_");
        return param.length() > maxParamLength ? param.substring(0, maxParamLength) : param;
    }

    private String formatScreenshotFileName(String prefix, String testName, String param, String timestamp) {
        return String.format("%s_%s_%s_%s_%s_%s%s",
                WebPortalConstants.BROWSER, WebPortalConstants.RUN_MODE,
                prefix, testName, param, timestamp,
                WebPortalConstants.IMAGE_FORMAT);
    }

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = 1;
        if (!result.isSuccess() && retryCount < maxRetry) {
            log.warn("Retrying {} (attempt {})", result.getName(), retryCount + 1);
            retryCount++;
            return true;
        }
        return false;
    }

    @Override
    public void onFinish(ISuite suite) {
        Instant end = Instant.now();
        Duration duration = Duration.between(startDate, end);
        log.info("Suite completed in {} seconds", duration.getSeconds());
        ExtentManager.flushReport();
    }

    @Override
    public void onFinish(ITestContext context) {
        // no-op
    }
}

