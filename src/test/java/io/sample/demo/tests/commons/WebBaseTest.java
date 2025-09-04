package io.sample.demo.tests.commons;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.sample.demo.qa.browsermanager.BrowserManager;
import io.sample.demo.qa.browsermanager.BrowserName;

import io.sample.demo.qa.constants.WebPortalConstants;
import io.sample.demo.tests.listeners.WebTestListeners;
import io.sample.demo.qa.utils.PerformanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public abstract class WebBaseTest {

    protected static ThreadLocal<Page> page = new ThreadLocal<>();
    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static BrowserManager browserManager;
//    protected final Logger log = LoggerFactory.getLogger(this.getClass());


    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        browserManager = new BrowserManager();
        log.info("Browser Manager has been initiated.");
        clearDirectory(System.getProperty("user.dir") + "/extent-reports");
        clearDirectory(System.getProperty("user.dir") + "/src/test/resources/screenshots");
        log.info("Previous run report files has been cleared");
    }

    @BeforeMethod(alwaysRun = true)
    public void init(Method method) {
        PLAYWRIGHT.set(Playwright.create());
        page.set(browserManager.getBrowserPage(PLAYWRIGHT.get()));
        log.info("Browser has been set.");
        WebTestListeners.setPage(page.get());
        PerformanceUtils.evaluatePageLoadTime(page.get(), method.getName());
        PerformanceUtils.evaluateDomContentLoadTime(page.get(), method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void destroy() {
//        if (isChromiumBrowser()) {
//            CdpUtils.destroyCdpSession();
//        }
        browserManager.destroyBrowserPage(page.get());
        page.remove();
        Objects.requireNonNull(PLAYWRIGHT.get(), "Playwright is null!");
        PLAYWRIGHT.get().close();
        PLAYWRIGHT.remove();
        log.info("Browser has been destroyed.");
    }

    private boolean isChromiumBrowser() {
        String browserName = WebPortalConstants.BROWSER;
        return (browserName.equalsIgnoreCase(BrowserName.CHROME.getBrowserType()) || browserName.equalsIgnoreCase(BrowserName.MS_EDGE.getBrowserType()));
    }
    private void clearDirectory(String path) {
        try (var paths = Files.walk(Paths.get(path))) {
            paths.map(java.nio.file.Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2)) // delete files before directories
                    .forEach(file -> {
                        if (!file.delete()) {
                            log.warn("Failed to delete {}", file.getAbsolutePath());
                        }
                    });
            log.info("Cleared directory: {}", path);
        } catch (IOException e) {
            log.error("Failed to clear directory {}: {}", path, e.getMessage());
        }
    }

}