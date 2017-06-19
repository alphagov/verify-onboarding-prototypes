package uk.gov.ida.stubverifyhub.resources;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import uk.gov.ida.stubverifyhub.StubVerifyHubApplication;
import uk.gov.ida.stubverifyhub.StubVerifyHubConfiguration;
import uk.gov.ida.stubverifyhub.resources.dto.ConfigureRpDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthRequestResourceTest {

    @ClassRule
    public static DropwizardAppRule<StubVerifyHubConfiguration> stubVerifyHub = new DropwizardAppRule<>(
            StubVerifyHubApplication.class, ResourceHelpers.resourceFilePath("stub-verify-hub.yml"));

    public Client client = JerseyClientBuilder.createClient().property(ClientProperties.FOLLOW_REDIRECTS, false);


    private static final String assertionConsumerService = "http://localhost:3200/assertion-consumer-service" ;
    private static final String rpEntityId = "rp-entity-id";

    @Before
    public void setup() {
        configureRp();
    }

    @Test
    public void sendAuthnRequest() throws Exception {

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("SAMLRequest", rpEntityId);
        formData.add("RelayState", "a-relay-state");
        Response response = client.target(getUriForPath("/SAML2/SSO"))
                .request()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(Entity.form(formData));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        String html = response.readEntity(String.class);
        assertThat(html).contains("a-saml-response");
        assertThat(html).contains("action=\"" + assertionConsumerService + "\"");
    }

    private void configureRp() {
        URI configureRp = getUriForPath("/configure-rp");
        Response response = client.target(configureRp)
                .request()
                .post(Entity.entity(new ConfigureRpDto(rpEntityId, assertionConsumerService), MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    private URI getUriForPath(String path) {
        return UriBuilder.fromUri("http://localhost")
                    .path(path)
                    .port(stubVerifyHub.getLocalPort())
                    .build();
    }

}