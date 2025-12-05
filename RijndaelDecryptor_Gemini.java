import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64; // Correct import for standard Java

public class RijndaelDecryptor_Gemini {

    private static final String ALGORITHM = "AES";
    private static final String MODE_PADDING = "AES/CBC/PKCS5Padding";

    /**
     * Decrypts a password string encrypted with Rijndael (AES) and CBC mode.
     * @param encryptedDataBase64 The password string received from the server (Base64 encoded).
     * @param keyBytes The secret symmetric key (must match server's key).
     * @param ivBytes The Initialization Vector (IV) bytes (must be sent by the server).
     * @return The original, decrypted password string.
     */
    public static String decryptPassword(String encryptedDataBase64, byte[] keyBytes, byte[] ivBytes) throws Exception {
        
        // 1. Convert the Base64 String back to the raw ciphertext bytes
        // FIX: Use standard Java Base64 Decoder
        byte[] ciphertext = Base64.getDecoder().decode(encryptedDataBase64);

        // 2. Prepare the Key and IV Specifications
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // 3. Instantiate the Cipher (The transformation MUST match the server)
        Cipher cipher = Cipher.getInstance(MODE_PADDING);

        // 4. Initialize the Cipher for Decryption
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // 5. Perform the Decryption
        byte[] decryptedBytes = cipher.doFinal(ciphertext);

        // 6. Convert the resulting bytes back to a String
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // --- Example Usage (Replace with your actual data) ---
    public static void main(String[] args) {
        // Dummy data for demonstration. In a real scenario, this data comes from the server.
        try {
            // Must be 16, 24, or 32 bytes (128, 192, or 256 bits)
            byte[] serverKey = "ThisIsASecretKey".getBytes(StandardCharsets.UTF_8); 
            // Must be 16 bytes for CBC mode
            byte[] serverIV = "RandomIV12345678".getBytes(StandardCharsets.UTF_8); 
            
            // Example Base64 ciphertext (The actual data will be different)
            String dummyEncrypted = Base64.getEncoder().encodeToString("The_Secret_Password".getBytes(StandardCharsets.UTF_8));
            
            String password = decryptPassword(dummyEncrypted, serverKey, serverIV);
            
            System.out.println("Dummy Encrypted Data: " + dummyEncrypted);
            System.out.println("Decrypted Password: " + password);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
