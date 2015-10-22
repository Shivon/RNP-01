// Reference: http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/

import java.io.IOException;

public class ReadConfigMain {
    public static void main(String[] args) throws IOException {
        GetPropertyValues properties = new GetPropertyValues();
        properties.getPropValues();
    }
}
