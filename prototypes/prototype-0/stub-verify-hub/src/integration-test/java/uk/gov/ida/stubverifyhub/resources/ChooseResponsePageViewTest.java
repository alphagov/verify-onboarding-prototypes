package uk.gov.ida.stubverifyhub.resources;

import com.fasterxml.jackson.core.type.TypeReference;
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
import static uk.gov.ida.stubverifyhub.utils.ExceptionsUtils.uncheck;
import static uk.gov.ida.stubverifyhub.utils.JsonUtils.objectMapper;

public class ChooseResponsePageViewTest extends StubVerifyHubAppRuleTestBase {

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void assertChooseResponse() throws Exception{
        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
        }};

        Response response = client.target(getUriForPath("/choose-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(new MultivaluedHashMap<>(formData)));

        Map<String, String> formDataMap = objectMapper.readValue(
            decode(response.getCookies().get("chooseResponse").getValue()),
            new TypeReference<Map<String, String>>() {
            }
        );

        assertThat(response.getStatus()).isEqualTo(SEE_OTHER.getStatusCode());
        assertThat(formDataMap).isEqualTo(formData);
    }

    @Test
    public void assertChooseResponsePage() throws Exception {
        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
        }};

        Response response = client.target(getUriForPath("/choose-response"))
            .request()
            .cookie(new NewCookie("chooseResponse", encode(objectMapper.writeValueAsString(formData))))
            .get();
        String html = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("Select the type of Response");
        assertThat(html).contains("<form action=\"/generate-saml-response\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + samlRequest + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("value=\"SUCCESS_MATCH\"");
        assertThat(html).contains("value=\"ACCOUNT_CREATION\"");
        assertThat(html).contains("value=\"AUTHENTICATION_FAILED\"");
        assertThat(html).contains("value=\"NO_MATCH\"");
        assertThat(html).contains("value=\"CANCELLATION\"");
        assertThat(html).contains("value=\"REQUEST_ERROR\"");
        assertThat(html).contains("value=\"INTERNAL_SERVER_ERROR\"");
    }
}
