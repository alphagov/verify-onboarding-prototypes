package uk.gov.ida.stubverifyhub.resources;

import com.google.common.collect.ImmutableMap;
import io.dropwizard.views.View;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.ida.stubverifyhub.views.AccountCreationView;
import uk.gov.ida.stubverifyhub.views.ChooseResponsePage;
import uk.gov.ida.stubverifyhub.views.SuccessMatchView;
import uk.gov.ida.stubverifyhub.views.LandingPageView;
import uk.gov.ida.stubverifyhub.views.SamlResponseForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.Map;

@Path("/")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class ApplicationResources {

    private static final String SUCCESS_MATCH = "SUCCESS_MATCH";
    private static final String ACCOUNT_CREATION = "ACCOUNT_CREATION";

    public ApplicationResources() {
    }

    @Path("/SAML2/SSO")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response sendAuthnRequest(MultivaluedMap<String, String> form) {
        return Response.ok(new LandingPageView(
            form.getFirst("SAMLRequest"),
            form.getFirst("relayState")
        )).build();
    }

    @Path("/generate-saml-response")
    @POST
    public Response generateSamlResponse(MultivaluedMap<String, String> form) {
        String responseType = form.getFirst("responseType");
        View view = null;
        switch (responseType) {
            case SUCCESS_MATCH:
                view = new SuccessMatchView(form.getFirst("SAMLRequest"), form.getFirst("relayState"));
                break;
            case ACCOUNT_CREATION:
                view = new AccountCreationView(form.getFirst("SAMLRequest"), form.getFirst("relayState"));
        }
        return Response.ok(view).build();
    }

    @Path("/send-successful-match-saml-response")
    @POST
    public Response sendSuccessfulMatchResponse(MultivaluedMap<String, String> form) {
        Map<String, String> responseFormData = ImmutableMap.of(
            "levelOfAssurance", form.getFirst("levelOfAssurance"),
            "pid", form.getFirst("pid"),
            "responseType", SUCCESS_MATCH
        );

        String samlResponseJson = new JSONObject(responseFormData).toString();

        String base64EncodedSamlResponse = new String(Base64.getEncoder().encode(samlResponseJson.getBytes()));
        return Response.ok(new SamlResponseForm(
            form.getFirst("assertionConsumerServiceUrl"),
            base64EncodedSamlResponse,
            form.getFirst("relayState")
        )).build();
    }

    @Path("/send-account-creation-saml-response")
    @POST
    public Response sendAccountCreationResponse(MultivaluedMap<String, String> form) {
        JSONArray addressLines = new JSONArray()
            .put(form.getFirst("addressLine1"))
            .put(form.getFirst("addressLine2"))
            .put(form.getFirst("addressLine3"));
        JSONObject address = new JSONObject()
            .put("verified", form.getFirst("addressVerified"))
            .put("lines", addressLines)
            .put("postCode", form.getFirst("postCode"))
            .put("internationalPostCode", form.getFirst("internationalPostCode"))
            .put("uprn", form.getFirst("uprn"))
            .put("fromDate", form.getFirst("fromDate"))
            .put("toDate", form.getFirst("toDate"));

        JSONObject attributes = new JSONObject()
            .put("firstName", form.getFirst("firstName"))
            .put("firstNameVerified", form.getFirst("firstNameVerified"))
            .put("middleName", form.getFirst("middleName"))
            .put("middleNameVerified", form.getFirst("middleNameVerified"))
            .put("surname", form.getFirst("surname"))
            .put("surnameVerified", form.getFirst("surnameVerified"))
            .put("dateOfBirth", form.getFirst("dateOfBirth"))
            .put("dateOfBirthVerified", form.getFirst("dateOfBirthVerified"))
            .put("address", address)
            .put("cycle3", form.getFirst("cycle3"));

        JSONObject samlResponseJson = new JSONObject()
            .put("responseType", ACCOUNT_CREATION)
            .put("pid", form.getFirst("pid"))
            .put("levelOfAssurance", form.getFirst("levelOfAssurance"))
            .put("attributes", attributes);

        String base64EncodedSamlResponse = new String(Base64.getEncoder().encode(samlResponseJson.toString().getBytes()));
        return Response.ok(new SamlResponseForm(
            form.getFirst("assertionConsumerServiceUrl"),
            base64EncodedSamlResponse,
            form.getFirst("relayState")
        )).build();
    }

    @Path("/choose-response")
    @POST
    public Response chooseResponsePage(MultivaluedMap<String, String> form) {
        return Response.ok(new ChooseResponsePage(
            form.getFirst("SAMLRequest"),
            form.getFirst("relayState")
        )).build();
    }
}
