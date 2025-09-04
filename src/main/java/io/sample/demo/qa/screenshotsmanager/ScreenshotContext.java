package io.sample.demo.qa.screenshotsmanager;

import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class ScreenshotContext {

    private ScreenshotStrategy screenshotStrategy;

    public ScreenshotContext(ElementScreenshotStrategy elementScreenshotStrategy) {
        this.screenshotStrategy=elementScreenshotStrategy;
    }
    public ScreenshotContext(FullPageScreenshotStrategy fullPageScreenshotStrategy) {
        this.screenshotStrategy=fullPageScreenshotStrategy;
    }

    public void captureScreenshot(Page page, String filePath) {
        Objects.requireNonNull(screenshotStrategy, "Screenshot strategy is not set.");
        screenshotStrategy.capture(page, filePath);
    }
}