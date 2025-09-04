package io.sample.demo.qa.browsermanager;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import io.sample.demo.qa.constants.WebPortalConstants;

public class FirefoxBrowser implements IBrowser {
    @Override
    public BrowserContext createSession(Playwright playwright, boolean isHeadless) {
        return playwright.firefox().launch(new BrowserType.LaunchOptions()
                        .setHeadless(isHeadless))
                .newContext(new Browser.NewContextOptions()
                        .setViewportSize(WebPortalConstants.SCREEN_WIDTH, WebPortalConstants.SCREEN_HEIGHT));
    }
}