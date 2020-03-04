import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class tcpClient {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String username;
		int threadSeq; //typeThread #
		board io = new board(); //Input/Output data between server thread
		InetAddress serverAddress = InetAddress.getByName("localhost"); // Server address
		int servPort = 8001; // get port number
		Socket clientSocket = null;

		try{
			clientSocket = new Socket(serverAddress,servPort);
			System.out.println("Welcome to the MMO Tic Tac Toe!!!");
			System.out.println("Please put down your name.");
			username = reader.readLine();
			io.clientName = username;
			
			//Handshake: Sending username to server
			OutputStream os = clientSocket.getOutputStream(); 
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.flush();
			oos.writeObject(io);   //send object to server
			oos.flush();
			
			//Handshake: Receiving typeThread back from server
			InputStream is = clientSocket.getInputStream();  
			ObjectInputStream ois = new ObjectInputStream(is);  
			io = (board)ois.readObject();
			threadSeq = io.typeThread;
			System.out.println("You will be player " + (threadSeq+1));
			
			while(true){
				if(threadSeq == 0){ //Player 1 receives input first
					if(io.gameWon != 1){
						//Sending move to server
						io.getInput();
						os = clientSocket.getOutputStream(); 
						oos = new ObjectOutputStream(os);
						oos.flush();
						oos.writeObject(io);   //send object to server
						oos.flush();
					}
					
					//Receiving move back from opponent
					is = clientSocket.getInputStream();  
					ois = new ObjectInputStream(is);  
					io = (board)ois.readObject();
					System.out.println("Your opponent has made his/her move: ");
					io.prettyPrint();
					if (io.gameWon == 1) break;
					
					
				}else if (threadSeq == 1){ //Player 2 receives player 1's updated board then continues game
					//Receiving move back from opponent
					is = clientSocket.getInputStream();  
					ois = new ObjectInputStream(is);  
					io = (board)ois.readObject();
					System.out.println("Your opponent has made his/her move: ");
					io.prettyPrint();
					if (io.gameWon == 1) break;
					
					if(io.gameWon != 1){
						//Sending move to server
						io.getInput();
						os = clientSocket.getOutputStream(); 
						oos = new ObjectOutputStream(os);
						oos.flush();
						oos.writeObject(io);   //send object to server
						oos.flush();
					}
				}
			}

				//System.out.println("gameWon is "+io.gameWon);
				if(io.userWon == threadSeq){
					System.out.println("Congrats " +username+" you have won!!!");
				}else{
					System.out.println("Sorry " +username+"... you have lost...");
				}
			
		}catch(ConnectException e){
		System.out.println("404 Error Server not found. Sucks to suck man.");
		
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}
}
	}