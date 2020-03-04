import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class serverThread implements Runnable{
	Socket convoSocket;
	String user;
	int typeThread;
	threadSharedMemory shared;
	
	public serverThread(Socket s, int n, threadSharedMemory shar){
		this.convoSocket = s;
		this.typeThread = n;
		this.shared = shar;
	}
	
	public void run() {
		board handshake; //Jury rigged the board object to serve as initiation handshake object. Shares critical
		// values between thread and client which makes coordinates how game initializes
		try {
			//handshake: receive username from client
			InputStream is = convoSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);  
			handshake = (board)ois.readObject();
			String user = handshake.clientName;
			System.out.println("Player "+user+" has joined!!!");
			System.out.println("Thread: " + user + " is running.");
			
			// handshake: send typeThread back to client. Dictates how initialization goes.
			handshake.setTypeThread(typeThread);
			OutputStream os = convoSocket.getOutputStream(); 
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.flush();
			oos.writeObject(handshake);   //send object to client
			oos.flush();
			
			if(typeThread == 0){
				shared.firstSender(convoSocket,user);
				
			}else if(typeThread == 1){
				shared.secondSender(convoSocket,user);

			}
			System.out.println("The mighty thread " + user +" has finished its purpose. ");
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}  
}
}


