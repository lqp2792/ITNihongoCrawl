package phu.quang.le.Selenium;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SimpleWorker implements Runnable {

	public String url;
	public String word;

	public SimpleWorker (String url, String word) {
		this.url = url;
		this.word = word;
	}

	@Override
	public void run () {
		WebDriver driver = new FirefoxDriver ();
		WebElement element;
		driver.get (url);
		// Select language = japanese
		Select select = new Select (driver.findElement (By.id ("TTSLangs")));
		select.selectByValue ("ja");
		WebDriverWait wait = new WebDriverWait (driver, 60);
		driver.manage ().timeouts ().implicitlyWait (10, TimeUnit.SECONDS);
		// switch to TTSText iframe, send word to texare
		driver.switchTo ().frame (driver.findElement (By.id ("TTSText")));
		element = driver.findElement (By.id ("text"));
		element.sendKeys (word);
		// go back to default frame
		driver.switchTo ().defaultContent ();
		// click submit
		driver.findElement (By.id ("ttsgo")).click ();
		// switch to speech frame
		wait.until (ExpectedConditions.frameToBeAvailableAndSwitchToIt ("speech"));
		element = driver.findElement (By.xpath ("//img[@title='Play']"));
		StringBuffer link = new StringBuffer (element.getAttribute ("onclick"));
		// Attribute processing, get url params
		int endOffset = link.indexOf ("loadSWF") + "loadSWF".length ();
		link.delete (0, endOffset + 1);
		int startOffset = link.indexOf (")");
		link.delete (startOffset, link.length ());
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
		getSWF (url, word+".swf");
		driver.quit ();
	}

	/**
	 * Write swf file from input URL
	 * 
	 * @param url
	 *            file url
	 * @param filename
	 *            filename
	 */
	public void getSWF (String url, String filename) {
		try {
			URL connection = new URL (url);
			InputStream input = connection.openStream ();
			OutputStream output = new BufferedOutputStream (new FileOutputStream (
					"swf/" + filename));
			int b;
			while ((b = input.read ()) != -1) {
				output.write (b);
			}
			output.flush ();
			output.close ();
			input.close ();
		} catch (MalformedURLException e) {
			System.err.println (e);
		} catch (IOException e) {
			System.err.println (e);
		}
	}
}
