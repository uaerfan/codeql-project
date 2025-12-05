import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class FileEncryptor_Gemini {
    
    // --- Configuration (Recommended to keep these values) ---
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_DERIVATION = "PBKDF2WithHmacSHA256";
    private static final int KEY_SIZE = 128; // 128-bit AES
    private static final int ITERATION_COUNT = 65536;
    
    // Salt MUST be stored somewhere and consistent. DO NOT CHANGE THIS!
    private static final byte[] SALT = new byte[] { 
        0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF 
    };
    
    private static final String MASTER_PASSWORD = "YOUR_MASTER_PASSWORD"; // <-- CHANGE THIS!

    // --- Utility Methods ---

    /** Derives a secure key from a password using PBKDF2. */
    private static byte[] generateKey(String password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION);
        SecretKey key = factory.generateSecret(
            new PBEKeySpec(password.toCharArray(), SALT, ITERATION_COUNT, KEY_SIZE));
        return key.getEncoded();
    }
    
    /** Copies data between two streams. */
    private static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
    }

    // --- 1. ENCRYPTION METHODS ---

    /** Encrypts a single file and saves the output, including the IV prefix. */
    public static void encryptFile(File inputFile, File outputFile) throws Exception {
        byte[] key = generateKey(MASTER_PASSWORD);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        
        // Cipher generates a new random IV for each encryption (critical for security)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec); 
        
        // Get the IV that was generated for this encryption
        byte[] iv = cipher.getIV();
        
        try (FileInputStream fileInputStream = new FileInputStream(inputFile);
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
             
            // Write the IV length and IV to the start of the output file
            fileOutputStream.write(iv.length);
            fileOutputStream.write(iv);

            // Write the encrypted file content
            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher)) {
                copyStream(fileInputStream, cipherOutputStream);
            }
        }
    }

    /** Encrypts all files in a source folder into a destination folder. */
    public static void encryptFolder(File folder, String encryptedFolder) throws Exception {
        if (!folder.isDirectory()) throw new IllegalArgumentException("Input folder is not a directory");
        File destDir = new File(encryptedFolder);
        if (!destDir.exists()) destDir.mkdirs();

        File[] files = folder.listFiles();
        if (files == null) throw new IllegalStateException("Failed to list files in input folder");

        for (File inputFile : files) {
            if (inputFile.isFile()) {
                File outputFile = new File(encryptedFolder, inputFile.getName() + ".enc");
                encryptFile(inputFile, outputFile);
                System.out.println("Encrypted: " + inputFile.getName());
            }
        }
    }

    // --- 2. DECRYPTION METHOD (In-Memory for Swing) ---

    /**
     * Decrypts the content of an encrypted file directly into a String.
     * This avoids writing plaintext to the hard drive.
     * * @param encryptedFile The encrypted file to read.
     * @return The plaintext content (HTML source).
     */
    public static String decryptFileContent(File encryptedFile) throws Exception {
        byte[] key = generateKey(MASTER_PASSWORD);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        
        try (FileInputStream fileInputStream = new FileInputStream(encryptedFile)) {
            
            // 1. Read the IV length and IV
            int ivLength = fileInputStream.read();
            if (ivLength == -1) throw new IOException("Encrypted file is empty.");
            byte[] iv = new byte[ivLength];
            if (fileInputStream.read(iv) != ivLength) throw new IOException("IV read failure.");

            // 2. Initialize Cipher for Decryption
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

            // 3. Decrypt the remaining stream content into an in-memory buffer
            ByteArrayOutputStream decryptedStream = new ByteArrayOutputStream();
            try (CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher)) {
                copyStream(cipherInputStream, decryptedStream);
            }

            // 4. Convert buffer to String (HTML content)
            return new String(decryptedStream.toByteArray(), StandardCharsets.UTF_8);
        }
    }


    public static void main(String[] args) {
        // --- DEMO ---
        // 1. Setup paths
        String inputFolderPath = "html_source";
        String encryptedFolderPath = "encrypted_resources";
        
        // Create dummy HTML file for testing
        try {
            File inputDir = new File(inputFolderPath);
            inputDir.mkdirs();
            Path testFile = Path.of(inputFolderPath, "welcome.html");
            Files.writeString(testFile, "<html><body><h1>Hello Encrypted World!</h1></body></html>");

            // 2. Encrypt the entire folder
            System.out.println("Starting encryption...");
            encryptFolder(inputDir, encryptedFolderPath);
            System.out.println("Encryption complete. Check '" + encryptedFolderPath + "' folder.");

            // 3. Demo Decrypting and using the content (Simulation of Swing app usage)
            File encryptedFile = new File(encryptedFolderPath, "welcome.html.enc");
            String htmlContent = decryptFileContent(encryptedFile);

            System.out.println("\n--- Decryption Test (In-Memory Content) ---");
            System.out.println(htmlContent.substring(0, Math.min(htmlContent.length(), 50)) + "...");
            System.out.println("------------------------------------------");

            // 4. Clean up test files (Optional)
            Files.delete(testFile);
            Files.delete(encryptedFile.toPath());
            Files.delete(inputDir.toPath());
            Files.delete(new File(encryptedFolderPath).toPath());
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
