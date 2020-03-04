import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class tcpServer {
	public static void main(String[] args) throws IOException {
		int serverPort = 8001;
		int clientPort;
		board gameBoard;
		int userWon = 0;
		SocketAddress clientAddress;
		ServerSocket handshakeSocket = new ServerSocket(serverPort);
		int joinCount = 0;
		threadSharedMemory cloud = new threadSharedMemory();
		while(true){
			// Handshake
				//System.out.println("Hello there");
				Socket convoSocket = handshakeSocket.accept();
				clientAddress = convoSocket.getRemoteSocketAddress();
				clientPort = convoSocket.getPort();
				System.out.println("New client has joined! \n " +
									"Connection: "+ clientAddress+ "\n" +
									"Port: "+ clientPort);
				if(joinCount == 0){
					Thread helper = new Thread (new serverThread(convoSocket,joinCount,cloud));
					helper.start();
					joinCount++;
					
				}else if(joinCount == 1){
					Thread helper2 = new Thread (new serverThread(convoSocket,joinCount,cloud));
					helper2.start();
					joinCount++;
				}else{
					System.out.println("Server is full, please come back later.");
			}
		}	
	}
}
