package uk.gov.ida.stubverifyhub.resources;

import org.json.JSONObject;
import uk.gov.ida.stubverifyhub.views.GenerateSamlResponseForm;
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

@Path("/")
public class AuthnRequestResource {

    public AuthnRequestResource() {}

    @Path("/SAML2/SSO")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response sendAuthnRequest(MultivaluedMap<String, String> form) {
        return Response.ok(new LandingPageView(form.getFirst("SAMLRequest"), form.getFirst("RelayState"))).build();
    }

    @Path("/generate-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response generateSamlResponse(MultivaluedMap<String, String> form) {
        return Response.ok(new GenerateSamlResponseForm(form.getFirst("SAMLRequest"), form.getFirst("RelayState"))).build();
    }

    @Path("/send-saml-response")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response sendSamlResponse(MultivaluedMap<String, String> form) {
        String levelOfAssurance = form.getFirst("LevelOfAssurance");
        String samlRequestJson = new JSONObject().put("levelOfAssurance", levelOfAssurance).toString();
        String base64EncodedSamlRequest = new String(Base64.getEncoder().encode(samlRequestJson.getBytes()));
        return Response.ok(new SamlResponseForm(form.getFirst("assertionConsumerServiceUrl"), base64EncodedSamlRequest, form.getFirst("RelayState"))).build();
    }
}
