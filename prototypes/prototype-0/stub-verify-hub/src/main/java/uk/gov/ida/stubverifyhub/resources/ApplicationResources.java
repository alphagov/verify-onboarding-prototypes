package uk.gov.ida.stubverifyhub.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.views.View;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.ida.stubverifyhub.views.AccountCreationView;
import uk.gov.ida.stubverifyhub.views.AuthenticationFailedView;
import uk.gov.ida.stubverifyhub.views.CancellationView;
import uk.gov.ida.stubverifyhub.views.ChooseResponsePage;
import uk.gov.ida.stubverifyhub.views.LandingPageView;
import uk.gov.ida.stubverifyhub.views.NoMatchView;
import uk.gov.ida.stubverifyhub.views.SamlResponseForm;
import uk.gov.ida.stubverifyhub.views.SuccessMatchView;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.decode;
import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.encode;
import static uk.gov.ida.stubverifyhub.utils.ExceptionsUtils.uncheck;
import static uk.gov.ida.stubverifyhub.utils.JsonUtils.objectMapper;

@Path("/")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class ApplicationResources {

    private static final String SUCCESS_MATCH = "SUCCESS_MATCH";
    private static final String ACCOUNT_CREATION = "ACCOUNT_CREATION";
    private static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
    private static final String NO_MATCH = "NO_MATCH";
    private static final String CANCELLATION = "CANCELLATION";

    public ApplicationResources() {
    }

    @Path("/SAML2/SSO")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendAuthnRequest(MultivaluedMap<String, String> form) {
        Map<String, String> cookiesData = ImmutableMap.of(
            "SAMLRequest", form.getFirst("SAMLRequest"),
            "relayState", form.getFirst("relayState")
        );

        String sendAuthnRequestCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/SAML2/SSO"))
            .cookie(new NewCookie("sendAuthnRequest", sendAuthnRequestCookies))
            .build();
    }

    @Path("/SAML2/SSO")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response sendAuthnRequestPage(
        @CookieParam("sendAuthnRequest") String sendAuthnRequestCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(sendAuthnRequestCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        return Response.ok(new LandingPageView(
            formDataMap.get("SAMLRequest"),
            formDataMap.get("relayState")
        )).build();
    }

    @Path("/generate-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response generateSamlResponse(MultivaluedMap<String, String> form) {
        Map<String, String> cookiesData = ImmutableMap.of(
            "SAMLRequest", form.getFirst("SAMLRequest"),
            "relayState", form.getFirst("relayState"),
            "scenario", form.getFirst("scenario")
        );

        String generateSamlResponseCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/generate-saml-response"))
            .cookie(new NewCookie("generateSamlResponse", generateSamlResponseCookies))
            .build();
    }

    @Path("/generate-saml-response")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response generateSamlResponsePage(
        @CookieParam("generateSamlResponse") String encodedFormDataJsonCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(encodedFormDataJsonCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        String samlRequest = formDataMap.get("SAMLRequest");
        String relayState = formDataMap.get("relayState");

        View view;
        switch (formDataMap.get("scenario")) {
            case SUCCESS_MATCH:
                view = new SuccessMatchView(samlRequest, relayState);
                break;
            case ACCOUNT_CREATION:
                view = new AccountCreationView(samlRequest, relayState);
                break;
            case AUTHENTICATION_FAILED:
                view = new AuthenticationFailedView(samlRequest, relayState);
                break;
            case NO_MATCH:
                view = new NoMatchView(samlRequest, relayState);
                break;
            case CANCELLATION:
                view = new CancellationView(samlRequest, relayState);
                break;
            default:
                throw new RuntimeException("Unknown scenario");
        }
        return Response.ok(view).build();
    }

    @Path("/send-successful-match-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendSuccessfulMatchResponse(MultivaluedMap<String, String> form) {
        Map<String, String> cookiesData = ImmutableMap.of(
            "relayState", form.getFirst("relayState"),
            "assertionConsumerServiceUrl", form.getFirst("assertionConsumerServiceUrl"),
            "pid", form.getFirst("pid"),
            "levelOfAssurance", form.getFirst("levelOfAssurance"),
            "scenario", SUCCESS_MATCH
        );

        String sendSuccessfulMatchResponseCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/send-successful-match-saml-response"))
            .cookie(new NewCookie("sendSuccessfulMatchResponse", sendSuccessfulMatchResponseCookies))
            .build();
    }

    @Path("/send-successful-match-saml-response")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response sendSuccessfulMatchResponsePage(
        @CookieParam("sendSuccessfulMatchResponse") String sendSuccessfulMatchResponseCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(sendSuccessfulMatchResponseCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        Map<String, String> responseFormData = ImmutableMap.of(
            "levelOfAssurance", formDataMap.get("levelOfAssurance"),
            "pid", formDataMap.get("pid"),
            "scenario", formDataMap.get("scenario")
        );

        String samlResponseJson = uncheck(() -> objectMapper.writeValueAsString(responseFormData));

        return Response.ok(new SamlResponseForm(
            formDataMap.get("assertionConsumerServiceUrl"),
            encode(samlResponseJson),
            formDataMap.get("relayState")
        )).build();
    }

    @Path("/send-account-creation-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendAccountCreationResponse(MultivaluedMap<String, String> form) {

        JSONObject attributes = new JSONObject()
            .put("firstName", form.getFirst("firstName"))
            .put("firstNameVerified", isVerified(form, "firstNameVerified"))
            .put("middleName", form.getFirst("middleName"))
            .put("middleNameVerified", isVerified(form, "middleNameVerified"))
            .put("surname", form.getFirst("surname"))
            .put("surnameVerified", isVerified(form, "surnameVerified"))
            .put("dateOfBirth", form.getFirst("dateOfBirth"))
            .put("dateOfBirthVerified", isVerified(form, "dateOfBirthVerified"))
            .put("cycle3", form.getFirst("cycle3"));

        if (addressProvided(form)) {
            JSONArray addressLines = new JSONArray()
                    .put(form.getFirst("addressLine1"))
                    .put(form.getFirst("addressLine2"))
                    .put(form.getFirst("addressLine3"));
            JSONObject address = new JSONObject()
                    .put("verified", isVerified(form, "addressVerified"))
                    .put("lines", addressLines)
                    .put("postCode", form.getFirst("postCode"))
                    .put("internationalPostCode", form.getFirst("internationalPostCode"))
                    .put("uprn", form.getFirst("uprn"));
            attributes.put("address", address);
        }

        JSONObject samlResponseJson = new JSONObject()
            .put("scenario", ACCOUNT_CREATION)
            .put("pid", form.getFirst("pid"))
            .put("levelOfAssurance", form.getFirst("levelOfAssurance"))
            .put("attributes", attributes);

        Map<String, String> cookiesData = ImmutableMap.of(
            "relayState", form.getFirst("relayState"),
            "assertionConsumerServiceUrl", form.getFirst("assertionConsumerServiceUrl"),
            "samlResponseJson", samlResponseJson.toString()
        );

        String sendAccountCreationResponseCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/send-account-creation-saml-response"))
            .cookie(new NewCookie("sendAccountCreationResponse", sendAccountCreationResponseCookies))
            .build();
    }

    @Path("/send-account-creation-saml-response")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response sendAccountCreationResponse(
        @CookieParam("sendAccountCreationResponse") String sendAccountCreationResponseCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(sendAccountCreationResponseCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        return Response.ok(new SamlResponseForm(
            formDataMap.get("assertionConsumerServiceUrl"),
            encode(formDataMap.get("samlResponseJson")),
            formDataMap.get("relayState")
        ))
            .build();
    }

    @Path("/choose-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response chooseResponse(MultivaluedMap<String, String> form) {
        Map<String, String> cookiesData = ImmutableMap.of(
            "SAMLRequest", form.getFirst("SAMLRequest"),
            "relayState", form.getFirst("relayState")
        );

        String chooseResponseCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/choose-response"))
            .cookie(new NewCookie("chooseResponse", chooseResponseCookies))
            .build();
    }

    @Path("/choose-response")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response chooseResponsePage(
        @CookieParam("chooseResponse") String chooseResponseCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(chooseResponseCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        return Response.ok(new ChooseResponsePage(
            formDataMap.get("SAMLRequest"),
            formDataMap.get("relayState"))
        ).build();
    }

    @Path("/send-authentication-failed-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendAuthenticationFailedResponse(MultivaluedMap<String, String> form) {
        Map<String, String> cookiesData = ImmutableMap.of(
            "relayState", form.getFirst("relayState"),
            "assertionConsumerServiceUrl", form.getFirst("assertionConsumerServiceUrl"),
            "scenario", AUTHENTICATION_FAILED
        );

        String sendAuthenticationFailedResponseCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/send-authentication-failed-saml-response"))
            .cookie(new NewCookie("sendAuthenticationFailedResponse", sendAuthenticationFailedResponseCookies))
            .build();
    }

    @Path("/send-authentication-failed-saml-response")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response sendAuthenticationFailedResponsePage(
        @CookieParam("sendAuthenticationFailedResponse") String sendAuthenticationFailedResponseCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(sendAuthenticationFailedResponseCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        Map<String, String> responseFormData = ImmutableMap.of(
            "scenario", formDataMap.get("scenario")
        );

        String samlResponseJson = uncheck(() -> objectMapper.writeValueAsString(responseFormData));

        return Response.ok(new SamlResponseForm(
            formDataMap.get("assertionConsumerServiceUrl"),
            encode(samlResponseJson),
            formDataMap.get("relayState")
        )).build();
    }

    @Path("/send-no-match-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendNoMatchResponse(MultivaluedMap<String, String> form) {
        Map<String, String> cookiesData = ImmutableMap.of(
            "relayState", form.getFirst("relayState"),
            "assertionConsumerServiceUrl", form.getFirst("assertionConsumerServiceUrl"),
            "scenario", NO_MATCH
        );

        String sendNoMatchResponseCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/send-no-match-saml-response"))
            .cookie(new NewCookie("sendNoMatchResponse", sendNoMatchResponseCookies))
            .build();
    }

    @Path("/send-no-match-saml-response")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response sendNoMatchResponsePage(
        @CookieParam("sendNoMatchResponse") String sendNoMatchResponseCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(sendNoMatchResponseCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        Map<String, String> responseFormData = ImmutableMap.of(
            "scenario", formDataMap.get("scenario")
        );

        String samlResponseJson = uncheck(() -> objectMapper.writeValueAsString(responseFormData));

        return Response.ok(new SamlResponseForm(
            formDataMap.get("assertionConsumerServiceUrl"),
            encode(samlResponseJson),
            formDataMap.get("relayState")
        )).build();
    }

    @Path("/send-cancellation-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendCancellationResponse(MultivaluedMap<String, String> form) {
        Map<String, String> cookiesData = ImmutableMap.of(
            "relayState", form.getFirst("relayState"),
            "assertionConsumerServiceUrl", form.getFirst("assertionConsumerServiceUrl"),
            "scenario", CANCELLATION
        );

        String sendCancellationResponseCookies = encode(uncheck(() -> objectMapper.writeValueAsString(cookiesData)));

        return Response.seeOther(URI.create("/send-cancellation-saml-response"))
            .cookie(new NewCookie("sendCancellationResponse", sendCancellationResponseCookies))
            .build();
    }

    @Path("/send-cancellation-saml-response")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response sendCancellationResponsePage(
        @CookieParam("sendCancellationResponse") String sendCancellationResponseCookies
    ) {
        Map<String, String> formDataMap = uncheck(() -> objectMapper.readValue(
            decode(sendCancellationResponseCookies),
            new TypeReference<Map<String, String>>() {
            }
        ));

        Map<String, String> responseFormData = ImmutableMap.of(
            "scenario", formDataMap.get("scenario")
        );

        String samlResponseJson = uncheck(() -> objectMapper.writeValueAsString(responseFormData));

        return Response.ok(new SamlResponseForm(
            formDataMap.get("assertionConsumerServiceUrl"),
            encode(samlResponseJson),
            formDataMap.get("relayState")
        )).build();
    }

    private boolean isVerified(MultivaluedMap<String, String> form, String key) {
        return Objects.equals(form.getFirst(key), "true");
    }

    private boolean addressProvided(MultivaluedMap<String, String> form) {
        return Stream.of(
                form.getFirst("addressLine1"),
                form.getFirst("addressLine2"),
                form.getFirst("addressLine3"),
                form.getFirst("addressVerified"),
                form.getFirst("postCode"),
                form.getFirst("internationalPostCode"),
                form.getFirst("uprn")
        ).anyMatch(x -> !Strings.isNullOrEmpty(x));
    }
}