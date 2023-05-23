package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static String getProperty(String fileName,String propertyName) {
        String propertyValue = null;

        try (InputStream input = new FileInputStream("./src/main/resources/"+fileName+".properties")) {
            Properties prop = new Properties();
            prop.load(input);
            propertyValue = prop.getProperty(propertyName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return propertyValue;
    }

}
