package company.pagesObjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage {

	WebDriver driver;	
	
	public LandingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this); //we need this to use pagefactory @FindBy, this is like find a web element but in other way
	}	
	
	
	//Page Factory
	
	@FindBy(id="userEmail")
	WebElement userEmail;
		
	@FindBy(id="userPassword")
	WebElement userPassword;
	
	@FindBy(id="login")
	WebElement submit;
	
	
	public void Login(String email, String password) {
		
		userEmail.sendKeys(email);
		userPassword.sendKeys(password);
		submit.click();
		
	}
	
}
