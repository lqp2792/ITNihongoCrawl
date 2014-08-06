package phu.quang.le.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class GoogleSuggest {

	public static void main (String[] args) throws Exception {
		WebDriver driver = new HtmlUnitDriver ();
		driver.get ("http://text-to-speech.imtranslator.net/");
		driver.switchTo ().frame (driver.findElement (By.id ("TTSText")));
		WebElement element = driver.findElement (By.id ("text"));
		element.sendKeys ("情報");
		driver.switchTo ().defaultContent ();
		driver.findElement (By.id ("TTSLangs")).click ();
		driver.findElement (By.xpath ("//option[value='ja']")).click ();;
		driver.findElement (By.id ("ttsgo")).click ();
		System.out.println(driver.getCurrentUrl ());
	}
}
