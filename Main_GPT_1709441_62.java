
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Main_GPT_1709441_62 {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Generate RSA keypair with 512 bits
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Get the public key as a byte array
        PublicKey publicKey = keyPair.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();

        // Encode the public key as a string
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKeyBytes);

        // Print the encoded public key
        System.out.println("Encoded Public Key: " + encodedPublicKey);
    }
}
