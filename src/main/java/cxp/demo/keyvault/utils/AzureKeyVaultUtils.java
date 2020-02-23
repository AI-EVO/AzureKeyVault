package cxp.demo.keyvault.utils;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AzureKeyVaultUtils {

    private static String vaultBase = System.getenv("VAULT_BASE");
    private static String ClientId = System.getenv("AZURE_CLIENT_ID");
    private static String clientKey = System.getenv("AZURE_CLIENT_SECRET");

    public static AuthenticationResult getAccessToken(String authorization, String resource) throws InterruptedException, ExecutionException, MalformedURLException {

        AuthenticationResult result = null;

        //Starts a service to fetch access token.
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(1);
            AuthenticationContext context = new AuthenticationContext(authorization, false, service);

            Future<AuthenticationResult> future = null;

            //Acquires token based on client ID and client secret.
            if (ClientId != null && clientKey != null) {
                ClientCredential credentials = new ClientCredential(ClientId, clientKey);
                future = context.acquireToken(resource, credentials, null);
            }

            result = future.get();
        } finally {
            service.shutdown();
        }

        if (result == null) {
            throw new RuntimeException("Authentication results were null.");
        }
        return result;
    }

    private static KeyVaultClient GetKeyVaultClient() {
        return new KeyVaultClient(new KeyVaultCredentials() {
            @Override
            public String doAuthenticate(String authorization, String resource, String scope) {
                String token = null;
                try {
                    AuthenticationResult authResult = getAccessToken(authorization, resource);
                    token = authResult.getAccessToken();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return token;
            }
        });
    }

    public static SecretBundle GetSecret(String secretName){
        KeyVaultClient keyVaultClient = GetKeyVaultClient();
        return keyVaultClient.getSecret(vaultBase, secretName);
    }

    public static KeyBundle GetKey(String keyName){
        KeyVaultClient keyVaultClient = GetKeyVaultClient();
        return keyVaultClient.getKey(vaultBase, keyName);
    }

    public static CertificateBundle GetCertificate(String certificateName){
        KeyVaultClient keyVaultClient = GetKeyVaultClient();
        return keyVaultClient.getCertificate(vaultBase, certificateName);
    }

}
