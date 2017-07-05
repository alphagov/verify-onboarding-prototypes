package uk.gov.ida.verifyserviceprovider;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class VerifyServiceProviderConfiguration extends Configuration {

    private static final String NOT_EMPTY_PATTERN = "(!\\s)+";
    private static final String NOT_EMPTY_MESSAGE = "may not be empty";

    @JsonProperty
    @NotNull
    @Valid
    @Pattern(regexp = NOT_EMPTY_PATTERN, message = NOT_EMPTY_MESSAGE)
    private String hubSsoLocation;

//    @JsonProperty
//    @NotNull
//    @Valid
//    @Pattern(regexp = NOT_EMPTY_PATTERN, message = NOT_EMPTY_MESSAGE)
//    private String hubEntityId;

    public String getHubSsoLocation() {
        return hubSsoLocation;
    }

//    public String getHubEntityId() {
//        return hubEntityId;
//    }
}
