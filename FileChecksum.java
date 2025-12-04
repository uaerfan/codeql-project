import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileChecksum {

    /**
     * Creates a raw byte array checksum for a file using the specified algorithm.
     * @param filename The path to the file.
     * @param algorithm The cryptographic algorithm (e.g., "MD5", "SHA-256").
     * @return The raw byte array representing the checksum.
     * @throws Exception if the file is not found or the algorithm is invalid.
     */
    public static byte[] createChecksum(String filename, String algorithm) throws Exception {
        // 1. Open the file as an InputStream
        try (InputStream fis = new FileInputStream(filename)) {
            
            // 2. Get the MessageDigest instance
            MessageDigest complete = MessageDigest.getInstance(algorithm);
            
            byte[] buffer = new byte[1024];
            int numRead;

            // 3. Read the file in chunks and update the digest
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            // 4. Return the final digest (checksum)
            return complete.digest();
        }
    }

    /**
     * Converts a file's checksum byte array into a hexadecimal String.
     * @param filename The path to the file.
     * @return The MD5 checksum as a 32-character hexadecimal String.
     * @throws Exception if the file is not found.
     */
    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename, "MD5");
        
        // Convert the byte array to a hexadecimal string
        StringBuilder result = new StringBuilder();
        for (byte by : b) {
            // Converts the byte to an unsigned integer (0-255) and formats it as a two-digit hex string
            result.append(String.format("%02x", by));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        // NOTE: Replace "path/to/your/file.txt" with an actual file path for testing.
        String filePath = "MD5Checksum.java"; // Or any file in your project
        
        try {
            String md5 = getMD5Checksum(filePath);
            System.out.println("MD5 Checksum of " + filePath + ": " + md5);
        } catch (Exception e) {
            System.err.println("Error calculating checksum: " + e.getMessage());
        }
    }
}
