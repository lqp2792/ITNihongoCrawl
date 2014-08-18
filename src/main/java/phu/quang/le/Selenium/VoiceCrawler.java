package phu.quang.le.Selenium;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import phu.quang.le.Crawler.Words;

public class VoiceCrawler {

	public static void main (String[] args) {
		String url = "http://text-to-speech.imtranslator.net/";
		ExecutorService service = Executors.newFixedThreadPool (2);
		List<String> words = Words.getWords ("parsed-data");
		for (int i = 0, n = words.size () ; i < n ; i++) {
			String word = words.get (i);
			SimpleWorker worker = new SimpleWorker (service, url, word);
			Future future = service.submit (worker);
			try {
				if (future.get () != null) {
					worker = new SimpleWorker (service, url, word);
				}
			} catch (InterruptedException | ExecutionException e) {
				System.err.println (e);
			}
		}
		service.shutdown ();
		try {
			service.awaitTermination (1, TimeUnit.SECONDS);
			System.out.println ("All Tasks are finished!");
		} catch (InterruptedException e) {
			System.err.println (e);
		}
	}
}
