package uk.gov.ida.stubverifyhub.resources;

import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class LandingPageTest extends StubVerifyHubAppRuleTestBase {

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void sendAuthnRequest_shouldReturnLandingPage() {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>() {{
            add("SAMLRequest", samlRequest);
            add("relayState", relayState);
        }};

        Response response = client.target(getUriForPath("/SAML2/SSO"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(formData));
        String html = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("You've sent a request.");
        assertThat(html).contains("<form action=\"/choose-response\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + samlRequest + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
    }

}
