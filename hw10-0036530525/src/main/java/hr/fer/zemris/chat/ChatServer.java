package hr.fer.zemris.chat;

import java.io.IOException;
import java.net.*;

public class ChatServer {
	public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Choose a port number
            System.out.println("Server listening on port 12345...");

            Socket clientSocket1 = serverSocket.accept();
            System.out.println("Client 1 connected.");

            Socket clientSocket2 = serverSocket.accept();
            System.out.println("Client 2 connected.");

            // Create threads to handle communication with each client
            Thread client1Thread = new ClientHandler(clientSocket1, clientSocket2);
            Thread client2Thread = new ClientHandler(clientSocket2, clientSocket1);

            client1Thread.start();
            client2Thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
