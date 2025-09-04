package io.sample.demo.qa.screenshotsmanager;

import com.microsoft.playwright.Page;

public interface ScreenshotStrategy {

    void capture(Page page, String filePath);
}