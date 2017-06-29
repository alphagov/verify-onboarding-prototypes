package uk.gov.ida.stubverifyhub.resources;

import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class GenerateSuccessfulMatchSamlResponseTest extends StubVerifyHubAppRuleTestBase {

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void generateSamlResponseFormTest() {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>() {{
            add("SAMLRequest", samlRequest);
            add("relayState", relayState);
            add("responseType", "SUCCESS_MATCH");
        }};

        Response response = client.target(getUriForPath("/generate-saml-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(formData));
        String html = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("<h1 class=\"heading-large\">Send a 'SUCCESS_MATCH' Response</h1>");
        assertThat(html).contains("<form action=\"/send-successful-match-saml-response\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + samlRequest + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("name=\"assertionConsumerServiceUrl\"/>");
        assertThat(html).contains("name=\"pid\"/>");
        assertThat(html).contains("value=\"LEVEL_1\"");
        assertThat(html).contains("value=\"LEVEL_2\"");
    }

    @Test
    public void sendSamlResponseFormTest() {
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>() {{
            add("SAMLRequest", samlRequest);
            add("relayState", relayState);
            add("assertionConsumerServiceUrl", assertionConsumerServiceUrl);
            add("levelOfAssurance", "LEVEL_1");
            add("pid", "some-PID-value");
        }};

        Response response = client.target(getUriForPath("/send-successful-match-saml-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(formData));

        String html = response.readEntity(String.class);

        Map<String, String> expectedSamlResponseData = ImmutableMap.of(
            "responseType", "SUCCESS_MATCH",
            "levelOfAssurance", "LEVEL_1",
            "pid", "some-PID-value"
        );

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("<form action=\"" + assertionConsumerServiceUrl + "\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLResponse\" value=\"" + encodeBase64JsonObject(new JSONObject(expectedSamlResponseData)) + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("<input type=\"submit\" id=\"continue-button\" value=\"Continue\"/>");
    }
}