package uk.gov.ida.stubverifyhub.views;

import io.dropwizard.views.View;

public class LandingPageView extends View {

    private final String samlRequest;
    private final String relayState;

    private final String chooseResponseUrl = "/choose-response";

    public LandingPageView(String samlRequest, String relayState) {
        super("landingPage.ftl");
        this.samlRequest = samlRequest;
        this.relayState = relayState;
    }

    public String getChooseResponseUrl() {
        return chooseResponseUrl;
    }

    public String getSamlRequest() {
        return samlRequest;
    }

    public String getRelayState() {
        return relayState;
    }
}
