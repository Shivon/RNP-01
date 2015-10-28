public class PropertiesPath {
    public static String getPath() {
        if (PropertiesPath.isUnix()) {
            return "./resources/config/config.properties";
        } else {
            return ".\\resources\\config\\config.properties";
        }
    }

    private static boolean isUnix() {
        String os = System.getProperty("os.name");
        return (os.startsWith("Linux") || os.startsWith("Mac"));
    }
}
