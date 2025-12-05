
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManagerFactory;

public class TrustedRootCertificates_GPT_3508050_3 {
    public static void main(String[] args) throws Exception {
        // Get the trust manager factory
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);

        // Get the default trust managers
        javax.net.ssl.TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        // Iterate over each trust manager
        for (javax.net.ssl.TrustManager trustManager : trustManagers) {
            if (trustManager instanceof javax.net.ssl.X509TrustManager) {
                // Get the trusted root certificates
                X509Certificate[] trustedRootCertificates = ((javax.net.ssl.X509TrustManager) trustManager).getAcceptedIssuers();

                // Print information about each trusted root certificate
                for (X509Certificate certificate : trustedRootCertificates) {
                    System.out.println("Subject DN: " + certificate.getSubjectDN());
                    System.out.println("Issuer DN: " + certificate.getIssuerDN());
                    System.out.println("Serial number: " + certificate.getSerialNumber());
                    System.out.println();
                }
            }
        }
    }
}
