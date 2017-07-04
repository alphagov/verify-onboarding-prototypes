package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class NoMatchPageView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-no-match-saml-response";

    public NoMatchPageView(String samlRequest, String relayState) {
        super("noMatchPage.ftl");
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
