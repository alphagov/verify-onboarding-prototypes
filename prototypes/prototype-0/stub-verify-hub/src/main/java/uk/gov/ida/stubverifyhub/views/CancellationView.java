package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class CancellationView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-cancellation-saml-response";

    public CancellationView(String samlRequest, String relayState) {
        super("cancellation.ftl");
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
