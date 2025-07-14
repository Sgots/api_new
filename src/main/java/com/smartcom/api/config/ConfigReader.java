package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public Properties init_prop()
    {
        properties = new Properties();
        try
        {
            FileInputStream fileInputStream = new FileInputStream("src/test/java/config/config.properties");
            properties.load(fileInputStream);
        }
        catch (FileNotFoundException fileNotFoundException)
        {
            fileNotFoundException.printStackTrace();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }

        return  properties;
    }
}
