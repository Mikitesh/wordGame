package WordGame;

import java.io.*;
import java.net.*;
import java.util.*;

public class WordGame {
	private static Set<PrintWriter> clientWriters = new HashSet<>();

     public static void main(String[]args) {
    	try {
    		ServerSocket ServerSocket = new ServerSocket(5000);
    		System.out.println("the server is loading :");
    		
    		while(true) {
    			Socket Soc = ServerSocket.accept();
    			System.out.println("new client connection ");
    			new ClientHandler(Soc).start();
    		}
    	}catch(IOException ex) {
    		 System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
}
 class ClientHandler extends Thread{
	 private Socket soce;
	 
	 public ClientHandler (Socket soce) {
		 this.soce=soce;
	 }
 public void Run() {
		try (
			BufferedReader in = new BufferedReader(new InputStreamReader(soce.getInputStream()));
		     PrintWriter out = new PrintWriter(soce.getOutputStream());){
			
			synchronized(clientWriters) {
				clientWriters.add(out);
			}
			
			String word;
		    String LastWord= null;
			
			while((word=in.readLine())!= null) {
				if (LastWord.isEmpty() || word.toLowerCase().charAt(0) == LastWord.toLowerCase().charAt(word.length()-1)) {
					LastWord=word;
					broadcast(word);
				}else {
                    out.println("Invalid word! It must start with '" + LastWord.charAt(LastWord.length() - 1) + "'.");
                }
			}
		} catch(IOException e) {
			e.printStackTrace();
		}finally {
             try {
                soce.close();
             } catch (IOException e) {
                e.printStackTrace();
             }
           }
        
		
private void broadcast(String word) {
	   for (PrintWriter writer : clientWriters) {
	            writer.println("New word: " + word);
	        }
 }
 }
	
 public class WordGameClient{
	 public static void main (String[]args) {
		 System.out.println("Connecting to Word Game Server...");
		 try 
			 (Socket Socket = new Socket("localhost",5000);
			 BufferedReader in= new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			 PrintWriter out = new PrintWriter(Socket.getOutputStream());
			 Scanner scanner = new Scanner(System.in)){
			 new Thread(() -> {
				    String response;
				    try {
				        while ((response = in.readLine()) != null) {
				            System.out.println(response);
				        }
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}).start();

			 String word;
	            while (true) {
	                System.out.print("Enter a word: ");
	                word = scanner.nextLine();
	                out.println(word);
		 }
		 }catch (IOException e) {
	            e.printStackTrace();
	 }
 }
