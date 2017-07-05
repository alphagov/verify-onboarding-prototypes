package uk.gov.ida.verifyserviceprovider;

import io.dropwizard.configuration.ConfigurationValidationException;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;

public class ConfigurationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DropwizardTestSupport testSupport;

    @After
    public void cleanUp() {
//        if (testSupport != null) testSupport.after();
    }

    @Test
    public void applicationShouldFailToSttartWhenConfigurationIsEmpty() {
        DropwizardTestSupport testSupport = new DropwizardTestSupport(VerifyServiceProviderApplication.class,
                ConfigurationTest.class.getResource("/test-config-with-missing-values.yml").getPath()
        );

        expectedException.expectCause(instanceOf(ConfigurationValidationException.class));

        startApplication();
    }

    @Test
    public void shouldFailWhenHubSsoLocationIsMissing() {
        testSupport = new DropwizardTestSupport(VerifyServiceProviderApplication.class,
                ConfigurationTest.class.getResource("/test-config-with-missing-values.yml").getPath(),
                ConfigOverride.config("hubEntityId", "")
        );

        expectedException.expectCause(instanceOf(ConfigurationValidationException.class));
        expectedException.expectMessage("hubEntityId" + " may not be empty");

        startApplication();

//        withMissingConfigurationValue("hubSsoLocation");
//        startApplication();
    }

    @Test
    @Ignore
    public void shouldFailWhenHubEntityIdIsMissing() {
        withMissingConfigurationValue("hubEntityId");
        startApplication();
    }

//    @Test
//    public void shouldFailWhenMsaEntityIdIsMissing() {
//        withMissingConfigurationValue("msaEntityId");
//        startApplication();
//    }

    private void withMissingConfigurationValue(String configName) {
        if (testSupport != null) {
            testSupport.after();
        }

        testSupport = new DropwizardTestSupport(VerifyServiceProviderApplication.class,
                ConfigurationTest.class.getResource("/test-config-with-missing-values.yml").getPath(),
                ConfigOverride.config(configName, "")
        );

        expectedException.expectCause(instanceOf(ConfigurationValidationException.class));
        expectedException.expectMessage(configName + " may not be empty");
    }

    private void startApplication() {
        //System.clearProperty("dw." + "hubEntityId");
        testSupport.before();
    }
}
