
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

public class EncryptedFileWriter_GPT_10283637_22 {

    private static final String AES_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/NoPadding";
    private static final String KEY = "0123456789abcdef";

    public static void main(String[] args) {
        String filePath = "encrypted_log_file.txt";

        try {
            // Generate a secret key for encryption
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), AES_ALGORITHM);

            // Create a cipher instance
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Open the file in append mode
            FileOutputStream fileOutputStream = new FileOutputStream(filePath, true);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

            // Write messages to the file
            writeToEncryptedFile(cipherOutputStream, "Message 1");
            writeToEncryptedFile(cipherOutputStream, "Message 2");

            // Close the cipher output stream
            cipherOutputStream.close();

            // Decrypt and print the contents of the file
            decryptAndPrintFile(filePath);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void writeToEncryptedFile(CipherOutputStream cipherOutputStream, String message) throws IOException {
        cipherOutputStream.write(message.getBytes());
        cipherOutputStream.write('\n'); // Add a newline character as a delimiter
    }

    private static void decryptAndPrintFile(String filePath) throws IOException {
        try {
            // Read the encrypted file
            byte[] encryptedData = Files.readAllBytes(Paths.get(filePath));

            // Create a cipher instance for decryption
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), AES_ALGORITHM));

            // Decrypt the data
            byte[] decryptedData = cipher.doFinal(encryptedData);

            // Print the decrypted message
            System.out.println(new String(decryptedData));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
        }
    }
}
