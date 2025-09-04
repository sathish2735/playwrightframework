package io.sample.demo.qa.utils;

import java.util.Objects;
import java.util.Properties;

public class WebConfigLoader {

    private static WebConfigLoader instance;
    private final Properties PROPERTIES;

    private WebConfigLoader(String env) {
        String configFilePath = String.format("./src/main/resources/webconfig-%s.properties", env.toLowerCase());
        PROPERTIES = PropertiesUtils.loadProperties(configFilePath);
    }

    public static WebConfigLoader getInstance() {
        return getInstance(System.getProperty("env", "qa")); // default to "qa"
    }

    public static WebConfigLoader getInstance(String env) {
        if (instance == null || instance.isSameEnv(env)) {
            synchronized (WebConfigLoader.class) {
                if (instance == null || instance.isSameEnv(env)) {
                    instance = new WebConfigLoader(env);
                    instance.currentEnv = env;
                }
            }
        }
        return instance;
    }

    private String currentEnv;

    private boolean isSameEnv(String env) {
        return currentEnv == null || !currentEnv.equalsIgnoreCase(env);
    }

    public String getSwagLabsUrl() {
        return getPropertyValue("swaglabs.url");
    }

    public String getSwagLabsUserName() {
        return getPropertyValue("swaglabs.username");
    }

    public String getSwagLabsPassword() {
        return getPropertyValue("swaglabs.password");
    }

    private String getPropertyValue(String propertyKey) {
        String propertyValue = PROPERTIES.getProperty(propertyKey);
        Objects.requireNonNull(propertyValue, "Property value for key '" + propertyKey + "' is missing!");
        return propertyValue;
    }
}
