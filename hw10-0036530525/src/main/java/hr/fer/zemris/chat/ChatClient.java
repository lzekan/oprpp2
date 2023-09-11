package hr.fer.zemris.chat;

import java.net.*;
import java.io.*;

public class ChatClient {
	public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345); // Connect to the server
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = serverIn.readLine();
                        if (message == null) {
                            break;
                        }
                        System.out.println("Received: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // Read messages from the user and send to the server
            while (true) {
                String message = in.readLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                out.println(message);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
