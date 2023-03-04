package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class JupiterToys {
	public static WebDriver driver;
	
	@Before
	public void setUp() throws IOException {
		setBrowserDriverPath();
		
		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);

		options.addArguments("--disable-notifications");
		options.setPageLoadStrategy(PageLoadStrategy.NONE);
		
		String url = propertiesReader("websiteUrl");
		getDriver().get(url);
		getDriver().manage().window().maximize();
	}

	@After
	public void tearDown() throws IOException {
		getDriver().close();
		getDriver().quit();
	}

	public static WebDriver getDriver() throws IOException{
		if (driver == null){
			driver = new ChromeDriver();
		}
		return driver;
	}
	
	public static void setBrowserDriverPath() throws IOException {
		String browserDriver = propertiesReader("browserDriver");
		String driverPath = propertiesReader("driverPath");

		System.setProperty(browserDriver, driverPath);
	}

	public static String propertiesReader(String key) throws IOException {
		Properties props = new Properties();
		FileReader reader = new FileReader("/planitAutomation-main/JupiterToys1/src/test/resources/SystemValues.properties");
		props.load(reader);
		return props.getProperty(key);
	}
}
