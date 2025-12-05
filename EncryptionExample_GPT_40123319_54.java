
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionExample_GPT_40123319_54 {

    public static void main(String[] args) throws Exception {
        String AndroidId = "00:00:00:00:00:00"; // Replace with actual MAC address

        String key = "0123456789abcdef"; // 16-byte encryption key (can be any value)
        String encrypted = encrypt(AndroidId, key);
        System.out.println("Encrypted: " + encrypted);
    }

    public static String encrypt(String data, String key) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
