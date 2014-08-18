package phu.quang.le.Selenium;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

public class SimpleUnit {

	public static void main (String[] args) {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getCookieManager().setCookiesEnabled(true);
		try {
			HtmlPage mainPage = webClient
					.getPage ("http://text-to-speech.imtranslator.net/");
			List<FrameWindow> frames = mainPage.getFrames ();
			HtmlSelect select = (HtmlSelect) mainPage.getElementById ("TTSLangs");
			HtmlOption option = select.getOption (6);
			option.setSelected (true);
			for (FrameWindow frame : frames) {
				if (frame.getName ().equals ("TTSText")) {
					HtmlPage subPage = (HtmlPage) frame.getEnclosedPage ();
					HtmlTextArea input = (HtmlTextArea) subPage
							.getElementById ("text");
					input.setTextContent ("情報");
				}
			}
			HtmlButtonInput button = (HtmlButtonInput) mainPage
					.getElementById ("ttsgo");
			button.click ();
			webClient.waitForBackgroundJavaScript(5000);
			frames = mainPage.getFrames ();
			for (FrameWindow frame : frames) {
				System.out.println (frame.getName ());
				if (frame.getName ().equals ("speech")) {
					HtmlPage subPage = (HtmlPage) frame.getEnclosedPage ();
					System.out.println (subPage.asXml ());
					System.out.println (subPage.getByXPath ("//img[@title='Play']")
							.size ());
				}
			}
		} catch (FailingHttpStatusCodeException e) {
			System.err.println (e);
		} catch (MalformedURLException e) {
			System.err.println (e);
		} catch (IOException e) {
			System.err.println (e);
		}
	}
}
