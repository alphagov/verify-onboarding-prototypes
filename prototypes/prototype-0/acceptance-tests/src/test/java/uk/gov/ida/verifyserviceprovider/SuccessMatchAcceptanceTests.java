package uk.gov.ida.verifyserviceprovider;

import common.uk.gov.ida.verifyserviceprovider.pages.Pages;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class SuccessMatchAcceptanceTests {

    @Test
    public void shouldAuthenticateUserSuccessfullyWithoutJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
            setJavascriptEnabled(false);
        }};

        driver.get(Pages.STUB_RP_START_PAGE);
        driver.findElement(By.cssSelector("#content>a.button.button-start")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send SAML Authn request to hub"));
        driver.findElement(By.cssSelector("form>button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Select the type of Response"));
        driver.findElement(By.cssSelector("div>input[value='SUCCESS_MATCH']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'SUCCESS_MATCH' Response"));

        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys(Pages.STUB_RP_RESPONSE_PAGE);
        driver.findElement(By.name("pid")).clear();
        driver.findElement(By.name("pid")).sendKeys("billy");
        driver.findElement(By.cssSelector("input[value='LEVEL_2']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Success!"));
        assertThat(driver.findElement(By.cssSelector("p")).getText(), is("You have successfully logged in as Billy Batson at level of assurance LEVEL_2."));
    }

    @Test
    public void shouldAuthenticateUserSuccessfullyWithJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
            setJavascriptEnabled(true);
        }};

        driver.get(Pages.STUB_RP_START_PAGE);
        driver.findElement(By.cssSelector("#content>a.button.button-start")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Select the type of Response"));
        driver.findElement(By.cssSelector("div>input[value='SUCCESS_MATCH']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'SUCCESS_MATCH' Response"));

        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys(Pages.STUB_RP_RESPONSE_PAGE);
        driver.findElement(By.name("pid")).clear();
        driver.findElement(By.name("pid")).sendKeys("billy");
        driver.findElement(By.cssSelector("input[value='LEVEL_2']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Success!"));
        assertThat(driver.findElement(By.cssSelector("p")).getText(), is("You have successfully logged in as Billy Batson at level of assurance LEVEL_2."));
    }
}
