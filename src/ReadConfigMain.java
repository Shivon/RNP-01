// Reference: http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/

import java.io.File;
import java.io.IOException;

public class ReadConfigMain {
    public static void main(String[] args) throws IOException {
        System.out.println("My OS is: " + System.getProperty("os.name"));
        //es muessen die Argumente Sender-email-Adresse und Pfad des Anhangs vorliegen
        if (args.length == 0) {
            System.err.println("Arguments Missing!");
            System.exit(1);
        }
        String addr = "localhost";
        int port = 60000;
        String email = args[0];
        String anhang = args[1];
        //Sender-Adresse, Sender-Port, Empfaenger-Email-Adresse und Anhang
        TCPClient tcp = new TCPClient(addr, port, email, anhang);
        tcp.startJob();
    }
}
