package uk.gov.ida.stubverifyhub.resources;

import uk.gov.ida.stubverifyhub.views.SamlResponseForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/SAML2/SSO")
public class AuthnRequestResource {

    private Map<String, String> database;

    public AuthnRequestResource(Map<String, String> database) {
        this.database = database;

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response response( MultivaluedMap<String, String> form ) {
        String rpEntityId = form.getFirst("SAMLRequest");
        return Response.ok(new SamlResponseForm(database.get(rpEntityId), "a-saml-response", "a-relay-state")).build();
    }
}
