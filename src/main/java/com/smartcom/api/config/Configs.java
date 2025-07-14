package com.smartcom.api.config;





import java.util.HashMap;
import java.util.Properties;

public class Configs {

    private static ConfigReader configReader = new ConfigReader();
    private static Properties properties = new Properties(configReader.init_prop());
    public static String imagePath = System.getProperty("user.dir") + getImagesPath();
    public static String strBrowser = properties.getProperty("browser");
    public static String strEnv = properties.getProperty("env");
    public static boolean isLocal = false;
    public static String runenv = properties.getProperty("runenv");
    public static String os = properties.getProperty("os");
    public static String osVersion = properties.getProperty("os_version");
    public static String strUrl = "";
    public static int passCount = 0;
    public static int failCount = 0;
    public static int skipCount = 0;
    public static HashMap<String, String> result = new HashMap<>();
    public static String currentBuildName = "";
    
    
    // Browserstack Configs

    public static final String AUTOMATE_USERNAME = properties.getProperty("automateUsername");
    public static final String AUTOMATE_ACCESS_KEY =properties.getProperty("automateAccessKey");
    public static final String browserStackURL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    static {
        if (strEnv.equals("uat"))
            strUrl = properties.getProperty("uat_url");
        else // dev env
            strUrl = properties.getProperty("dev_url");
        //browserstack or local
        if (runenv.toLowerCase().contains("browser"))
            isLocal = false;
        else
            isLocal = true;

    }

    public static String projectPath = System.getProperty("user.dir");
    public static String storesPath = projectPath + getDBPath();


    // db and cognito settings
    public static String aws_PoolId_uat = properties.getProperty("awsPoolIdUAT");
    public static String aws_PoolId = properties.getProperty("awsPoolId");
    public static String amazonAWSAccessKey = properties.getProperty("amazonAWSAccessKey");
    public static String amazonAWSSecretKey = properties.getProperty("amazonAWSSecretKey");
    public static String amazonAWSSessionToken = properties.getProperty("amazonAWSSessionToken");
    public static String amazonAWSAccessKey_uat = properties.getProperty("amazonAWSAccessKeyUAT");
    public static String amazonAWSSecretKey_uat = properties.getProperty("amazonAWSSecretKeyUAT");
    public static String amazonAWSSessionToken_uat = properties.getProperty("amazonAWSSessionTokenUAT");
    public static boolean runAll = true;

    public static void main(String[] args) {
        System.out.println(storesPath);
    }

    /*public static String getConfigPath() {
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            return "\\src\\test\\resources\\config.properties";
        else
            return "/src/test/resources/config.properties";
    }*/

    public static String getImagesPath() {
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            return "\\src\\test\\resources\\images\\";
        else
            return "/src/test/resources/images/";
    }

    public static String getDBPath() {
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            return "\\stores\\";
        else
            return "/stores/";
    }
}
