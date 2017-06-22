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

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a Response"));

        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys("http://localhost:3200/verify/response");

        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        String pageSource = driver.getPageSource();
        assertThat(pageSource, containsString("You have successfully logged in as"));
        assertThat(pageSource, containsString("Default User"));
        assertThat(pageSource, containsString("at level of assurance LEVEL_1"));
    }

    @Test
    public void shouldAuthenticateUserSuccessfullyWithJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{ setJavascriptEnabled(true); }};

        driver.get("http://localhost:3200/verify/start");
        driver.findElement(By.cssSelector("#continue-button")).click();

        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys("http://localhost:3200/verify/response");
        driver.findElement(By.cssSelector("#continue-button")).click();

        String pageSource = driver.getPageSource();
        assertThat(pageSource, containsString("You have successfully logged in as"));
        assertThat(pageSource, containsString("Default User"));
        assertThat(pageSource, containsString("at level of assurance LEVEL_1"));
    }

}
