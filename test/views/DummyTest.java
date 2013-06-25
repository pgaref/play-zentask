package views;

import static play.test.Helpers.fakeGlobal;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import play.libs.Yaml;
import play.test.FakeApplication;
import play.test.Helpers;
import play.test.WithServer;
import util.EbeanTestUtil;

import com.avaje.ebean.Ebean;

public class DummyTest extends WithServer {

    public static FakeApplication app;

    @Test
    public void test() throws Exception {

        // Create a new instance of the html unit driver
        WebDriver driver = new HtmlUnitDriver();

        app = Helpers.fakeApplication(fakeGlobal());
        start(app, 9003);

        try {
            EbeanTestUtil.dropDB();
            EbeanTestUtil.createDB();
            Ebean.save((List<?>) Yaml.load("test-data.yml"));
        } catch (IOException e) {
            // ignore
        }

        // And now use this to visit Google
        driver.get("http://localhost:9003");

        WebElement element = driver.findElement(By.name("email"));
        element.sendKeys("bob@example.com");
        element = driver.findElement(By.name("password"));
        element.sendKeys("secret");
        element.submit();

        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.presenceOfElementLocated((By.className("dashboard"))));

    }
}
