import org.Base64;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TCPClient {
    // Server hostname
    private final String hostname;
    // Port number of server
    private final int serverPort;
    // TCP standard socket class
    private Socket clientSocket;

    // Output stream to server
    private DataOutputStream outToServer;
    // Input stream from server
    private BufferedReader inFromServer;

    private String user;
    private String password;
    private String recipientEmail;
    private String senderEmail;
    private String subject;
    private String content;
    private String attachmentPath;
    private final String BOUNDARY = "tudelu";

    public TCPClient(String hostname, int serverPort, String email, String attachment) {
        this.serverPort = serverPort;
        this.hostname = hostname;
        this.recipientEmail = email;
        this.attachmentPath = attachment;
        try {
            GetPropertyValues gpv = new GetPropertyValues();
            user = gpv.getUser();
            password = gpv.getPassword();
            senderEmail = gpv.getMailAddress();
            subject = gpv.getSubject();
            content = gpv.getContent();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startJob() {
        // Start client. Type "quit" for terminating
        // String entered by user
        String sentence;
        try {
            // Create socket --> connection establishment with server
            clientSocket = new Socket(hostname, serverPort);

            // Filter socket basic stream by special streams
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

            sentence = null;

            // Response from Server
            String serverResponse = readFromServer();
            System.out.println("Antwort: " + serverResponse);
            writeToServer("EHLO client.example.de");
            System.out.println("EHLO client.example.de");
            // Loop terminates when buffer is empty
            for (int i = 0; i < 10; i++) {
                serverResponse = readFromServer();
                System.out.println(serverResponse);
            }
            writeToServer("AUTH LOGIN");
            serverResponse = readFromServer();
            // if(serverResponse.equals("334 VXNlcm5hbWU6")){
            // Authentification of user and password with Base64
            writeToServer(encodeAuthentication(user));
            readFromServer();
            // if(serverResponse.equals("334 UGFzc3dvcmQ6")){
            writeToServer(encodeAuthentication(password));
            readFromServer();

            // Send email
            writeToServer("MAIL FROM:" + this.senderEmail);
            readFromServer();
            writeToServer("RCPT TO:" + this.recipientEmail);
            readFromServer();
            writeToServer("DATA");
            readFromServer();
            writeToServer("Subject: " + this.subject);

            // General MIME settings
            writeToServer("MIME-Version: 1.0");
            writeToServer("Content-Type: multipart/mixed; boundary= " + BOUNDARY);

            // Settings for text body
            writeToServer("--" + BOUNDARY);
            writeToServer("Content-Transfer-Encoding: quoted-printable");
            writeToServer("Content-Type: text/plain");
            writeToServer("");
            writeToServer(this.content);

            // Settings for attachment body
            writeToServer("--" + BOUNDARY);
            writeToServer("Content-Transfer-Encoding: base64");
            writeToServer("Content-Type: text/plain");
            File attachmentFile = new File(attachmentPath);
            writeToServer("Content-Disposition: attachment; filename=" + attachmentFile.getName());
            writeToServer("");
            Path filePath = Paths.get(attachmentPath);
            byte[] attachmentContent = Files.readAllBytes(filePath);
            writeToServer(encodeAuthentication(new String(attachmentContent)));

            // End of message
            writeToServer(".");

            readFromServer();
            writeToServer("QUIT");

            // Close socket-stream --> connection termination
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Connection aborted by server!");
        }
        System.out.println("TCP Client stopped!");
    }

    private void writeToServer(String request) throws IOException {
        // Send one line (with CRLF) to server
        outToServer.writeBytes(request + '\r' + '\n');
        System.out.println("TCP Client has sent the message: " + request);
    }

    private String readFromServer() throws IOException {
        // Read reply from server
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
