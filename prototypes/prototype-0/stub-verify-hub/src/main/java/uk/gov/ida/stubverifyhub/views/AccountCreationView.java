package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class AccountCreationView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-account-creation-saml-response";

    public AccountCreationView(String samlRequest, String relayState) {
        super("accountCreationForm.ftl");
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
