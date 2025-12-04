import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileChecksumExample {

    public static void main(String[] args) {
        try {
            String filePath = "path/to/your/file.txt";
            String checksum = getFileChecksum(filePath);
            System.out.println("MD5 checksum: " + checksum);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileChecksum(String filePath) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filePath), md5Digest)) {
            while (dis.read() != -1) ;
            // DigestInputStream automatically updates the digest (MD5) in the background
            
            byte[] digest = md5Digest.digest();
            StringBuilder result = new StringBuilder();
            
            for (byte b : digest) {
                result.append(String.format("%02x", b)); // Convert byte to hexadecimal format
            }
            
            return result.toString();
        }
    }
}

