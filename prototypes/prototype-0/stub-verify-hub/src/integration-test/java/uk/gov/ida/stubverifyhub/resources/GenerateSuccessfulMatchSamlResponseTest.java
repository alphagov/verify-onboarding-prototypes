package uk.gov.ida.stubverifyhub.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SEE_OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.decode;
import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.encode;
import static uk.gov.ida.stubverifyhub.utils.JsonUtils.objectMapper;

public class GenerateSuccessfulMatchSamlResponseTest extends StubVerifyHubAppRuleTestBase {

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void generateSamlResponseTest() throws Exception {
        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
            put("scenario", "SUCCESS_MATCH");
        }};

        Response response = client.target(getUriForPath("/generate-saml-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(new MultivaluedHashMap<>(formData)));

        Map<String, String> formDataMap = objectMapper.readValue(
            decode(response.getCookies().get("generateSamlResponse").getValue()),
            new TypeReference<Map<String, String>>() {
            }
        );

        assertThat(response.getStatus()).isEqualTo(SEE_OTHER.getStatusCode());
        assertThat(formDataMap).isEqualTo(formData);
    }

    @Test
    public void generateSamlResponseFormPageTest() throws Exception {
        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
            put("scenario", "SUCCESS_MATCH");
        }};

        Response response = client.target(getUriForPath("/generate-saml-response"))
            .request()
            .cookie(new NewCookie("generateSamlResponse", encode(objectMapper.writeValueAsString(formData))))
            .get();
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
    public void sendSamlResponseTest() throws Exception {
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";

        Map<String, String> formData = new HashMap<String, String>() {{
            put("relayState", relayState);
            put("assertionConsumerServiceUrl", assertionConsumerServiceUrl);
            put("pid", "some-PID-value");
            put("levelOfAssurance", "LEVEL_1");
            put("scenario", "SUCCESS_MATCH");
        }};

        Response response = client.target(getUriForPath("/send-successful-match-saml-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(new MultivaluedHashMap<>(formData)));

        Map<String, String> formDataMap = objectMapper.readValue(
            decode(response.getCookies().get("sendSuccessfulMatchResponse").getValue()),
            new TypeReference<Map<String, String>>() {
            }
        );

        assertThat(response.getStatus()).isEqualTo(SEE_OTHER.getStatusCode());
        assertThat(formDataMap).isEqualTo(formData);
    }

    @Test
    public void sendSamlResponseFormPageTest() throws Exception {
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";

        Map<String, String> formData = new HashMap<String, String>() {{
            put("relayState", relayState);
            put("assertionConsumerServiceUrl", assertionConsumerServiceUrl);
            put("pid", "some-PID-value");
            put("levelOfAssurance", "LEVEL_1");
            put("scenario", "SUCCESS_MATCH");
        }};

        Response response = client.target(getUriForPath("/send-successful-match-saml-response"))
            .request()
            .cookie(new NewCookie("sendSuccessfulMatchResponse", encode(objectMapper.writeValueAsString(formData))))
            .get();

        String html = response.readEntity(String.class);

        Map<String, String> expectedSamlResponseData = ImmutableMap.of(
            "levelOfAssurance", "LEVEL_1",
            "pid", "some-PID-value",
            "scenario", "SUCCESS_MATCH"
        );

        String encodedSamlResponseJson = encode(objectMapper.writeValueAsString(expectedSamlResponseData));

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("<form action=\"" + assertionConsumerServiceUrl + "\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLResponse\" value=\"" + encodedSamlResponseJson + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("<input type=\"submit\" id=\"continue-button\" value=\"Continue\"/>");
    }
}