package uk.gov.ida.stubverifyhub.resources;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;
import uk.gov.ida.stubverifyhub.StubVerifyHubApplication;
import uk.gov.ida.stubverifyhub.StubVerifyHubConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthRequestResourceTest {

    @ClassRule
    public static DropwizardAppRule<StubVerifyHubConfiguration> stubVerifyHub = new DropwizardAppRule<>(
            StubVerifyHubApplication.class, "stub-verify-hub-integration-test.yml");

    public Client client = JerseyClientBuilder.createClient().property(ClientProperties.FOLLOW_REDIRECTS, false);

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void sendAuthnRequest_shouldReturnLandingPage() {

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("SAMLRequest", samlRequest);
        formData.add("RelayState", relayState);
        Response response = client.target(getUriForPath("/SAML2/SSO"))
                .request()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(Entity.form(formData));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        String html = response.readEntity(String.class);
        assertThat(html).contains("You've sent a request.");
        assertThat(html).contains("<form action=\"/generate-saml-response\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + samlRequest + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"RelayState\" value=\"" + relayState + "\"/>");
    }

    @Test
    public void generateSamlResponseFormTest() {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("SAMLRequest", samlRequest);
        formData.add("RelayState", relayState);
        Response response = client.target(getUriForPath("/generate-saml-response"))
                .request()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(Entity.form(formData));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        String html = response.readEntity(String.class);
        assertThat(html).contains("Send a Response");
        assertThat(html).contains("<form action=\"/send-saml-response\" method=\"POST\">");
        assertThat(html).contains("<input type=\"text\" value=\"\" name=\"assertionConsumerServiceUrl\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + samlRequest + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"RelayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("<input type=\"radio\" name=\"LevelOfAssurance\" value=\"LEVEL_1\" checked> Level Of Assurance 1 <br>");
        assertThat(html).contains("<input type=\"radio\" name=\"LevelOfAssurance\" value=\"LEVEL_2\"> Level Of Assurance 2 <br>");
    }

    @Test
    public void sendSamlResponseFormTest() {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("SAMLRequest", samlRequest);
        formData.add("RelayState", relayState);
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";
        formData.add("assertionConsumerServiceUrl", assertionConsumerServiceUrl);
        formData.add("LevelOfAssurance", "LEVEL_1");
        Response response = client.target(getUriForPath("/send-saml-response"))
                .request()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(Entity.form(formData));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        String html = response.readEntity(String.class);
        assertThat(html).contains("<form action=\"" + assertionConsumerServiceUrl + "\" method=\"POST\">");

        String encodedSamlResponse = new String(Base64.getEncoder().encode("{\"levelOfAssurance\":\"LEVEL_1\"}".getBytes()));

        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLResponse\" value=\"" + encodedSamlResponse + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"RelayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("<input type=\"submit\" id=\"continue-button\" value=\"Send Saml Response to RP\"/>");

    }

    private URI getUriForPath(String path) {
        return UriBuilder.fromUri("http://localhost")
                .path(path)
                .port(stubVerifyHub.getLocalPort())
                .build();
    }

}