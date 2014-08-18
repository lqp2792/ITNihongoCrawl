package phu.quang.le.test;

import java.io.IOException;
import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import phu.quang.le.Utils.DBUtility;

public class IntializeTest {

	public static void main (String[] args) {
		ExecutorService service = Executors.newFixedThreadPool (5);
		Connection connection = DBUtility.getConnection ();
		getTest ();
	}

	public static void getTest () {
		String url = "http://dictionary.goo.ne.jp/srch/all/*/m0u/";
		String word = "出題";
		url = url.replace ("*", word);
		System.out.println (url);
		try {
			Document doc = Jsoup.connect (url).get ();
			Element div = doc.select ("div.allResultList").first ();
			Element dd = div.select ("dd").first ();
			System.out.println (minText (dd.ownText ()));
		} catch (IOException e) {
		}
	}

	public static String minText (String text) {
		boolean shouldCut = true;
		text = text.replace (" ", "");
		int start = -1, end = -1;
		System.out.println (text);
		while (shouldCut) {
			start = text.indexOf ("［");
			end = text.indexOf ("］");
			System.out.println (start + " " + end);
			if(start != -1) {
				text = text.substring (0, start) + text.substring (end + 1);
			}
			start = text.indexOf ("(");
			end = text.indexOf (")");
			if(start != -1) {
				text = text.substring (0, start) + text.substring (end + 1);
			}
			start = text.indexOf ("「");
			end = text.indexOf ("」");
			if(start != -1) {
				text = text.substring (0, start) + text.substring (end + 1);
			}
			if (start == -1) {
				shouldCut = false;
			}
		}
		return text;
	}
}
