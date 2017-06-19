package uk.gov.ida.stubverifyhub.resources;

import uk.gov.ida.stubverifyhub.resources.dto.ConfigureRpDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/configure-rp")
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigureRpResource {

    private Map<String, String> database;

    public ConfigureRpResource(Map<String, String> database) {
        this.database = database;
    }

    @POST
    public Response configureRp(ConfigureRpDto configureRpDto) {
        database.put(configureRpDto.getRpEntityId(), configureRpDto.getAssertionConsumerServiceUrl());
        return Response.ok().build();
    }
}
