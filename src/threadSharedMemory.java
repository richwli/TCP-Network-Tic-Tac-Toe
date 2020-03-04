import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class threadSharedMemory {
	board gameBoard;
	int playersJoined = 0;
	int gameWon = 0; //Signifies when game has been won by someone
	
	public threadSharedMemory(){
		gameBoard = new board();
	}
	
	public synchronized void firstSender(Socket clientSocket, String user) throws IOException, InterruptedException, ClassNotFoundException{
		int userWon;
		board temp = new board(); //serves as comparison to see if board changes or not 
		playersJoined++;
		
		while(playersJoined != 2){
			System.out.println("Waiting for full lobby.");
			wait();
		}
		while(true){
			// Reads IN user input / make move ONTO board
			System.out.println("I am "+user +" playing.");

			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);  
			gameBoard = (board)ois.readObject();
			userWon = gameBoard.add();
			// Win condition
			if (userWon == 1){
				System.out.println("User won = 1");
				gameWon = 1;  //Sets game won for player
				gameBoard.gameWon=1;
				gameBoard.userWon = 0;
			}
			
			temp.copyBoard(gameBoard);
			gameBoard.prettyPrint();
			notifyAll();
			
			// WAIT for other thread to move
			while(board.compareBoard(temp,gameBoard) && gameWon == 0){	
				System.out.println("(1) Waiting for opponent to move . . . ");
				wait();
				System.out.println("(1) Hello I was woken! ");
			}
			// Sends game changes to client
			OutputStream os = clientSocket.getOutputStream(); 
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.flush();
			oos.writeObject(gameBoard);   //send object to client
			oos.flush();
			
			//Game finisher
			if (gameWon == 1){
				break;
			}
			}
			
	}
	
	public synchronized void secondSender(Socket clientSocket, String user) throws IOException, InterruptedException, ClassNotFoundException{
		int userWon = 0;
		board temp = new board(); //serves as comparison to see if board changes or not
		temp.copyBoard(gameBoard);
		while(true){
			while(board.compareBoard(temp,gameBoard) && gameWon == 0){	
				System.out.println("(2) Waiting for opponent to move . . . ");
				playersJoined++;
				notify();
				wait();
			}
			
			// Sends game changes BACK to client
			OutputStream os = clientSocket.getOutputStream(); 
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.flush();
			oos.writeObject(gameBoard);   //send object to client
			oos.flush();
			
			//Game finisher
			if (gameWon == 1){
				break;
			}
			
			// Make move ONTO board
			System.out.println("I am "+user +" playing.");
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);  
			gameBoard = (board)ois.readObject();
			userWon = gameBoard.add();
			if (userWon == 1){
				System.out.println("User won = 1");
				gameWon = 1;  //Sets game won for player
				gameBoard.gameWon=1;
				gameBoard.userWon = 1;
			}
			temp.copyBoard(gameBoard);
			gameBoard.prettyPrint();
			notify();
		
			
			}
		}

}

