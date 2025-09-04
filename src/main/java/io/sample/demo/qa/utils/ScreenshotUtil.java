package io.sample.demo.qa.utils;

import com.microsoft.playwright.Page;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    public static String captureScreenshot(Page page, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "screenshot_" + testName + "_" + timestamp + ".png";
        Path path = Paths.get("extent-reports/screenshots", fileName);
        page.screenshot(new Page.ScreenshotOptions().setPath(path).setFullPage(true));
        return path.toString();
    }
}
