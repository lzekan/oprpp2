package hr.fer.zemris.chat;

import java.net.*;
import java.io.*;

public class ClientHandler extends Thread{

	private final Socket clientSocket;
    private final Socket otherClientSocket;

    public ClientHandler(Socket clientSocket, Socket otherClientSocket) {
        this.clientSocket = clientSocket;
        this.otherClientSocket = otherClientSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Received: " + message);

                // Forward the message to the other client
                PrintWriter otherOut = new PrintWriter(otherClientSocket.getOutputStream(), true);
                otherOut.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
