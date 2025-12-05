
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DecryptExample_GPT_10357630_21 {
    public static void main(String[] args) throws Exception {
        byte[] sessionKey = null; // Where you get this from is beyond the scope of this post
        byte[] iv = null; // Ditto
        String encryptedPassword = ""; // The password retrieved from the server and encrypted
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        // Decrypt the password
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
        byte[] decryptedPassword = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        String password = new String(decryptedPassword, "UTF-8");
        
        System.out.println("Decrypted Password: " + password);
    }
}
