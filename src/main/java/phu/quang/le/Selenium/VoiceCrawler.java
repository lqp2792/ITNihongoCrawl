package phu.quang.le.Selenium;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import phu.quang.le.Crawler.Words;

public class VoiceCrawler {

	public static void main (String[] args) {
		String url = "http://text-to-speech.imtranslator.net/";
		ExecutorService service = Executors.newFixedThreadPool (5);
		List<String> words = Words.getWords ("parsed-data");
		for (int i = 0, n = words.size (); i < n; i++) {
			String word = words.get (i);
			SimpleWorker worker = new SimpleWorker (url, word);
			service.execute (worker);
		}
	}
}
