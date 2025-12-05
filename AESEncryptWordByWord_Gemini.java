import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.util.StringJoiner;

public class AESEncryptWordByWord_Gemini {

    /**
     * Converts array of bytes to a hex string.
     * @param buf Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    /**
     * Converts hex string to byte array.
     * Note: This method is not strictly needed for word-by-word encryption
     * but kept for completeness.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) throws Exception {

        String message = "Test text!";
        // Ensure the message has at least one space for splitting
        String inputMessage = (args.length == 0 ? message : args[0]);

        // 1. Key Generation (Same as your original code)
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); 
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        System.out.println("Original string: " + inputMessage);
        System.out.println("Key: " + asHex(raw));

        // 2. Encryption Setup
        // Using "AES/ECB/PKCS5Padding" is crucial here. The ECB mode ensures 
        // that encrypting "Test" multiple times will always yield the same ciphertext, 
        // and PKCS5Padding handles different word lengths.
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        // 3. Split, Encrypt, and Join
        String[] words = inputMessage.split(" ");
        StringJoiner encryptedWords = new StringJoiner(" ");

        for (String word : words) {
            if (!word.isEmpty()) {
                // Encrypt the bytes of the individual word
                byte[] encryptedBytes = cipher.doFinal(word.getBytes("UTF-8"));
                
                // Convert the encrypted bytes to Hex string
                String encryptedHex = asHex(encryptedBytes);
                
                // Add the hex string to the list, separated by a space
                encryptedWords.add(encryptedHex);
            }
        }
        
        System.out.println("Encrypted string: " + encryptedWords.toString());

        // --- DECRYPTION DEMO (Optional, but useful) ---
        System.out.println("\n--- Decryption Check ---");
        
        Cipher decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec);
        
        String[] encryptedHexWords = encryptedWords.toString().split(" ");
        StringJoiner decryptedWords = new StringJoiner(" ");
        
        for (String hexWord : encryptedHexWords) {
             if (!hexWord.isEmpty()) {
                // 1. Convert Hex string back to byte array
                byte[] encryptedBytes = hexStringToByteArray(hexWord);
                
                // 2. Decrypt the bytes
                byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);
                
                // 3. Convert bytes back to word
                String decryptedWord = new String(decryptedBytes, "UTF-8");
                decryptedWords.add(decryptedWord);
             }
        }
        System.out.println("Decrypted string: " + decryptedWords.toString());
    }
}
