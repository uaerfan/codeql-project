import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient {
    private static final int PORT = 3248;
    // Define the client's file storage directory (CHANGE THIS PATH!)
    private static final String CLIENT_FILE_DIR = "C:\\client_files\\";

    public static void main(String args[]) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String ipAdd;
        int portNum = PORT; // Use the default port

        // Get IP address only once
        System.out.println("Please input the IP address of the file server (e.g., localhost):");
        ipAdd = scanner.nextLine();
        
        // Ensure client directory exists
        new File(CLIENT_FILE_DIR).mkdirs();

        System.out.println("Input done. Connecting to " + ipAdd + ":" + portNum);

        while (true) {
            System.out.print("\nEnter command (DOWNLOAD:file.txt, UPLOAD:file.txt, or message): ");
            String userInput = scanner.nextLine();
            
            if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("close")) {
                break;
            }

            Socket clientSocket = null;
            try {
                clientSocket = new Socket(ipAdd, portNum);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                if (userInput.startsWith("DOWNLOAD:")) {
                    String fileName = userInput.substring("DOWNLOAD:".length());
                    
                    // 1. Send Command
                    outToServer.writeBytes(userInput + "\n");
                    
                    // 2. Wait for Server's File Size Header
                    String sizeHeader = inFromServer.readLine();
                    
                    if (sizeHeader == null || sizeHeader.equals("FILE_NOT_FOUND")) {
                        System.out.println("SERVER ERROR: File not found or connection lost.");
                    } else if (sizeHeader.startsWith("FILE_SIZE:")) {
                        long fileSize = Long.parseLong(sizeHeader.substring("FILE_SIZE:".length()));
                        receiveFileFromServer(clientSocket, fileName, fileSize);
                    }
                    
                } else if (userInput.startsWith("UPLOAD:")) {
                    String fileName = userInput.substring("UPLOAD:".length());
                    sendFileToServer(clientSocket, fileName, userInput, outToServer);
                    
                } else {
                    // Default message sending
                    outToServer.writeBytes(userInput + "\n");
                    String modifiedSentence = inFromServer.readLine();
                    System.out.println("FROM SERVER: " + modifiedSentence);
                }

            } catch (ConnectException e) {
                System.err.println("Could not connect to server. Is the server running?");
            } catch (FileNotFoundException e) {
                System.err.println("Error: The file " + e.getMessage() + " was not found in the client directory.");
            } catch (Exception e) {
                System.err.println("Client error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            }
        }
        scanner.close();
    }
    
    // --- Helper Method to Receive File (Download) ---
    private static void receiveFileFromServer(Socket socket, String fileName, long fileSize) throws Exception {
        File newFile = new File(CLIENT_FILE_DIR, fileName);
        
        System.out.println("Receiving file: " + fileName + " (" + fileSize + " bytes)...");
        
        try (FileOutputStream fos = new FileOutputStream(newFile);
             InputStream is = socket.getInputStream()) {
            
            byte[] buffer = new byte[8192];
            long bytesRemaining = fileSize;
            int bytesRead;

            // Loop until all expected bytes have been read
            while (bytesRemaining > 0 && 
                   (bytesRead = is.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
                
                fos.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
            
            System.out.println("Download complete! Saved to: " + newFile.getAbsolutePath());
        }
    }

    // --- Helper Method to Send File (Upload) ---
    private static void sendFileToServer(Socket socket, String fileName, String command, DataOutputStream outToServer) throws Exception {
        File file = new File(CLIENT_FILE_DIR, fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(fileName);
        }

        // 1. Send the UPLOAD command
        outToServer.writeBytes(command + "\n");
        
        // 2. Send the file size header so the server knows when to stop reading
        long fileSize = file.length();
        outToServer.writeBytes("FILE_SIZE:" + fileSize + "\n");
        
        // 3. Stream the file bytes
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = socket.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int read;

            while ((read = fis.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
            os.flush();
            System.out.println("Upload complete! Sent " + fileSize + " bytes.");
        }
    }
}
