package uk.gov.ida.verifyserviceprovider;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class VerifyServiceProviderAcceptanceTests {

    @Test
    public void shouldAuthenticateUserSuccessfullyWithoutJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{ setJavascriptEnabled(false); }};

        driver.get("http://localhost:3200/verify/start");

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send SAML Authn request to hub"));

        driver.findElement(By.cssSelector("form>button")).click();
        driver.findElement(By.id("continue-button")).click();

        assertThat(driver.getPageSource(), containsString("Hello Default User you are logged in"));
    }

    @Test
    public void shouldAuthenticateUserSuccessfullyWithJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{ setJavascriptEnabled(true); }};

        driver.get("http://localhost:3200/verify/start");
        driver.findElement(By.id("continue-button")).click();

        assertThat(driver.getPageSource(), containsString("Hello Default User you are logged in"));
    }

}
