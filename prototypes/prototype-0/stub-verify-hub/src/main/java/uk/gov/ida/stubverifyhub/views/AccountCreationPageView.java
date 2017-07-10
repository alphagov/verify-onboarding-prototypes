package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

import java.util.UUID;

public class AccountCreationPageView extends View {

    private final String samlRequest;
    private final String relayState;
    private final String sendSamlResponseUrl = "/send-account-creation-saml-response";
    private final String randomPID;

    public AccountCreationPageView(String samlRequest, String relayState) {
        super("accountCreationPage.ftl");
        this.samlRequest = samlRequest;
        this.relayState = relayState;
        this.randomPID = UUID.randomUUID().toString();
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

    public String getRandomPID() {
        return randomPID;
    }
}
