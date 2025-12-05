
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor_GPT_13488155_18 {
    // Change these values as per your needs
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_DERIVATION = "PBKDF2WithHmacSHA256";
    private static final int KEY_SIZE = 128;
    private static final int ITERATION_COUNT = 65536;
    private static final byte[] SALT = new byte[] { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

    public static void encryptFolder(File folder, String encryptedFolder) throws Exception {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Input folder is not a directory");
        }
        
        File[] files = folder.listFiles();
        if (files == null) {
            throw new IllegalStateException("Failed to list files in input folder");
        }
        
        for (File inputFile : files) {
            if (inputFile.isFile()) {
                File outputFile = new File(encryptedFolder, inputFile.getName() + ".enc");
                encryptFile(inputFile, outputFile);
            }
        }
    }

    public static void decryptFolder(String encryptedFolder, File folder) throws Exception {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Output folder is not a directory");
        }
        
        File[] files = new File(encryptedFolder).listFiles();
        if (files == null) {
            throw new IllegalStateException("Failed to list files in encrypted folder");
        }
        
        for (File inputFile : files) {
            if (inputFile.isFile()) {
                String outputFileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".enc"));
                File outputFile = new File(folder, outputFileName);
                decryptFile(inputFile, outputFile);
            }
        }
    }

    private static void encryptFile(File inputFile, File outputFile) throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            byte[] key = generateKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            
            byte[] iv = cipher.getIV();
            fileOutputStream.write(iv.length);
            fileOutputStream.write(iv);
            
            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher)) {
                copyStream(fileInputStream, cipherOutputStream);
            }
        }
    }

    private static void decryptFile(File inputFile, File outputFile) throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            int ivLength = fileInputStream.read();
            byte[] iv = new byte[ivLength];
            fileInputStream.read(iv);
            
            byte[] key = generateKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new javax.crypto.spec.IvParameterSpec(iv));
            
            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher)) {
                copyStream(fileInputStream, cipherOutputStream);
            }
        }
    }

    private static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
    }

    private static byte[] generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION);
        SecretKey key = factory.generateSecret(
                new PBEKeySpec("your_password".toCharArray(), SALT, ITERATION_COUNT, KEY_SIZE));
        return key.getEncoded();
    }

    public static void main(String[] args) {
        try {
            String inputFolderPath = "C:/InputFolder";
            String encryptedFolderPath = "C:/EncryptedFolder";
            String outputFolderPath = "C:/OutputFolder";
            
            encryptFolder(new File(inputFolderPath), encryptedFolderPath);
            decryptFolder(encryptedFolderPath, new File(outputFolderPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
