package phu.quang.le.Selenium;

import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestWorker {
	public static void main (String[] args) throws Exception {
		WebDriver driver = new FirefoxDriver ();
		WebElement element;
		driver.get ("http://text-to-speech.imtranslator.net/");
		Select select = new Select (driver.findElement (By.id ("TTSLangs")));
		select.selectByValue ("ja");
		// element = driver.findElement (By.xpath ("//option[@value='ja']"));
		WebDriverWait wait = new WebDriverWait (driver, 60);
		driver.manage ().timeouts ().implicitlyWait (10, TimeUnit.SECONDS);
		driver.switchTo ().frame (driver.findElement (By.id ("TTSText")));
		element = driver.findElement (By.id ("text"));
		element.sendKeys ("情報");
		driver.switchTo ().defaultContent ();
		driver.findElement (By.id ("ttsgo")).click ();
	
		wait.until (ExpectedConditions.frameToBeAvailableAndSwitchToIt ("speech"));
		element = driver.findElement (By.xpath ("//img[@title='Play']"));
		StringBuffer link = new StringBuffer (element.getAttribute ("onclick"));
		int endOffset = link.indexOf ("loadSWF") + "loadSWF".length ();
		link.delete (0, endOffset + 1);
		int startOffset = link.indexOf (")");
		link.delete (startOffset, link.length ());
		System.out.println (link.toString ());
		String url = "http://cs1.imtranslator.net/SL/Free_Projects/*/";
		StringTokenizer tokens = new StringTokenizer (link.toString (), "page,'");
		int i = 0;
		while (tokens.hasMoreTokens ()) {
			String param = tokens.nextToken ();
			if (i == 0 || i == 3) {
				url = url + param;
			}
			if (i == 1) {
				url = url.replaceAll ("\\*", param);
			}
			if (i == 2) {
				url = url + param + ".swf?param1=";
			}
			i++;
		}
		System.out.println (url);
		driver.quit ();
	}
}
