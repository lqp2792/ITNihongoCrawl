package phu.quang.le.CrawlNihongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class SimpleCrawler {

	public static void startCrawling (String url, String word, int state) {
		url = url.replaceAll ("\\*", word);
		try {
			System.out.println (url);
			URL uriLink = new URL (url);
			URLConnection connection = uriLink.openConnection ();
			connection.addRequestProperty ("User-agent",
					"Mozilla/5.0 (X11; Linux x86_64) "
							+ "AppleWebKit/537.36 (KHTML, like Gecko) "
							+ "Chrome/36.0.1985.125 Safari/537.36");
			Parser parser = new Parser (connection);
			NodeList list = parser.extractAllNodesThatMatch (new AndFilter (
					new NodeClassFilter (Div.class),
					new HasAttributeFilter ("id", "main")));
			list.visitAllNodesWith (new SimpleVisitor (state));
			Thread.sleep (1000);
		} catch (ParserException | IOException e) {
			System.err.println (e);
		} catch (InterruptedException e) {
			System.err.println (e);
		}
	}

	public static void main (String[] args) {
		List<String> urls = new ArrayList<String> ();
		urls.add ("http://ejje.weblio.jp/content/*");
		urls.add ("http://ejje.weblio.jp/sentence/content/*");
		urls.add ("http://ejje.weblio.jp/english-thesaurus/content/*");
		BufferedReader reader = new BufferedReader (new InputStreamReader (
				ClassLoader.getSystemResourceAsStream ("parsed-data")));
		String word = null;
		try {
			while ((word = reader.readLine ()) != null) {
				for(int i=0; i<urls.size (); i++) {
					String url = urls.get (i);
					startCrawling (url, word, i+1);
				}
			}
			
		} catch (IOException e) {
			System.err.println (e);
		}
	}
}
