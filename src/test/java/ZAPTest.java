import io.github.bonigarcia.wdm.WebDriverManager;
import net.bytebuddy.implementation.bytecode.Throw;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import static utils.Utils.*;

public class ZAPTest {
    static final String ZAP_PROXY_ADDRESS = getProperty("config","ZAP_PROXY_ADDRESS");
    static final String ZAP_PROXY_PORT = getProperty("config","ZAP_PROXY_PORT");
    static final String ZAP_API_KEY = getProperty("config", "ZAP_API_KEY");

    private WebDriver driver;
    private ClientApi api;

    @BeforeMethod
    public void setup(){
        String proxyServerUrl = ZAP_PROXY_ADDRESS + ":" + ZAP_PROXY_PORT;
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyServerUrl);
        proxy.setSslProxy(proxyServerUrl);

        ChromeOptions co = new ChromeOptions();
        co.setAcceptInsecureCerts(true);
        co.setProxy(proxy);
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(co);

        api = new ClientApi(ZAP_PROXY_ADDRESS, Integer.parseInt(ZAP_PROXY_PORT), ZAP_API_KEY);

    }

    @Test
    public void abstractaWebSecurityTest(){
        driver.get("http://opencart.abstracta.us/index.php?route=checkout/cart");
        Assert.assertTrue(driver.getTitle().contains("Shopping Cart"));
    }

    @AfterMethod
    public void tearDown() throws Exception {
        if (api != null) {
            String title = "POC ZAP Selenium - Abstracta";
            String template = "traditional-html";
            String description = "Este es un reporte de pruebas de ZAP";
            String reportfilename = "abstracta-web-security-report.html";
            String targetFolder = System.getProperty("user.dir");
            try {
                ApiResponse res = api.reports.generate(title, template, null, description, null, null, null,null, null,  reportfilename,null, targetFolder,null);
                System.out.println("Reporte de ZAP generado aqui: " + res.toString());
            } catch (ClientApiException ex) {
                throw new Exception(ex);
            }

        }
    }


}
