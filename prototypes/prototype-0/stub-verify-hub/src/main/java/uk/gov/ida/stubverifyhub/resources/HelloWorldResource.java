package uk.gov.ida.stubverifyhub.resources;

import uk.gov.ida.stubverifyhub.StubVerifyHubConfiguration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello-world")
public class HelloWorldResource {

    StubVerifyHubConfiguration configuration;

    public HelloWorldResource(StubVerifyHubConfiguration configuration) {
        this.configuration = configuration;
    }

    @GET
    public String getHelloWorld() {
        return "Hello " + configuration.getHelloWorldValue();
    }
}
