
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class EncryptedLogger_GPT_10283637_25 {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/NoPadding";
    private static final byte[] KEY = "0123456789abcdef".getBytes(); // Your secret key
    private static final int BLOCK_SIZE = 16; // Size of the encryption block in bytes

    public static void main(String[] args) throws Exception {
        String logFileName = "encrypted_log.log";

        // Append some messages to the encrypted log file
        appendToEncryptedLog(logFileName, "First message");
        appendToEncryptedLog(logFileName, "Second message");

        // Decrypt and print the contents of the log file
        System.out.println(decryptLogFile(logFileName));
    }

    public static void appendToEncryptedLog(String logFileName, String message) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));

        byte[] encryptedMessage = cipher.doFinal(padMessage(message).getBytes());

        try (OutputStream outputStream = new FileOutputStream(logFileName, true)) {
            outputStream.write(encryptedMessage);
            outputStream.flush();
        }
    }

    public static String decryptLogFile(String logFileName) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));

        try (InputStream inputStream = new FileInputStream(logFileName)) {
            byte[] encryptedMessage = inputStream.readAllBytes();
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

            return stripPadding(new String(decryptedMessage));
        }
    }

    private static String padMessage(String message) {
        int remainder = message.length() % BLOCK_SIZE;
        int paddingLength = remainder == 0 ? BLOCK_SIZE : BLOCK_SIZE - remainder;
        StringBuilder paddedMessage = new StringBuilder(message);
        for (int i = 0; i < paddingLength; i++) {
            paddedMessage.append(" ");
        }
        return paddedMessage.toString();
    }

    private static String stripPadding(String message) {
        return message.trim();
    }
}
