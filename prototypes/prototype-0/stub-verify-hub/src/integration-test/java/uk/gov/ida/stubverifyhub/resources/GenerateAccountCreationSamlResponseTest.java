package uk.gov.ida.stubverifyhub.resources;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class GenerateAccountCreationSamlResponseTest extends StubVerifyHubAppRuleTestBase {

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void generateSamlResponseFormTest() {
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>() {{
            add("SAMLRequest", samlRequest);
            add("relayState", relayState);
            add("responseType", "ACCOUNT_CREATION");
        }};

        Response response = client.target(getUriForPath("/generate-saml-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(formData));
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
    public void sendSamlResponseFormTest() {
        String assertionConsumerServiceUrl = "http://localhost/assertion-consumer-service-url";

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>() {{
            add("SAMLRequest", samlRequest);
            add("relayState", relayState);
            add("assertionConsumerServiceUrl", assertionConsumerServiceUrl);
            add("pid", "some-pid-value");
            add("levelOfAssurance", "LEVEL_1");
            add("firstName", "some-first-name");
            add("firstNameVerified", "true");
            add("middleName", "some-middle-name");
            add("middleNameVerified", "true");
            add("surname", "some-surname");
            add("surnameVerified", "true");
            add("dateOfBirth", "2000/01/01");
            add("dateOfBirthVerified", "true");
            add("addressVerified", "true");
            add("addressLine1", "some-address-line-1");
            add("addressLine2", "some-address-line-2");
            add("addressLine3", "some-address-line-3");
            add("postCode", "some-post-code");
            add("internationalPostCode", "some-international-post-code");
            add("uprn", "some-uprn");
            add("fromDate", "2000/01/01");
            add("toDate", "207/01/01");
            add("cycle3", "some-cycle-3");
        }};

        Response response = client.target(getUriForPath("/send-account-creation-saml-response"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(formData));

        String html = response.readEntity(String.class);

        JSONObject address = new JSONObject()
            .put("verified", "true")
            .put("lines", new JSONArray().put("some-address-line-1").put("some-address-line-2").put("some-address-line-3"))
            .put("postCode", "some-post-code")
            .put("internationalPostCode", "some-international-post-code")
            .put("uprn", "some-uprn")
            .put("fromDate", "2000/01/01")
            .put("toDate", "207/01/01");

        JSONObject attributes = new JSONObject()
            .put("firstName", "some-first-name")
            .put("firstNameVerified", "true")
            .put("middleName", "some-middle-name")
            .put("middleNameVerified", "true")
            .put("surname", "some-surname")
            .put("surnameVerified", "true")
            .put("dateOfBirth", "2000/01/01")
            .put("dateOfBirthVerified", "true")
            .put("address", address)
            .put("cycle3", "some-cycle-3");

        JSONObject samlResponseJson = new JSONObject()
            .put("responseType", "ACCOUNT_CREATION")
            .put("pid", "some-pid-value")
            .put("levelOfAssurance", "LEVEL_1")
            .put("attributes", attributes);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("<form action=\"" + assertionConsumerServiceUrl + "\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLResponse\" value=\"" + encodeBase64JsonObject(samlResponseJson) + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
        assertThat(html).contains("<input type=\"submit\" id=\"continue-button\" value=\"Continue\"/>");
    }
}