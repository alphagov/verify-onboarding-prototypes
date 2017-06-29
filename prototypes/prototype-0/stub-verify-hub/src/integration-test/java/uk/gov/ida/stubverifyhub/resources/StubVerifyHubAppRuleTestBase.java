package uk.gov.ida.stubverifyhub.resources;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONObject;
import org.junit.ClassRule;
import uk.gov.ida.stubverifyhub.StubVerifyHubApplication;
import uk.gov.ida.stubverifyhub.StubVerifyHubConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Base64;
import java.util.Map;

public class StubVerifyHubAppRuleTestBase {

    @ClassRule
    public static DropwizardAppRule<StubVerifyHubConfiguration> stubVerifyHub = new DropwizardAppRule<>(
        StubVerifyHubApplication.class, "stub-verify-hub-integration-test.yml");

    protected Client client = JerseyClientBuilder.createClient().property(ClientProperties.FOLLOW_REDIRECTS, false);

    protected URI getUriForPath(String path) {
        return UriBuilder.fromUri("http://localhost")
                .path(path)
                .port(stubVerifyHub.getLocalPort())
                .build();
    }

}
