
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.net.ssl.HttpsURLConnection;

public class DownloadFileExample_GPT_37562926_61 {
    public static void download(String downloadURL) throws IOException {
        URL website = new URL(downloadURL);
        String fileName = "downloaded.zip";

        // Create a new instance of HttpsURLConnection
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
        HttpsURLConnection conn = (HttpsURLConnection) website.openConnection();

        // Trust the SSL certificate of the server by adding it to the Java keystore
        conn.setSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());

        try (InputStream inputStream = conn.getInputStream()) {
            Files.copy(inputStream, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void main(String[] arguments) throws IOException {
        String downloadURL = "https://mh-nexus.de/downloads/HxDSetupEN.zip";
        download(downloadURL);
    }
}
