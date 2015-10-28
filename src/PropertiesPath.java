public class PropertiesPath {
    public static String getPath() throws Exception {
        if (PropertiesPath.is_Unix()) {
            return "./resources/config/config.properties";
        } else {
            return ".\\resources\\config\\config.properties";
        }
    }

    private static boolean is_Unix() {
        String os = System.getProperty("os.name");
        return (os.startsWith("Linux") || os.startsWith("Mac"));
    }
}
