package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class InternalServerErrorPageView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-internal-server-error-saml-response";

    public InternalServerErrorPageView(String samlRequest, String relayState) {
        super("internalServerErrorPage.ftl");
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
