
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;

public class ImportCertificate_GPT_29559215_65 {
    public static void main(String[] args) throws Exception {
        String hostname = "bctcl-parasuram.bctchn.local";
        int port = 8443;

        // Create a URL object for the website
        URL url = new URL("https://" + hostname + ":" + port);

        // Open a connection to the website
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        // Get the SSL certificate from the website
        Certificate certificate = connection.getServerCertificates()[0];

        // Create a keystore object
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

        // Load the default truststore
        keystore.load(null, null);

        // Add the SSL certificate to the keystore
        keystore.setCertificateEntry(hostname, certificate);

        // Save the keystore to a file
        try (FileOutputStream fos = new FileOutputStream("truststore.jks")) {
            keystore.store(fos, "changeit".toCharArray());
        }

        System.out.println("SSL certificate imported successfully");
    }
}
