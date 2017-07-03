package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class NoMatchView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-no-match-saml-response";

    public NoMatchView(String samlRequest, String relayState) {
        super("noMatchForm.ftl");
        this.samlRequest = samlRequest;
        this.relayState = relayState;
    }

    public String getSamlRequest() {
        return samlRequest;
    }

    public String getRelayState() {
        return relayState;
    }

    public String getSendSamlResponseUrl() {
        return sendSamlResponseUrl;
    }
}
