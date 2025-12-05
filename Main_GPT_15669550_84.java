
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class Main_GPT_15669550_84 {
    public static void main(String[] args) {
        try {
            String ss = "9a";
            byte[] ba = hexStringToByteArray(ss);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(ba);
            String results = byteArrayToHexString(digest);
            System.out.println("sha:" + results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for(byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
