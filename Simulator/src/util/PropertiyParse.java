package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiyParse {

    public static String readProperty(String key) {
        Properties properties = new Properties();
        InputStream inputStream = Object.class.getResourceAsStream("/profile.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("get properties error" + e);
        }
        return properties.getProperty(key);
    }
}
