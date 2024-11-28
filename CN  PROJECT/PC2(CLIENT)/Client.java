package CLIENT;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("your Server PC1 ip address here", 3334)) {
            System.out.println("Connected to the PC1 (server) .");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);

                // Only send input when prompted
                if (serverMessage.contains("Enter your choice") || serverMessage.contains("Enter")) {
                    String input = userInput.readLine();
                    out.println(input);
                    if (input.equalsIgnoreCase("9")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to connect to the server.");
            e.printStackTrace();
        }
    }
}
