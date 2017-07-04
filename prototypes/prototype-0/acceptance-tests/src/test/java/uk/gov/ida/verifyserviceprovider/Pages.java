package uk.gov.ida.verifyserviceprovider;

import java.util.Optional;

public class Pages {

    public static final String STUB_RP_START_PAGE = Optional.ofNullable(System.getenv("STUB_RP_START_URL")).orElse("http://localhost:3200");
    public static final String STUB_RP_RESPONSE_PAGE = STUB_RP_START_PAGE + "/verify/response";
}
