
import java.io.*;
import java.net.*;

class TCPServer {

    public static void main(String args[]) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(3248);
        System.out.println("Server started!");
        
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("New client connected: " + connectionSocket.getInetAddress().getHostAddress());
            
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            
            String clientSentence = inFromClient.readLine();
            String capitalizedSentence = "";
            
            if (clientSentence.equals("download")) {
                File myFile = new File("C:\\Users\\cguo\\11.lsp");
                byte[] mybytearray = new byte[(int) myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                OutputStream os = connectionSocket.getOutputStream();
                System.out.println("Sending file to client...");
                os.write(mybytearray, 0, mybytearray.length);
                os.flush();
            } else {
                capitalizedSentence = clientSentence.toUpperCase() + "\n";
                outToClient.writeBytes(capitalizedSentence);
            }
            
            outToClient.close();
            inFromClient.close();
            connectionSocket.close();
        }
    }
}

