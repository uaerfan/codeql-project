
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Gen_DESAES_key_GPT_44047862_66 {

    public static void main(String[] args) throws Exception {
        byte[] message = "Hello World".getBytes();

        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        SecretKey desKey = keyGenerator.generateKey();

        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);

        byte[] encryptedMessage = desCipher.doFinal(message);
    }
}
