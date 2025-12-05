
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncryptionExample_GPT_10303767_26 {

    private static final String ALGORITHM = "AES";
    private static final String KEY_FILE = "key.txt";
    private static final String PASSWORD_FILE = "encrypted_password.txt";
    private static final String PRIVATE_KEY = "your_private_key_here"; // Replace with your own private key

    public static void main(String[] args) {
        // Generate and store the key
        Key key = generateKey(PRIVATE_KEY);
        storeKey(key, KEY_FILE);

        // Encrypt and store the password
        String password = "your_password_here"; // Replace with the password to be encrypted
        encryptAndStorePassword(password, key);
        
        // Decrypt the password
        String decryptedPassword = decryptPassword(key);
        System.out.println("Decrypted Password: " + decryptedPassword);
    }

    private static Key generateKey(String privateKey) {
        try {
            byte[] keyBytes = privateKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            keyBytes = sha.digest(keyBytes);
            return new SecretKeySpec(keyBytes, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void storeKey(Key key, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] keyBytes = key.getEncoded();
            fos.write(keyBytes);
            System.out.println("Key stored successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void encryptAndStorePassword(String password, Key key) {
        try (FileOutputStream fos = new FileOutputStream(PASSWORD_FILE)) {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedPassword = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            fos.write(Base64.getEncoder().encode(encryptedPassword));
            System.out.println("Password encrypted and stored successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String decryptPassword(Key key) {
        try (FileInputStream fis = new FileInputStream(PASSWORD_FILE)) {
            byte[] encryptedPasswordBytes = fis.readAllBytes();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedPasswordBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPasswordBytes));
            return new String(decryptedPasswordBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
