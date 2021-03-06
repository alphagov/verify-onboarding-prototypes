package uk.gov.ida.verifyserviceprovider;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static common.uk.gov.ida.verifyserviceprovider.pages.Pages.STUB_RP_RESPONSE_PAGE;
import static common.uk.gov.ida.verifyserviceprovider.pages.Pages.STUB_RP_START_PAGE;

public class AccountCreationAcceptanceTests {
    @Test
    public void shouldCreateUserWithoutJavaScript() {
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
        driver.findElement(By.cssSelector("div>input[value='ACCOUNT_CREATION']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'ACCOUNT_CREATION' Response"));

        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys(STUB_RP_RESPONSE_PAGE);
        driver.findElement(By.name("pid")).clear();
        driver.findElement(By.name("pid")).sendKeys(randomUUID().toString());
        driver.findElement(By.cssSelector("input[value='LEVEL_2']")).click();
        driver.findElement(By.name("firstName")).clear();
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("surname")).clear();
        driver.findElement(By.name("surname")).sendKeys("User");
        driver.findElement(By.cssSelector("input#continue-button")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();


        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Success!"));
        assertThat(driver.findElement(By.cssSelector("p")).getText(), is("You have successfully logged in as Test User at level of assurance LEVEL_2."));
    }

    @Test
    public void shouldCreateUserWithJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
            setJavascriptEnabled(true);
        }};

        driver.get(STUB_RP_START_PAGE);
        driver.findElement(By.cssSelector("#content>a.button.button-start")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Select the type of Response"));
        driver.findElement(By.cssSelector("div>input[value='ACCOUNT_CREATION']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'ACCOUNT_CREATION' Response"));

        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys(STUB_RP_RESPONSE_PAGE);
        driver.findElement(By.name("pid")).clear();
        driver.findElement(By.name("pid")).sendKeys(randomUUID().toString());
        driver.findElement(By.cssSelector("input[value='LEVEL_2']")).click();
        driver.findElement(By.name("firstName")).clear();
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("surname")).clear();
        driver.findElement(By.name("surname")).sendKeys("User");
        driver.findElement(By.cssSelector("input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Success!"));
        assertThat(driver.findElement(By.cssSelector("p")).getText(), is("You have successfully logged in as Test User at level of assurance LEVEL_2."));
    }

}
