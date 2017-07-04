package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class AccountCreationPageView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-account-creation-saml-response";

    public AccountCreationPageView(String samlRequest, String relayState) {
        super("accountCreationPage.ftl");
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
