package uk.gov.ida.stubverifyhub;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import uk.gov.ida.stubverifyhub.resources.AuthnRequestResource;
import uk.gov.ida.stubverifyhub.resources.ConfigureRpResource;

import java.util.HashMap;
import java.util.Map;

public class StubVerifyHubApplication extends Application<StubVerifyHubConfiguration> {

    public static void main(String[] args) throws Exception {
        new StubVerifyHubApplication().run("server", "stub-verify-hub.yml");
    }

    @Override
    public void initialize(Bootstrap<StubVerifyHubConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(new ResourceConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
            )
        );
        bootstrap.addBundle(new ViewBundle<StubVerifyHubConfiguration>());
    }

    @Override
    public String getName() {
        return "verify-service-provider";
    }

    @Override
    public void run(StubVerifyHubConfiguration configuration, Environment environment) throws Exception {
        Map<String, String> database = new HashMap<>();

        environment.jersey().register(new AuthnRequestResource(database));
        environment.jersey().register(new ConfigureRpResource(database));
    }

}
