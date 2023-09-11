package hr.fer.zemris.chat;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;

public class ServerExample 
{
	private static class Client {
		
		private InetAddress ip;
		private int port;
		private String username;
		private long randkey;
		private long packetNo;
		private long UID;

		
		public Client(InetAddress ip, int port, String username, long randkey, long uid) {
			this.ip = ip;
			this.port = port;
			this.username = username;
			this.randkey = randkey;
			this.packetNo = 0L;
			this.UID = uid;
		}
		
	}
	
	private static List<Client> clients = new ArrayList<>();
	private static long nextUID = ThreadLocalRandom.current().nextLong();

    public static void main( String[] args ) throws IOException {
    	final int PORT = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try {
                	Socket clientSocket = serverSocket.accept();
                	Thread clientThread = new Thread(new ClientHandler(clientSocket));
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    clientThread.start();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            handleNewClient(clientSocket);
        }
    }
    
    private static void handleNewClient(Socket clientSocket) {
    	
        boolean connectionMade = false;
    	
		try {
			DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());        
			DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
	        
	        byte packetByte = 0;
	        long packetNo = 0;
	        String packetUTF;
	        long packetRandkey;
	        
	        while(!connectionMade) {
	        	
	            packetByte = inputStream.readByte();
	            packetNo = inputStream.readLong();
	            packetUTF = inputStream.readUTF();
	            packetRandkey = inputStream.readLong();
	            
	            if(packetByte == 1) {
	            	double rand = new Random().nextDouble();
	            	
	            	if(rand > 0.33){
	            		processHello(clientSocket, outputStream, packetByte, packetNo, packetUTF, packetRandkey); 	
	            		connectionMade = true;
	            	} else {
	            		outputStream.writeByte(-1);
	            		outputStream.flush();
	            	}
	            
	            }
	            
	        }
	        
	        boolean isBye = false;
	        
	        while(!isBye) {
	        	isBye = processMSG(clientSocket, inputStream, outputStream);
	        }
	       
	        clientSocket.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
    
    
    
    private static void processHello(Socket clientSocket, DataOutputStream outputStream, 
    		byte packetByte, long packetNo, String packetUTF, long packetRandkey) throws IOException  {
    	
    	boolean retransmit = false;
    	
    	for(Client client : clients) {
			System.out.println(client.randkey + " " + packetRandkey);
    		
    		if(client.ip.equals(clientSocket.getInetAddress()) &&
    			client.port == clientSocket.getLocalPort() &&
    				client.randkey == packetRandkey) {
    			
    			System.out.println("Konekcija s klijentom " + client.username + " je vec otvorena.");
    			retransmit = true;
    			
    			outputStream.writeByte(2);
    			outputStream.writeLong(packetNo);
    			outputStream.writeLong(client.UID);
    	    	outputStream.flush();
    			break;
    		}
    	}
    	
    	if(!retransmit) {
    		
    		nextUID++;
    		Client newClient = new Client(clientSocket.getInetAddress(), clientSocket.getLocalPort(), 
        			packetUTF, packetRandkey, nextUID);
        	
        	outputStream.writeByte(2);
        	outputStream.writeLong(packetNo);
        	outputStream.writeLong(nextUID++);
        	outputStream.flush();
        	
        	System.out.println("Kreiram novog Clienta...");
        	clients.add(newClient);
        	
    	}
    	
    }
    
    private static boolean processMSG(Socket clientSocket, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
    	
        byte packetByte = inputStream.readByte();
        long packetNo = inputStream.readLong();
        long uid = inputStream.readLong();
        String msg = inputStream.readUTF();
        
        String username = "";
    	
    	for(Client client : clients) {
    		
    		if(client.UID == uid) {
    			username = client.username;
    		}
    	}
    	
        if(packetByte == 4) {
            
            System.out.println("Poruka od korisnika " + username + ": " + msg);
            sendACK(outputStream, packetNo, uid);
            return false;
        
        }
        
        if(packetByte == 3 && msg.equals("bye")) {
        	sendACK(outputStream, packetNo, uid);
            System.out.println("Zatvaram konekciju s korisnikom " + username + "...");
        }
    	
    	return true;
    	
    } 
    
    private static void sendACK(DataOutputStream outputStream, long packetNo, long uid) throws IOException {
    	
        outputStream.writeByte(2);
        outputStream.writeLong(packetNo);
        outputStream.writeLong(uid);
        outputStream.flush();
        
    }
    
}

