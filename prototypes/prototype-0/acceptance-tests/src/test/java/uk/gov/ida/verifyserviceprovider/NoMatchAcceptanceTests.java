package uk.gov.ida.verifyserviceprovider;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class NoMatchAcceptanceTests {

    @Test
    public void shouldFailAuthenticationWhenScenarioSelectedWithoutJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
            setJavascriptEnabled(false);
        }};

        driver.get("http://localhost:3200");
        driver.findElement(By.cssSelector("#content>a.button.button-start")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send SAML Authn request to hub"));
        driver.findElement(By.cssSelector("form>button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Select the type of Response"));
        driver.findElement(By.cssSelector("div>input[value='NO_MATCH']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

//        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'NO_MATCH' Response"));

//        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
//        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys("http://localhost:3200/verify/response");
//        driver.findElement(By.cssSelector("input#continue-button")).click();
//
//        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send SAML Response to RP"));
//        driver.findElement(By.cssSelector("input#continue-button")).click();
//
//
//        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("No match!"));
    }

    @Test
    public void shouldCreateUserWithJavaScript() {
        WebDriver driver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
            setJavascriptEnabled(true);
        }};

        driver.get("http://localhost:3200");
        driver.findElement(By.cssSelector("#content>a.button.button-start")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("You've sent a request."));
        driver.findElement(By.cssSelector("form>input#continue-button")).click();

        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Select the type of Response"));
        driver.findElement(By.cssSelector("div>input[value='NO_MATCH']")).click();
        driver.findElement(By.cssSelector("input#continue-button")).click();

//        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Send a 'NO_MATCH' Response"));

//        driver.findElement(By.name("assertionConsumerServiceUrl")).clear();
//        driver.findElement(By.name("assertionConsumerServiceUrl")).sendKeys("http://localhost:3200/verify/response");
//        driver.findElement(By.cssSelector("input#continue-button")).click();
//
//        assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("No match!"));
    }
}
