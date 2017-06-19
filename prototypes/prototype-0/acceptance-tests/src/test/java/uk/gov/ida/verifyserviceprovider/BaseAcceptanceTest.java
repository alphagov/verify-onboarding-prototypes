package uk.gov.ida.verifyserviceprovider;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BaseAcceptanceTest {

    protected WebDriver webDriver = new HtmlUnitDriver(DesiredCapabilities.chrome()) {{
        setJavascriptEnabled(true);
    }};

}
