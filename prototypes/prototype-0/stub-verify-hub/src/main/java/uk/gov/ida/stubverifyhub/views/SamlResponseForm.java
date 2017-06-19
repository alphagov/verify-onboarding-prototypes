package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class SamlResponseForm extends View {

    private String assertionConsumerService;
    private String samlResponse;
    private String relayState;

    public SamlResponseForm(String assertionConsumerService, String samlResponse, String relayState) {
        super("samlResponseForm.ftl");
        this.assertionConsumerService = assertionConsumerService;
        this.samlResponse = samlResponse;
        this.relayState = relayState;
    }


    public String getSamlResponse() {
        return samlResponse;
    }

    public String getAssertionConsumerService() {
        return assertionConsumerService;
    }

    public String getRelayState() {
        return relayState;
    }
}
