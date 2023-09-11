package hr.fer.zemris.java.oglasi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Oglasi extends JFrame
{
	
	private static DefaultListModel<String> listaOglasa = new DefaultListModel<>();
	
	public Oglasi() {
		super();
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JList<String> oglasi = new JList<>(listaOglasa);
        JScrollPane panelOglasa = new JScrollPane(oglasi);
        panelOglasa.setPreferredSize(new Dimension(200, getHeight()));
        add(panelOglasa, BorderLayout.CENTER);
           
	}
	
	
    public static void main( String[] args )
    {
    	SwingUtilities.invokeLater(() -> new Oglasi().setVisible(true));
    	
    	final int PORT = 6000;

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
            umetniOglas(clientSocket);
        }
    }
    
    public static void umetniOglas(Socket clientSocket) {

		try {
			DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
			
			while(true) {
				String korisnik = inputStream.readUTF();
				String oglas = inputStream.readUTF();
				
				if(oglas.equals("bye")) {
					inputStream.close();
					break;
				}
				
				System.out.println(korisnik + " je objavio oglas: " + oglas);
				update(korisnik + " je objavio oglas: " + oglas);
				outputStream.writeUTF("ack");
				outputStream.flush();
			}
		
			outputStream.writeUTF("bye");
			outputStream.flush();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }
    
    private static void update(String oglas) {
    	SwingUtilities.invokeLater(() -> listaOglasa.addElement(oglas)); 
    }

}



