package uk.gov.ida.verifyserviceprovider;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class VerifyServiceProviderAcceptanceTests extends BaseAcceptanceTest {

    @Test
    @Ignore("Stub hub isn't built yet")
    public void shouldAuthenticateUserSuccessfully() {
        webDriver.get("http://localhost:3200/verify/start");

        assertThat(webDriver.findElement(By.cssSelector("h1")).getText(), is("Send SAML Authn request to hub"));

        webDriver.findElement(By.cssSelector("form>button")).click();

        assertThat(webDriver.getTitle(), is("Stub Verify Hub - Choose a Response"));
    }

}
