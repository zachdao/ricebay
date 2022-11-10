package edu.rice.comp610.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading properties to configure RiceBay
 */
public class PropertiesLoader {

    /**
     * Load properties for the environment specified as a system property {@literal ricebay.environ}.
     * If not specified, then load properties for the {@literal local} environment.
     *
     * @return the properties.
     */
    public static Properties loadProperties() {
        String environ = System.getProperties().getProperty("ricebay.environ", "local");
        return loadProperties(environ);
    }

    /**
     * Load properties from a properties file resource in the package {@link edu.rice.comp610}
     * @param environmentName the name of the environment. Can be one of "heroku", "local"
     */
    public static Properties loadProperties(String environmentName) {
        String path = "/" + environmentName + ".properties";
        try (InputStream stream = PropertiesLoader.class.getResourceAsStream(path)) {
            if (stream == null)
                throw new IOException("resource not found");
            Properties properties = new Properties(System.getProperties());
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties for environment " + environmentName + " (" + path + ")", e);
        }
    }
}
