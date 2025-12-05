
import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClientGPT_03 {

    public static void main(String args[]) throws Exception {
        int filesize = 6022386;
        int bytesRead;
        int current = 0;
        String ipAdd = "";
        int portNum = 0;
        boolean goes = false;
        if (goes == false) {
            System.out.println("please input the ip address of the file server");
            Scanner scan = new Scanner(System.in);
            ipAdd = scan.nextLine();
            System.out.println("please input the port number of the file server");
            Scanner scan1 = new Scanner(System.in);
            portNum = scan1.nextInt();
            goes = true;
        }
        System.out.println("input done");
        int timeCount = 1;
        while (goes == true) {
            String sentence = "";
            String modifiedSentence;

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
                    System.in));

            Socket clientSocket = new Socket(ipAdd, portNum);
            
            if (timeCount == 1) {
                sentence = "set";
            } else {
                sentence = inFromUser.readLine();
            }
            
            if (sentence.equals("close")) {
                clientSocket.close();
            } else if (sentence.equals("download")) {
                InputStream is = clientSocket.getInputStream();
                FileOutputStream fos = new FileOutputStream("C:\\users\\cguo\\kk.lsp");
                byte[] buffer = new byte[filesize];
                int bytesReadTotal = 0;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    bytesReadTotal += bytesRead;
                }
                System.out.println("File downloaded successfully. Total bytes read: " + bytesReadTotal);
                fos.close();
            } else {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                outToServer.writeBytes(sentence + "\n");
                modifiedSentence = inFromServer.readLine();
                
                System.out.println("FROM SERVER: " + modifiedSentence);
                
                clientSocket.close();
            }
            
            timeCount--;
        }
    }
}

