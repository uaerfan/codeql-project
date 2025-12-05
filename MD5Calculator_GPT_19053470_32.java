
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Calculator_GPT_19053470_32 {
    public static void main(String[] args) {
        String cadena = "109370";
        try {
            MessageDigest di = MessageDigest.getInstance("MD5");
            di.update(cadena.getBytes());
            byte[] mdi = di.digest();

            StringBuffer md5 = new StringBuffer();
            for (byte b : mdi) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    md5.append("0");
                }
                md5.append(hex);
            }

            System.out.println(md5.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
