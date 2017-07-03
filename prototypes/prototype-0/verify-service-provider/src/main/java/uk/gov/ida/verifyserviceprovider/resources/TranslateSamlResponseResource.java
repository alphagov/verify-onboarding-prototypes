package uk.gov.ida.verifyserviceprovider.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.ida.verifyserviceprovider.dto.ErrorBody;
import uk.gov.ida.verifyserviceprovider.dto.TranslateSamlResponseBody;
import uk.gov.ida.verifyserviceprovider.dto.TranslatedResponseBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Base64;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/translate-response")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TranslateSamlResponseResource {

    private static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";

    private static final Logger LOGGER = LoggerFactory.getLogger(TranslateSamlResponseResource.class);

    @POST
    public Response translateResponse(TranslateSamlResponseBody translateSamlResponseBody) {
        String decodedSamlResponse = new String(Base64.getDecoder().decode(translateSamlResponseBody.response));

        Response response;
        switch (new JSONObject(decodedSamlResponse).get("scenario").toString()) {
            case AUTHENTICATION_FAILED:
                response = Response.status(UNAUTHORIZED)
                    .entity(new ErrorBody(AUTHENTICATION_FAILED, "Authentication has failed."))
                    .build();
                break;
            default:
                response = createDefaultResponse(decodedSamlResponse);
                break;
        }

        return response;
    }

    private Response createDefaultResponse(String decodedSamlResponse) {
        try {
            return Response.ok(convertTranslatedResponseBody(decodedSamlResponse)).build();
        } catch (IOException e) {
            LOGGER.error("Error during SAML response translation.", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private TranslatedResponseBody convertTranslatedResponseBody(String decodedSamlResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper() {{
            registerModule(new Jdk8Module());
            registerModule(new JavaTimeModule());
        }};

        return objectMapper.readValue(decodedSamlResponse, new TypeReference<TranslatedResponseBody>() {
        });
    }
}
