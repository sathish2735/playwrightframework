package io.sample.demo.qa.browsermanager;

import io.sample.demo.qa.exceptions.UtilsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BrowserName {

    CHROME("chrome"),
    FIREFOX("firefox"),
    MS_EDGE("msedge"),
    WEBKIT("webkit");

    private final String browserType;

//    // Constructor
//    BrowserName(String browserType) {
//        this.browserType = browserType;
//    }
//
//    // Getter
//    public String getBrowserType() {
//        return browserType;
//    }

    public static BrowserName fromString(String browserName) {
        return Arrays.stream(BrowserName.values())
                .filter(browserType -> browserType.getBrowserType().equalsIgnoreCase(browserName))
                .findFirst().orElseThrow(() -> new UtilsException("Unknown browser: " + browserName));
    }
}