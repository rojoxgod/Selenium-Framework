package company;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import company.pagesObjects.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class StandAloneTest {

	public static WebDriver driver;
	public static ChromeOptions options;
	public static WebDriverWait wait;

	public static void main(String[] args) {

		InitializeDriver("edge");

		Login(driver, "Test12345!@mail.com", "Test123!");

		// LoginPageObject(driver);

		AddToCart(driver);

		ConfirmAndCheckout(driver);

		Payment(driver);

		Exit();

	}

	public static void InitializeDriver(String browser) {

		switch (browser) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			break;
		case "edge":
			WebDriverManager.edgedriver().setup();
			break;
		default:
			System.out.println("Invalid browser specified.");
			return;
		}

		switch (browser) {
		case "chrome":
			driver = new ChromeDriver();
			break;
		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "edge":
			driver = new EdgeDriver();
			break;
		default:
			System.out.println("Invalid browser specified.");
			return;
		}

		
		//options = new ChromeOptions();
		//options.addArguments("--remote-allow-origins=*");
		//used for allow different options in chrome


		// 1 - System.setProperty("webdriver.chrome.driver", "E:\\work\\qa
		// engineering\\chromedriver_win32\\chromedriver.exe");
		// 2 - WebDriverManager.chromedriver().setup();
		// two ways of declaring the webdriver

		//driver = new ChromeDriver(options);
		//creating driver object

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // implicit wait for all project
		driver.manage().window().maximize();

		driver.get("https://rahulshettyacademy.com/client");

	}

	public static void Login(WebDriver driver, String email, String password) {

		driver.findElement(By.id("userEmail")).sendKeys(email);
		driver.findElement(By.id("userPassword")).sendKeys(password);
		driver.findElement(By.id("login")).click();

	}

	public static void LoginPageObject(WebDriver driver) {

		// Page Object, not super useful in my opinion
		LandingPage landingPage = new LandingPage(driver);
		landingPage.Login("Test12345!@mail.com", "Test123!");

	}

	public static void AddToCart(WebDriver driver) {

		List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
		WebElement product = products.stream()
				.filter(s -> s.findElement(By.cssSelector("b")).getText().equalsIgnoreCase("ADIDAS ORIGINAL"))
				.findFirst().orElse(null);
		product.findElement(By.cssSelector(".card-body button:last-of-type")).click();

	}

	public static void ConfirmAndCheckout(WebDriver driver) {

		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		String expected = "Product Added To Cart";
		wait.until(ExpectedConditions.textToBe(By.id("toast-container"), expected));
		System.out.println("Product added to cart successfully");

		driver.findElement(By.xpath("//button[@routerlink='/dashboard/cart']")).click();

		List<WebElement> addedProducts = driver.findElements(By.xpath("//div[@class='cartSection']/h3"));
		Boolean isProductPresent = addedProducts.stream()
				.anyMatch(s -> s.getText().equalsIgnoreCase("ADIDAS ORIGINAL"));
		Assert.assertTrue(isProductPresent);

		driver.findElement(By.xpath("//li[@class='totalRow']/button")).click();

	}

	public static void Payment(WebDriver driver) {

		Actions act = new Actions(driver);

		act.moveToElement(driver.findElement(By.xpath("//input[@placeholder='Select Country']"))).click()
				.sendKeys("Italy").build().perform();
		act.moveToElement(driver.findElement(By.xpath("//button[@class='ta-item list-group-item ng-star-inserted']")))
				.click().build().perform();
		act.moveToElement(driver.findElement(By.xpath("//div[@class='actions']/a"))).click().build().perform();

		String actual = driver.findElement(By.className("hero-primary")).getText().trim().toLowerCase();
		String expected = "thankyou for the order.";
		Assert.assertEquals(actual, expected);

	}

	public static void Exit() {

		driver.close();

	}

}
