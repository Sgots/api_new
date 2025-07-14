package com.smartcom.api.config;

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
          //  FileInputStream fileInputStream = new FileInputStream("C:\\Users\\kraym\\Desktop\\Smartcom\\application.properties");

           // FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.properties");
         FileInputStream fileInputStream = new FileInputStream("/root/config/application.properties");
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
