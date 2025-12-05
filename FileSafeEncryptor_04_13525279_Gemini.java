import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64; // Correct import for standard Java
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypts strings and encodes the result using Base64 URL_SAFE for use in file paths.
 */
public class FileSafeEncryptor_04_13525279_Gemini {

    // --- Configuration ---
    private static final String ALGORITHM = "AES";
    private static final String MODE = "AES/ECB/PKCS5Padding";


    // --- Key Generation (Safer Alternative) ---
    private static SecretKeySpec getKeySpec(String seed) throws Exception {
        // Use SHA-256 hash of the seed to generate a consistent, properly sized 
        // 16-byte (128-bit) key for AES.
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(seed.getBytes(StandardCharsets.UTF_8));
        key = Arrays.copyOf(key, 16); // Use only the first 16 bytes for AES-128
        
        return new SecretKeySpec(key, ALGORITHM);
    }

    // --- ENCRYPTION (Creates the File Path Segment) ---
    /**
     * Encrypts the plaintext and encodes the result as a URL- and File-safe string.
     * @param seed The secret string used to derive the encryption key.
     * @param cleartext The string to encrypt (e.g., "my_secret_folder").
     * @return The encrypted and Base64 URL-safe string.
     */
    public static String encrypt(String seed, String cleartext) throws Exception {
        SecretKeySpec secretKey = getKeySpec(seed);
        Cipher cipher = Cipher.getInstance(MODE);
        // Corrected: Use Cipher.ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, secretKey); 

        // 1. Encrypt the plaintext
        byte[] cipherText = cipher.doFinal(cleartext.getBytes(StandardCharsets.UTF_8));

        // 2. Encode using STANDARD JAVA Base64 URL Encoder.
        return Base64.getUrlEncoder().withoutPadding().encodeToString(cipherText);
    }

    // --- DECRYPTION (Reads the File Path Segment) ---
    /**
     * Decodes the encrypted path segment and decrypts it back to the original string.
     * @param seed The secret string used to derive the encryption key.
     * @param encryptedPathSegment The string from the file path.
     * @return The original decrypted string.
     */
    public static String decrypt(String seed, String encryptedPathSegment) throws Exception {
        SecretKeySpec secretKey = getKeySpec(seed);
        Cipher cipher = Cipher.getInstance(MODE);
        // FIX: Must reference the constant from the Cipher class
        cipher.init(Cipher.DECRYPT_MODE, secretKey); 

        // 1. Decode the Base64 URL_SAFE string back to binary using STANDARD JAVA Decoder
        byte[] cipherText = Base64.getUrlDecoder().decode(encryptedPathSegment);
        
        // 2. Decrypt the binary data
        byte[] decryptedBytes = cipher.doFinal(cipherText);
        
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    public static void main(String[] args) {
        // --- Demonstration ---
        String seedKey = "MySuperSecretAppKey0123456789";
        String originalPath = "user_data_12345/session-a"; 
        String filePathPrefix = "/tmp/MyApp/";
        String filePathSuffix = "/config.json";

        try {
            // 1. Encrypt and get the file-safe segment
            String encryptedSegment = encrypt(seedKey, originalPath);
            
            // 2. Use it in the file path
            String fullFilePath = filePathPrefix + encryptedSegment + filePathSuffix;

            System.out.println("Original Path String: " + originalPath);
            System.out.println("Encrypted Path Segment (File-Safe): " + encryptedSegment);
            System.out.println("Full File Path Used: " + fullFilePath);
            
            // 3. Decrypt the segment retrieved from the path
            String decryptedPath = decrypt(seedKey, encryptedSegment);
            
            System.out.println("\nDecrypted String: " + decryptedPath);
            
            // Check for success
            if (originalPath.equals(decryptedPath)) {
                System.out.println("SUCCESS: Encryption and Decryption match.");
            } else {
                System.out.println("FAILURE: Strings do not match.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
