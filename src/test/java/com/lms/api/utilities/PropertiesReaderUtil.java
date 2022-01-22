package com.lms.api.utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesReaderUtil {
    FileInputStream fis;

    public Properties loadProperties() {
        try {
            fis = new FileInputStream("src/test/resources/properties/application.properties");
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            return prop;

        } catch (Exception e) {
            System.out.println("Config.properties file not found");
            return null;
        }

    }
}
