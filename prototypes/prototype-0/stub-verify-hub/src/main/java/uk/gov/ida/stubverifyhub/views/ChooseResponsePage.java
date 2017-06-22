package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class ChooseResponsePage extends View {
    private final String samlRequest;
    private final String relayState;
    private final String generateSamlResponseFormUrl = "/generate-saml-response";

    public ChooseResponsePage(String samlRequest, String relayState) {
        super("chooseResponse.ftl");
        this.samlRequest = samlRequest;
        this.relayState = relayState;
    }

    public String getGenerateSamlResponseFormUrl() {
        return generateSamlResponseFormUrl;
    }

    public String getSamlRequest() {
        return samlRequest;
    }

    public String getRelayState() {
        return relayState;
    }
}
