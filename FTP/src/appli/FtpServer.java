package appli;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FtpServer {

    static ServerSocket socket_server = null; //server socket
    static Socket socket_client = null; //client socket
    static DataOutputStream writer;
    static DataInputStream reader;
    static StringBuilder request = new StringBuilder();
    static Scanner sc;

    public static void openConnection() throws Exception {
        socket_server = new ServerSocket(12345); //create a new socket on port n°12345
        sc = new Scanner(System.in);
        socket_client = socket_server.accept(); //listen for a new client
        System.out.println("New client is trying to connect");

        reader = new DataInputStream(socket_client.getInputStream());
        writer = new DataOutputStream(socket_client.getOutputStream());

        writer.writeUTF("220\r\n");
        writer.flush();

        String username = reader.readUTF();
        username = username.substring(5).replaceAll("(\\r|\\n)", "");
        System.out.println("Username: " + username);

        writer.writeUTF("331\r\n");
        writer.flush();

        String password = reader.readUTF();
        password = password.substring(5).replaceAll("(\\r|\\n)", "");
        System.out.println("Password: " + password);

        if (!username.equals("ffctlr") || !password.equals("rsx_2018")) {
            writer.writeUTF("530\r\n");
            writer.flush();
            socket_client.close(); //close client's socket
            System.out.println("Client not able to connect");
        } else {
            writer.writeUTF("230\r\n");
            writer.flush();
            System.out.println("Client connected...");
        }
    }

    public static void closeConnection() throws Exception {
        sc.close();
        reader.close();
        writer.close();
        socket_client.close();
        socket_server.close();
    }

    public static void run() throws Exception {

        while (true) {
            openConnection();

            while (true) {

                String request = reader.readUTF();

                String cmd = request;
                String arg = "";

                if (request.indexOf(" ") != -1) {
                    cmd = request.substring(0, request.indexOf(" "));
                    arg = request.substring(request.indexOf(" ") + 1).replaceAll("(\\r|\\n)", "");
                }

                switch (cmd) {
                    case "STOR":
                        //wait for file size
                        int fileSize = reader.readInt();
                        //if -1, file does not exist
                        if (fileSize == -1) {
                            System.out.println("Client tried to upload " + arg + " but file does not exist.");
                            break;
                        }

                        System.out.println("STOR " + arg + "...");
                        //create an array of byte with size of fileSize
                        byte[] fileContent = new byte[fileSize];
                        //read file bytes and store them in fileContent byte[]
                        reader.readFully(fileContent, 0, fileSize);
                        String fileName = "copy-" + arg.substring(arg.lastIndexOf("/") + 1).replaceAll("(\\r|\\n)", "");
                        FileOutputStream fileWriter = new FileOutputStream(new File(fileName));
                        fileWriter.write(fileContent, 0, fileContent.length);
                        fileWriter.close();
                        System.out.println("File " + fileName + " has been uploaded!");
                        break;

                    case "RETR":

                        File file = new File(arg);

                        if (!file.exists()) {
                            writer.writeInt(-1);
                            writer.flush();
                            System.out.println("Client ask for file " + arg + " but file does not exist.");
                            break;
                        }

                        Path path = Paths.get(arg);
                        System.out.println("RETR " + path.toString() + "...");
                        fileContent = Files.readAllBytes(path);
                        writer = new DataOutputStream(socket_client.getOutputStream());
                        writer.writeInt(fileContent.length);
                        writer.flush();
                        writer.write(fileContent, 0, fileContent.length);
                        writer.flush();
                        System.out.println("File " + request.substring(request.lastIndexOf("/") + 1).replaceAll("(\\r|\\n)", "") + " has been sent!");
                        break;


                    case "QUIT\r\n":
                        closeConnection();
                        System.out.println("Client disconnected");
                        System.exit(1);
                        break;

                    default:
                        writer.writeUTF("Command does not exist!\r\n");
                        System.out.println("Client ask for command " + cmd.replaceAll("(\\r|\\n)", "") + " but command does not exist.");
                        break;
                }
                closeConnection();
                openConnection();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("***FTP SERVER RUNNING ON PORT N°12345***\n");
        run();
    }

}
