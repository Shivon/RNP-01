/**
 * Created by Jana on 24.10.2015.
 */

import org.Base64;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TCPClient {
    /* Portnummer des Servers*/
    private final int serverPort;

    /* Hostname des Servers*/
    private final String hostname;

    private Socket clientSocket; // TCP-Standard-Socketklasse

    private DataOutputStream outToServer; // Ausgabestream zum Server
    private BufferedReader inFromServer; // Eingabestream vom Server

    private String user;
    private String password;
    private String recipientEmail;
    private String senderEmail;
    private String betreff;
    private String inhalt;
    private String anhangPfad;
    private final String BOUNDARY = "tudelu";

    public TCPClient(String hostname, int serverPort, String email, String anhang) {
        this.serverPort = serverPort;
        this.hostname = hostname;
        this.recipientEmail = email;
        this.anhangPfad = anhang;
        try {
            GetPropertyValues gpv = new GetPropertyValues();
            user = gpv.getUser();
            password = gpv.getPassword();
            senderEmail = gpv.getMailAdress();
            betreff = gpv.getBetreff();
            inhalt = gpv.getInhalt();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startJob() {
        /* Client starten. Ende, wenn quit eingegeben wurde */
        String sentence; // vom User uebergebener String
        try {
            /* Socket erzeugen --> Verbindungsaufbau mit dem Server */
            clientSocket = new Socket(hostname, serverPort);

            /* Socket-Basisstreams durch spezielle Streams filtern */
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

            sentence = null;

            /* Response from Server */
            String serverResponse = readFromServer();
            System.out.println("Antwort: " + serverResponse);
            writeToServer("EHLO client.example.de");
            System.out.println("EHLO client.example.de");
            /* Die schleife soll solange laufen, bis der buffer leer ist. */
            for (int i = 0; i < 10; i++) {
                serverResponse = readFromServer();
                System.out.println(serverResponse);
            }
            writeToServer("AUTH LOGIN");
            serverResponse = readFromServer();
//          if(serverResponse.equals("334 VXNlcm5hbWU6")){
            /* Authentification of user and password with Base64 */
            writeToServer(encodeAuthentication(user));
            readFromServer();
//          if(serverResponse.equals("334 UGFzc3dvcmQ6")){
            writeToServer(encodeAuthentication(password));
            readFromServer();

            /* send email */
            writeToServer("MAIL FROM:" + this.senderEmail);
            readFromServer();
            writeToServer("RCPT TO:" + this.recipientEmail);
            readFromServer();
            writeToServer("DATA");
            readFromServer();
            writeToServer("Subject: " + this.betreff);

            /* general MIME settings */
            writeToServer("MIME-Version: 1.0");
            writeToServer("Content-Type: multipart/mixed; boundary= " + BOUNDARY);

            /* settings for text body */
            writeToServer("--" + BOUNDARY);
            writeToServer("Content-Transfer-Encoding: quoted-printable");
            writeToServer("Content-Type: text/plain");
            writeToServer("");
            writeToServer(this.inhalt );

            /* settings for attachment body */
            writeToServer("--" + BOUNDARY);
            writeToServer("Content-Transfer-Encoding: base64");
            writeToServer("Content-Type: text/plain");
            File anhangFile = new File(anhangPfad);
            writeToServer("Content-Disposition: attachment; filename=" + anhangFile.getName());
            writeToServer("");
            Path filePath = Paths.get(anhangPfad);
            byte[] anhangInhalt = Files.readAllBytes(filePath);
            writeToServer(encodeAuthentication(new String(anhangInhalt)));

            /* End of message */
            writeToServer(".");

            readFromServer();
            writeToServer("QUIT");

            /* Socket-Streams schliessen --> Verbindungsabbau */
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Connection aborted by server!");
        }
        System.out.println("TCP Client stopped!");
    }

    private void writeToServer(String request) throws IOException {
        /* Sende eine Zeile (mit CRLF) zum Server */
        outToServer.writeBytes(request + '\r' + '\n');
        System.out.println("TCP Client has sent the message: " + request);
    }

    private String readFromServer() throws IOException {
        /* Lies die Antwort (reply) vom Server */
        String reply = inFromServer.readLine();
        System.out.println("TCP Client got from Server: " + reply);
        return reply;
    }

    private String encodeAuthentication(String encrypt){
            byte[] bytes = encrypt.getBytes();
            String newEncodedString = new String(Base64.encodeBytes(bytes));

            return newEncodedString;
    }

}
