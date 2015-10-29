// Reference: http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/

import java.io.IOException;

public class MailFile {
    public static void main(String[] args) throws IOException {
        System.out.println("My OS is: " + System.getProperty("os.name"));
        // Following arguments needs to be available: sender mail address and path of attachment
        if (args.length == 0) {
            System.err.println("Arguments Missing!");
            System.exit(1);
        }
        String senderAddress = "localhost";
        int senderPort = 60000;
        String receiverEmail = args[0];
        String attachment = args[1];
        TCPClient tcp = new TCPClient(senderAddress, senderPort, receiverEmail, attachment);
        tcp.startJob();
    }
}
