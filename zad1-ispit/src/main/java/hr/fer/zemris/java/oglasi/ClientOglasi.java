package hr.fer.zemris.java.oglasi;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientOglasi {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
		String ime = "";
		
		try(Socket socket = new Socket(serverAddress, 6000)){
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            
            System.out.print("Unesi svoje ime: ");
        	Scanner sc = new Scanner(System.in);
        	ime = sc.next();
            
            while(true) {
            	System.out.print("Unesi oglas: ");
            	String oglas = sc.next();
            	
            	outputStream.writeUTF(ime);
            	outputStream.writeUTF(oglas);
            	outputStream.flush();
                
                if(inputStream.readUTF().equals("bye")) {
                	outputStream.close();
                }
            	
            }
           
            
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
