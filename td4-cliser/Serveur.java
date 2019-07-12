import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur {
	
	public static void main(String args[]) throws Exception {
		ServerSocket sockserv = null;
		Socket sockcli = null;
		DataInputStream in;
		DataOutputStream out;
		String msg_in, msg_out;
		Scanner clavier = null;
		msg_in = "";
		
		sockserv = new ServerSocket(1234);
		try {
			while(msg_in != "stop") {
				try {
					sockcli = sockserv.accept();
					in = new DataInputStream(sockcli.getInputStream());
					out = new DataOutputStream(sockcli.getOutputStream());
					
					msg_in = in.readLine();
					System.out.println("[SERVEUR] Client : " + msg_in);
					
					clavier = new Scanner(System.in);
					msg_out = clavier.nextLine(); 
					out.write(msg_out.getBytes());
					
				}
				catch (IOException ex) { }
			}
		}
		finally {
			try {
				sockcli.close();
			}
			catch (IOException ex) { }
			try {
				sockserv.close();
			}
			catch (IOException ex) { }
			clavier.close();
		}
	}
	
}
	
