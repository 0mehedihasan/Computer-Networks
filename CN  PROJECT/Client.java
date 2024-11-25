package system;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server.");

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);

                if (serverMessage.startsWith("Enter")) {
                    String userInput = console.readLine();
                    out.println(userInput);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}