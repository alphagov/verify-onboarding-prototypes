package uk.gov.ida.stubverifyhub.resources.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigureRpDto {

    private String rpEntityId;

    private String assertionConsumerServiceUrl;

    @JsonCreator
    public ConfigureRpDto(@JsonProperty("rpEntityId") String rpEntityId,
                          @JsonProperty("assertionConsumerServiceUrl") String assertionConsumerServiceUrl) {
        this.rpEntityId = rpEntityId;
        this.assertionConsumerServiceUrl = assertionConsumerServiceUrl;
    }

    public String getRpEntityId() {
        return rpEntityId;
    }

    public String getAssertionConsumerServiceUrl() {
        return assertionConsumerServiceUrl;
    }

}
