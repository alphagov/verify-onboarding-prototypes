package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class SamlResponsePageView extends View {

    private String assertionConsumerService;
    private String samlResponse;
    private String relayState;

    public SamlResponsePageView(String assertionConsumerService, String samlResponse, String relayState) {
        super("samlResponsePage.ftl");
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
