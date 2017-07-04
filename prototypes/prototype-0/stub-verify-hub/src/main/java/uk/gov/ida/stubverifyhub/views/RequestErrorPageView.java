package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class RequestErrorPageView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-request-error-saml-response";

    public RequestErrorPageView(String samlRequest, String relayState) {
        super("requestErrorPage.ftl");
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
