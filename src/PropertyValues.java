// Reference: http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/

import java.io.*;
import java.util.Date;
import java.util.Properties;

public class PropertyValues {
    String result = "";
    InputStreamReader inputStream;
    String hostName;
    String port;
    String user;
    String password;
    String mailAddress;
    String subject;
    String content;

    public PropertyValues() throws IOException {
        getValues();
    }

    // Read properties from input stream
    // Exception when path incorrect/ properties not existent
    public void getValues() throws IOException {
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = new InputStreamReader(new FileInputStream(PropertiesPath.getPath()), "UTF-8");

            // TODO: Check this! Always true??
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());

            // Get property value and print it out
            mailAddress = prop.getProperty("mailAddress");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            hostName = prop.getProperty("hostName");
            port = prop.getProperty("port");
            subject = prop.getProperty("subject");
            String temp_content = prop.getProperty("mailBody");
            System.out.println("Temp_content: " + temp_content);
            System.out.println("Content: " + content);
            if (temp_content.contains("\n.")) {
                content = temp_content.replaceAll("\n.", "\n..");
            } else if (temp_content.startsWith(".")) {
                content = temp_content.replaceAll(".", "..");
            } else {
                content = temp_content;
            }
            System.out.println("Content after if..then...else: " + content);
            result = "Props = " + mailAddress + ", " + user + ", " + password + ", " + hostName + ", " + port;
            System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    // Property getters
    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getMailAddress() {
        return mailAddress;
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
}
