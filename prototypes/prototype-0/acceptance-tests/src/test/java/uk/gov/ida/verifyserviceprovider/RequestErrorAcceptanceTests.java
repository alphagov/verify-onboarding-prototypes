package uk.gov.ida.verifyserviceprovider;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static common.uk.gov.ida.verifyserviceprovider.pages.Pages.STUB_RP_RESPONSE_PAGE;
import static common.uk.gov.ida.verifyserviceprovider.pages.Pages.STUB_RP_START_PAGE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestErrorAcceptanceTests {

    @Test
    public void shouldFailAuthenticationWithRequestErrorWhenScenarioSelectedWithoutJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
            setJavascriptEnabled(false);
        }};

        driver.get(STUB_RP_START_PAGE);
        driver.findElement(By.cssSelector("#content>a.button.button-start")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send SAML Authn request to hub"));
        driver.findElement(By.cssSelector("form>button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Select the type of Response"));
        driver.findElement(By.cssSelector("div>input[value='REQUEST_ERROR']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'REQUEST_ERROR' Response"));

        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys(STUB_RP_RESPONSE_PAGE);
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send SAML Response to RP"));
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Authentication failed!"));
        assertThat(driver.findElement(By.cssSelector("p")).getText(), is("Because REQUEST_ERROR"));
    }

    @Test
    public void shouldFailAuthenticationWithRequestErrorWhenScenarioSelectedWithJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
            setJavascriptEnabled(true);
        }};

        driver.get(STUB_RP_START_PAGE);
        driver.findElement(By.cssSelector("#content>a.button.button-start")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Select the type of Response"));
        driver.findElement(By.cssSelector("div>input[value='REQUEST_ERROR']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'REQUEST_ERROR' Response"));

        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys(STUB_RP_RESPONSE_PAGE);
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Authentication failed!"));
        assertThat(driver.findElement(By.cssSelector("p")).getText(), is("Because REQUEST_ERROR"));
    }
}
