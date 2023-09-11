package hr.fer.zemris.chat;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class ClientExample {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		InetAddress serverAddress = InetAddress.getByName(args[0]);
		int serverPort = Integer.parseInt(args[1]);
		String packetSender = args[2];
		long packetNo = 0L;
		
		try(Socket socket = new Socket(serverAddress, serverPort)){
			
			//System.out.println(serverAddress);
			
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            
            //ByteArrayOutputStream bos = new ByteArrayOutputStream(outputStre); 
            
            long randkey = new Random().nextInt();
            long uid = -1;
            
            while(true) {
                
                outputStream.writeByte(1); 
                outputStream.writeLong(packetNo); 
                outputStream.writeUTF(packetSender);
                outputStream.writeLong(randkey);
                
                outputStream.flush();
                Thread.sleep(1000);
                
                if(inputStream.readByte() == 2) {
                	
                	long packetNumber = inputStream.readLong();
                	uid = inputStream.readLong();
                	
                	if(packetNumber == packetNo) {
                        System.out.println("Konekcija ostvarena");
                    	break;
                	}

    
                	
                } else {
                	System.out.println("Konekcija neuspjela, pokusavam ponovno...");
                }
                
                try {
    				Thread.sleep(1000);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
              
            }
            
            packetNo++;
            Scanner sc = new Scanner(System.in);
            
            while(true) {
            	
            	System.out.print("Poruka korisnika " + packetSender + ": ");
            	String msg = sc.nextLine();
           
            	
                outputStream.writeByte(msg.equals("bye") ? 3 : 4); 
                outputStream.writeLong(packetNo++);
                outputStream.writeLong(uid);
                outputStream.writeUTF(msg);
                
                outputStream.flush();
                
                System.out.println("Redni broj paketa: " + packetNo);
                
                if(msg.equals("bye")) {
                	System.out.println("Odjava sa servera.");
                	socket.close();
                	break;
                }
            	
            }
           		
		} catch(IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
