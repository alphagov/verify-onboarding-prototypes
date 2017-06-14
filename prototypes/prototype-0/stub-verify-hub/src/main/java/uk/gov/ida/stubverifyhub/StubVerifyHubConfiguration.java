package uk.gov.ida.stubverifyhub;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class StubVerifyHubConfiguration extends Configuration {

    @JsonProperty
    @NotNull
    @Valid
    protected String helloWorldValue;

    public String getHelloWorldValue() {
        return helloWorldValue;
    }
}
