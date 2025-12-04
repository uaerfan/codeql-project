import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPServer {
    // Define a port number for the server
    private static final int PORT = 3248;
    // Define the base directory for file storage (change this path)
    private static final String SERVER_FILE_DIR = "C:\\server_files\\";

    public static void main(String args[]) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(PORT);
        System.out.println("Server running on port " + PORT);

        // Ensure the directory exists
        new File(SERVER_FILE_DIR).mkdirs();

        while (true) {
            Socket connectionSocket = null;
            try {
                // 1. Accept a new connection
                connectionSocket = welcomeSocket.accept();
                System.out.println("Client connected: " + connectionSocket.getInetAddress());

                // Use a dedicated reader/writer for command strings
                BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(
                    connectionSocket.getOutputStream());

                String clientCommand = inFromClient.readLine();
                System.out.println("Received command: " + clientCommand);

                // --- Handle Commands ---
                
                if (clientCommand.startsWith("DOWNLOAD:")) {
                    String fileName = clientCommand.substring("DOWNLOAD:".length());
                    sendFileToClient(connectionSocket, fileName, outToClient);

                } else if (clientCommand.startsWith("UPLOAD:")) {
                    String fileName = clientCommand.substring("UPLOAD:".length());
                    receiveFileFromClient(connectionSocket, fileName, inFromClient);

                } else {
                    // Default case (like your original uppercase logic)
                    String capitalizedSentence = clientCommand.toUpperCase() + "\n";
                    outToClient.writeBytes("FROM SERVER: " + capitalizedSentence);
                }

            } catch (Exception e) {
                System.err.println("Server error: " + e.getMessage());
            } finally {
                if (connectionSocket != null) {
                    connectionSocket.close();
                    System.out.println("Connection closed.");
                }
            }
        }
    }

    // --- Server Helper Methods ---

    private static void sendFileToClient(Socket socket, String fileName, DataOutputStream outToClient) throws Exception {
        File file = new File(SERVER_FILE_DIR + fileName);
        if (!file.exists()) {
            outToClient.writeBytes("FILE_NOT_FOUND\n");
            System.out.println("File not found: " + fileName);
            return;
        }

        // 1. Tell client the file size
        long fileSize = file.length();
        outToClient.writeBytes("FILE_SIZE:" + fileSize + "\n");
        
        // 2. Send the file bytes
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int read;
            OutputStream os = socket.getOutputStream();

            while ((read = fis.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
            os.flush();
            System.out.println("Sent file: " + fileName + " (" + fileSize + " bytes)");
        }
    }

    private static void receiveFileFromClient(Socket socket, String fileName, BufferedReader inFromClient) throws Exception {
        // The client must send the size before the file bytes
        String sizeHeader = inFromClient.readLine();
        if (!sizeHeader.startsWith("FILE_SIZE:")) {
            System.err.println("Missing file size header.");
            return;
        }

        long fileSize = Long.parseLong(sizeHeader.substring("FILE_SIZE:".length()));
        File newFile = new File(SERVER_FILE_DIR + fileName);
        
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[1024];
            long bytesRemaining = fileSize;
            int bytesRead;

            while (bytesRemaining > 0 && (bytesRead = is.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
                fos.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
            
            System.out.println("Received file: " + fileName + " (" + fileSize + " bytes)");
        }
    }
}
