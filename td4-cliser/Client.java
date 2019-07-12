import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) {
		DataInputStream in = null;
		DataOutputStream out = null;
		
		Socket socket = null;
		String msg_out, msg_in = null;
		Scanner clavier = null;
		
		try {
			socket = new Socket("10.20.114.11", 1234);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) { }

		while(msg_in != "stop") {
			try {			
				clavier = new Scanner(System.in);
				msg_out = clavier.nextLine()+"\n";
				out.write(msg_out.getBytes());
				
				msg_in = in.readLine();
				System.out.println("[CLIENT] Serveur : " + msg_in);
				
			}
			catch (IOException ex) { }
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clavier.close();
		
	}

}
