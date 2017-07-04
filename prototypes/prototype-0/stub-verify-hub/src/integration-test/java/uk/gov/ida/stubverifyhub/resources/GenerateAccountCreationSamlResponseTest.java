package uk.gov.ida.stubverifyhub.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SEE_OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.decode;
import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.encode;
import static uk.gov.ida.stubverifyhub.utils.JsonUtils.objectMapper;

public class GenerateAccountCreationSamlResponseTest extends StubVerifyHubAppRuleTestBase {

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void generateSamlResponseFormTest() throws Exception {
        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
            put("scenario", "ACCOUNT_CREATION");
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
            put("scenario", "ACCOUNT_CREATION");
        }};

        Response response = client.target(getUriForPath("/generate-saml-response"))
            .request()
            .cookie(new NewCookie("generateSamlResponse", encode(objectMapper.writeValueAsString(formData))))
            .get();
        String html = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("Send a 'ACCOUNT_CREATION' Response");
        assertThat(html).contains("<form action=\"/send-account-creation-saml-response\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + samlRequest + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("name=\"assertionConsumerServiceUrl\"");
        assertThat(html).contains("name=\"pid\"");
        assertThat(html).contains("name=\"levelOfAssurance\"");
        assertThat(html).contains("name=\"levelOfAssurance\"");
        assertThat(html).contains("name=\"firstName\"");
        assertThat(html).contains("name=\"firstNameVerified\"");
        assertThat(html).contains("name=\"middleName\"");
        assertThat(html).contains("name=\"middleNameVerified\"");
        assertThat(html).contains("name=\"surname\"");
        assertThat(html).contains("name=\"surnameVerified\"");
        assertThat(html).contains("name=\"dateOfBirth\"");
        assertThat(html).contains("name=\"dateOfBirthVerified\"");
        assertThat(html).contains("name=\"addressVerified\"");
        assertThat(html).contains("name=\"addressLine1\"");
        assertThat(html).contains("name=\"addressLine2\"");
        assertThat(html).contains("name=\"addressLine3\"");
        assertThat(html).contains("name=\"postCode\"");
        assertThat(html).contains("name=\"internationalPostCode\"");
        assertThat(html).contains("name=\"uprn\"");
        assertThat(html).contains("name=\"fromDate\"");
        assertThat(html).contains("name=\"toDate\"");
        assertThat(html).contains("name=\"cycle3\"");
    }

    @Test
    public void sendSamlResponseFormWithAddressTest() throws IOException {
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";

        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
            put("assertionConsumerServiceUrl", assertionConsumerServiceUrl);
            put("pid", "some-pid-value");
            put("levelOfAssurance", "LEVEL_1");
            put("firstName", "some-first-name");
            put("firstNameVerified", "true");
            put("middleName", "some-middle-name");
            put("middleNameVerified", "true");
            put("surname", "some-surname");
            put("surnameVerified", "true");
            put("dateOfBirth", "2000/01/01");
            put("dateOfBirthVerified", "true");
            put("addressVerified", "true");
            put("addressLine1", "some-address-line-1");
            put("addressLine2", "some-address-line-2");
            put("addressLine3", "some-address-line-3");
            put("postCode", "some-post-code");
            put("internationalPostCode", "some-international-post-code");
            put("uprn", "some-uprn");
            put("fromDate", "2000/01/01");
            put("toDate", "207/01/01");
            put("cycle3", "some-cycle-3");
        }};

        Map<String, String> cookiesData = ImmutableMap.of(
            "relayState", relayState,
            "assertionConsumerServiceUrl", assertionConsumerServiceUrl,
            "samlResponseJson", createAccountCreationJsonObject(true).toString()
        );

        Response response = client.target(getUriForPath("/send-account-creation-saml-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(new MultivaluedHashMap<>(formData)));

        String samlContent = decode(response.getCookies().get("sendAccountCreationResponse").getValue());

        assertThat(response.getStatus()).isEqualTo(SEE_OTHER.getStatusCode());
        assertThat(new JSONObject(samlContent).toMap()).isEqualTo(cookiesData);
    }

    @Test
    public void sendSamlResponseFormWithoutAddressTest() throws IOException {
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";

        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
            put("assertionConsumerServiceUrl", assertionConsumerServiceUrl);
            put("pid", "some-pid-value");
            put("levelOfAssurance", "LEVEL_1");
            put("firstName", "some-first-name");
            put("firstNameVerified", "true");
            put("middleName", "some-middle-name");
            put("middleNameVerified", "true");
            put("surname", "some-surname");
            put("surnameVerified", "true");
            put("dateOfBirth", "2000/01/01");
            put("dateOfBirthVerified", "true");
            put("cycle3", "some-cycle-3");
        }};

        Map<String, String> cookiesData = ImmutableMap.of(
                "relayState", relayState,
                "assertionConsumerServiceUrl", assertionConsumerServiceUrl,
                "samlResponseJson", createAccountCreationJsonObject(false).toString()
        );

        Response response = client.target(getUriForPath("/send-account-creation-saml-response"))
                .request()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(Entity.form(new MultivaluedHashMap<>(formData)));

        String samlContent = decode(response.getCookies().get("sendAccountCreationResponse").getValue());

        assertThat(response.getStatus()).isEqualTo(SEE_OTHER.getStatusCode());
        assertThat(new JSONObject(samlContent).toMap()).isEqualTo(cookiesData);
    }

    @Test
    public void sendSamlResponseFormPageTest() throws JsonProcessingException {
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";
        JSONObject samlResponseJson = createAccountCreationJsonObject(true);

        Map<String, String> cookiesData = ImmutableMap.of(
            "relayState", relayState,
            "assertionConsumerServiceUrl", assertionConsumerServiceUrl,
            "samlResponseJson", samlResponseJson.toString()
        );

        Response response = client.target(getUriForPath("/send-account-creation-saml-response"))
            .request()
            .cookie(new NewCookie("sendAccountCreationResponse", encode(objectMapper.writeValueAsString(cookiesData))))
            .get();

        String html = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("<form action=\"" + assertionConsumerServiceUrl + "\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLResponse\" value=\"" + encode(samlResponseJson.toString()) + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("<input type=\"submit\" id=\"continue-button\" value=\"Continue\"/>");
    }

    private JSONObject createAccountCreationJsonObject(boolean withAddress) {
        JSONObject address = new JSONObject()
            .put("verified", true)
            .put("lines", new JSONArray().put("some-address-line-1").put("some-address-line-2").put("some-address-line-3"))
            .put("postCode", "some-post-code")
            .put("internationalPostCode", "some-international-post-code")
            .put("uprn", "some-uprn")
            .put("fromDate", "2000/01/01")
            .put("toDate", "207/01/01");

        JSONObject attributes = new JSONObject()
            .put("firstName", "some-first-name")
            .put("firstNameVerified", true)
            .put("middleName", "some-middle-name")
            .put("middleNameVerified", true)
            .put("surname", "some-surname")
            .put("surnameVerified", true)
            .put("dateOfBirth", "2000/01/01")
            .put("dateOfBirthVerified", true)
            .put("cycle3", "some-cycle-3");

        if (withAddress) {
            attributes.put("address", address);
        }

        return new JSONObject()
            .put("scenario", "ACCOUNT_CREATION")
            .put("pid", "some-pid-value")
            .put("levelOfAssurance", "LEVEL_1")
            .put("attributes", attributes);
    }
}