package uk.gov.ida.stubverifyhub.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import uk.gov.ida.stubverifyhub.StubVerifyHubConfiguration;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldResourceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HelloWorldResource(getConfiguration()))
            .build();

    private static StubVerifyHubConfiguration getConfiguration() {
        class TestStubVerifyHubConfiguration extends StubVerifyHubConfiguration {
            public TestStubVerifyHubConfiguration() {
                this.helloWorldValue = "World";
            }
        };

        return new TestStubVerifyHubConfiguration();
    }

    @Test
    public void shouldSayHello() throws Exception {
        Response response = resources.target("/hello-world")
                .request()
                .get();

        assertThat(response.readEntity(String.class)).isEqualTo("Hello World");
    }
  }
