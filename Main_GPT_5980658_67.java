
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main_GPT_5980658_67 {

    public static void main(String[] args) {
        String stringToHash = "Hello World";
        String hashedString = sha1(stringToHash);
        System.out.println(hashedString);
    }

    public static String sha1(String stringToHash) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = messageDigest.digest(stringToHash.getBytes());
            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : bytes) {
                stringBuilder.append(String.format("%02X", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
