
import javax.net.ssl.*;
import java.io.*;
import java.net.*;

public class UrlReadExample_GPT_1638275_2 {

    public static void main(String[] args) throws Exception {
        // Disable SSL certificate validation
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Create a SSL context with the custom trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        // Set the default SSL socket factory
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create an HTTP URL connection
        URL url = new URL("https://example.com");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        // Read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print the response
        System.out.println(response.toString());
    }
}
