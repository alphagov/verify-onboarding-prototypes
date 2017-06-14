package uk.gov.ida.stubverifyhub;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.gov.ida.stubverifyhub.resources.HelloWorldResource;

public class StubVerifyHubApplication extends Application<StubVerifyHubConfiguration> {

    public static void main(String[] args) throws Exception {
        new StubVerifyHubApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<StubVerifyHubConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
            )
        );
    }

    @Override
    public String getName() {
        return "verify-service-provider";
    }

    @Override
    public void run(StubVerifyHubConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new HelloWorldResource(configuration));
    }
}
