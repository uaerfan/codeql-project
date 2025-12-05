
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class EncryptionExample_GPT_5520640_70 {

    public static void main(String[] args) {
        try {
            String algorithm = "DES";
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);

            byte[] encBytes = "12345678".getBytes("UTF8");
            byte[] decBytes = "56781234".getBytes("UTF8");

            DESKeySpec keySpecEncrypt = new DESKeySpec(encBytes);
            DESKeySpec keySpecDecrypt = new DESKeySpec(decBytes);

            SecretKey keyEncrypt = keyFactory.generateSecret(keySpecEncrypt);
            SecretKey keyDecrypt = keyFactory.generateSecret(keySpecDecrypt);

            Cipher cipherEncrypt = Cipher.getInstance(algorithm);
            Cipher cipherDecrypt = Cipher.getInstance(algorithm);

            String input = "john doe";

            cipherEncrypt.init(Cipher.ENCRYPT_MODE, keyEncrypt);
            byte[] inputBytes = cipherEncrypt.doFinal(input.getBytes());
            System.out.println("Encrypted Bytes: " + Arrays.toString(inputBytes));

            cipherDecrypt.init(Cipher.DECRYPT_MODE, keyDecrypt);
            byte[] outputBytes = cipherDecrypt.doFinal(inputBytes);
            System.out.println("Decrypted Bytes: " + new String(outputBytes));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
