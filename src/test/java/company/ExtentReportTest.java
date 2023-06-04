package company;

import java.util.Base64;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentReportTest {

	ExtentReports report;
	
	@BeforeTest
	public void config() {
		
		//ExternReports - main class for execution
		//ExtentSparkReporter - used for make configuration of entire report
		
		String path = System.getProperty("user.dir") + "\\reports\\index.html"; // path to the folder // getPropery gives us the path to the project
		
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("Extent Report");
		reporter.config().setDocumentTitle("Extent Report Page");
		
		
		report = new ExtentReports(); 
		report.attachReporter(reporter);
		report.setSystemInfo("QA Engineer", "Vitali Poluboc");
		
	}
	
	@Test
	public void initialDemo() {
		
		ExtentTest watcher = report.createTest("Initial Demo"); //mandatory to start the report observe the test execution and 
										   //also creates an object that is responsible for watching the text execution
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver(options);
	
		
		driver.get("https://google.com");
		System.out.println(driver.getTitle());
		
		//makes screenshot
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);		
        String base64Screenshot = Base64.getEncoder().encodeToString(screenshot);
        watcher.addScreenCaptureFromBase64String(base64Screenshot);
		
		report.flush(); //mandatory to stop the report observe the test execution
		
	}
	
}
