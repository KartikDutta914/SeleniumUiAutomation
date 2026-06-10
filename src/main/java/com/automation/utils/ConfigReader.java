package com.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class to read configuration properties from config.properties.
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties = new Properties();

    static {
        String configFilePath = "src/main/resources/config.properties";
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
            logger.info("Configuration properties loaded successfully from: {}", configFilePath);
        } catch (IOException e) {
            logger.error("Failed to load configuration properties file at: {}", configFilePath, e);
            throw new RuntimeException("Could not load config.properties file", e);
        }
    }

    /**
     * Retrieve property value based on key.
     * @param key the property key
     * @return property value as String, or null if not found
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property for key '{}' was not found in config.properties", key);
        }
        return value;
    }
}
