package com.epam.lab.testng1;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class AppTest {
	WebDriver driver;
	List<String> textStrings; 
	@BeforeMethod
	public void beforeMethod() {		
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://github.com/");
		driver.manage().window().maximize();
	}
	@Test
	public void logining() {
		driver.findElement(By.linkText("Sign in")).click();
		driver.findElement(By.id("login_field")).sendKeys("hrafovyevhen@gmail.com");
		driver.findElement(By.id("password")).sendKeys("grafin63");
		driver.findElement(By.name("commit")).submit();
		Assert.assertEquals(driver.getCurrentUrl(), "https://github.com/", "Unsuccessful authorization");
	}
	@Test(dependsOnMethods = { "logining" })
	public void seaching() {
		driver.findElement(By.name("q")).sendKeys("selenium java", Keys.ENTER);
		print10Results();
	}
	@Test(dependsOnMethods = { "seaching" })
	public void verifySeaching() {
		 List<WebElement> elementList = driver.findElements(By.cssSelector("div[class='repo-list-item d-flex flex-justify-start py-4 public source']"));
	        Assert.assertTrue(elementList.stream().allMatch((x) ->
	          x.findElement(By.cssSelector("div[class = 'topics-row-container col-9 d-inline-flex flex-wrap flex-items-center f6 my-1']"))
	              .getText().contains("selenium")), "Not all results has tag selenium");
	}
	@Test(dependsOnMethods = { "verifySeaching" })
	public void sortingResults() {		
        System.out.println("'selenium java' sorted search result: \n");
        textStrings.stream().sorted().forEach(s -> System.out.println(s));
        System.out.println();
	}
	@Test(dependsOnMethods = { "sortingResults" })
	public void otherSearch() {		
        System.out.println("'java King' search result:");
        driver.findElement(By.name("q")).clear();
        driver.findElement(By.name("q")).sendKeys("java King", Keys.ENTER);
        print10Results();
	}
	private void print10Results() {		 
		List<WebElement> resultTitles = driver.findElements(By.cssSelector("h3 a"));
		textStrings = new ArrayList<>();
		System.out.println();
		resultTitles.forEach(x -> textStrings.add(x.getText()));
		resultTitles.forEach(x -> System.out.println(x.getText()));
		WebElement totalResultCount = driver
			.findElement(By.cssSelector("div[class = 'd-flex flex-justify-between border-bottom pb-3'] > h3"));
		System.out.println(String.format("%s \n", totalResultCount.getText()));
	}	
	@AfterMethod
	public void afterMethod() {
		driver.quit();
	}
}
