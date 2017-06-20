package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class GenerateSamlResponseForm extends View {

    private final String sendSamlResponseUrl = "/send-saml-response";
    private final String samlRequest;
    private final String relayState;

    public GenerateSamlResponseForm(String samlRequest, String relayState) {
        super("generateSamlResponseForm.ftl");

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
