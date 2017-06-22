package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class SuccessMatchView extends View {

    private final String sendSamlResponseUrl = "/send-successful-match-saml-response";
    private final String samlRequest;
    private final String relayState;

    public SuccessMatchView(String samlRequest, String relayState) {
        super("successMatchForm.ftl");

        this.samlRequest = samlRequest;
        this.relayState = relayState;
    }

    public String getSendSamlResponseUrl() {
        return sendSamlResponseUrl;
    }

    public String getSamlRequest() {
        return samlRequest;
    }

    public String getRelayState() {
        return relayState;
    }
}
