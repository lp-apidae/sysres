package appli;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class FtpClient {

    static Socket socket_client = null; //Socket to connect with Server
    static InetAddress addr; //Used to get host name from IP address
    static Scanner sc = new Scanner(System.in);
    static DataOutputStream writer;
    static DataInputStream reader;
    static String response;
    static String ip;
    static int port;
    static String username;
    static String password;
    static StringBuilder resp = new StringBuilder();

    //function to handle connection with server
    public static void openConnection() throws Exception {

        System.out.print("Enter an IP address: ");
        ip = sc.nextLine(); //Server IP address to reach
        System.out.print("\nEnter a port number: ");
        String stringPort = sc.nextLine();
        port = Integer.parseInt(stringPort); //PORT number
        socket_client = new Socket(ip, port);

        System.out.print("\nCONNECTING TO SERVER " + ip + " with port n°" + port);

        for (int i = 0; i < 20; i++) {
            System.out.print(".");
            TimeUnit.MILLISECONDS.sleep(50);
        }

        reader = new DataInputStream(socket_client.getInputStream());

        response = reader.readUTF(); //read server response

        if (!response.startsWith("220")) {
            System.out.println("\n\nERROR: COULDN'T REACH FTP SERVER.\n");
            System.exit(-1);
        }

        //If server response code is 220, ask for a username
        System.out.print("\n\nEnter a username: ");
        username = sc.nextLine();

        writer = new DataOutputStream(socket_client.getOutputStream());

        //send username to server
        writer.writeUTF("USER " + username + "\r\n");
        writer.flush();

        response = reader.readUTF(); //read server response

        if (!response.startsWith("331")) {
            System.out.println("\n\nError: " + response);
            System.exit(-1);
        }

        //If server response code is 331, ask for a password
        System.out.print("\nEnter a password: ");
        password = sc.nextLine();

        writer.writeUTF("PASS " + password + "\r\n");
        writer.flush();

        response = reader.readUTF(); //read server response

        if (!response.startsWith("230")) {
            System.out.println("\n\nError: " + response);
            System.exit(-1);
        }

        //if server response code is 230, inform client that connection with server is now open
        System.out.println("\nCONNECTED TO FTP SERVER!\n");

    }

    public static void openConnection(String ip, int port, String username, String password) throws Exception {

        socket_client = new Socket(ip, port);

        reader = new DataInputStream(socket_client.getInputStream());

        response = reader.readUTF(); //read server response

        if (!response.startsWith("220")) {
            System.out.println("\n\nERROR: COULDN'T REACH FTP SERVER.\n");
            System.exit(-1);
        }

        writer = new DataOutputStream(socket_client.getOutputStream());

        //send username to server
        writer.writeUTF("USER " + username + "\r\n");
        writer.flush();

        response = reader.readUTF(); //read server response

        if (!response.startsWith("331")) {
            System.out.println("\n\nError: " + response);
            System.exit(-1);
        }

        writer.writeUTF("PASS " + password + "\r\n");
        writer.flush();

        response = reader.readUTF(); //read server response

        if (!response.startsWith("230")) {
            System.out.println("\n\nError: " + response);
            System.exit(-1);
        }
    }

    //function called to close connection with server (close input and output streams + close socket)
    public static void closeConnection() throws Exception {
        reader.close();
        writer.close();
        socket_client.close();
    }

    public static void run() throws Exception {

        //see FtpClient.openConnection()
        openConnection();


        while (true) {
            System.out.print("Enter an FTP command: ");
            String request = "";
            request = sc.nextLine();
            writer.writeUTF(request + "\r\n");
            writer.flush();

            String cmd = request;
            String arg = "";

            if (request.indexOf(' ') != -1) {
                cmd = request.substring(0, request.indexOf(" "));
                arg = request.substring(request.indexOf(" ") + 1).replaceAll("(\\r|\\n)", "");
            }

            switch (cmd) {
                case "STOR":
                    File file = new File(arg);

                    if (!file.exists()) {
                        writer.writeInt(-1);
                        writer.flush();
                        System.out.println("\nFile " + request.substring(request.lastIndexOf("/") + 1) + " does not exist.\n");
                        break;
                    }

                    Path path = Paths.get(arg);
                    byte[] fileContent = Files.readAllBytes(path);
                    writer = new DataOutputStream(socket_client.getOutputStream());
                    writer.writeInt(fileContent.length);
                    writer.flush();
                    writer.write(fileContent, 0, fileContent.length);
                    writer.flush();
                    System.out.println("\nFile " + request.substring(request.lastIndexOf("/") + 1) + " has been sent!\n");
                    break;

                case "RETR":
                    //wait for file size
                    int fileSize = reader.readInt();
                    //if -1, file does not exist
                    if (fileSize == -1) {
                        System.out.println("\nFile " + request.substring(request.lastIndexOf(" ") + 1) + " does not exist!\n");
                        break;
                    }
                    //create an array of byte with size of fileSize
                    fileContent = new byte[fileSize];
                    //read file bytes and store them in fileContent byte[]
                    reader.readFully(fileContent, 0, fileSize);
                    String fileName = "copy-" + arg.substring(arg.lastIndexOf("/") + 1);
                    FileOutputStream fileWriter = new FileOutputStream(new File(fileName));
                    fileWriter.write(fileContent, 0, fileContent.length);
                    fileWriter.close();
                    System.out.println("\nFile " + fileName + " has been uploaded!\n");
                    break;

                case "QUIT":
                    closeConnection();
                    sc.close();
                    System.out.println("\nCONNECTION CLOSED\n");
                    System.exit(1);

                default:
                    response = reader.readUTF();
                    System.out.println("\n" + response);
                    break;
            }
            //see FtpClient.closeConnection()
            closeConnection();
            Thread.sleep(100);
            openConnection(ip, port, username, password);
        }


    }

    public static void main(String[] args) throws Exception {
        System.out.println("***FTP CLIENT TO SERVE***\n");
        run();
    }
}
