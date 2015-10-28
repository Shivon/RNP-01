// Reference: http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/

import java.io.*;
import java.util.Date;
import java.util.Properties;

public class GetPropertyValues {
    String result = "";
    InputStreamReader inputStream;
    String hostName;
    String port;
    String user;
    String password;
    String mailAdress;
    String betreff;
    String inhalt;


    public GetPropertyValues() throws IOException {
        getPropValues();
    }
    //Property Getters
    public String getBetreff() {
        return betreff;
    }

    public String getInhalt() {
        return inhalt;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    //Properties aus InputStream lesen, wenn Pfad inkorrekt/Properties nicht vorhanden -> Exception
    public void getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = new InputStreamReader(new FileInputStream(PropertiesPath.getPath()), "UTF-8");

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            mailAdress = prop.getProperty("mailAddress");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            hostName = prop.getProperty("hostName");
            port = prop.getProperty("port");
            betreff = prop.getProperty("betreff");
            inhalt = prop.getProperty("mailbody");
            result = "Props = " + mailAdress + ", " + user + ", " + password + ", " + hostName + ", " + port;
            System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }
}
