package phu.quang.le.Crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import phu.quang.le.Utils.StringUtils;

public class SimpleJsoupCrawler {

	public static void startCrawling (String url, String word, int state) {
		String yomikata = null;
		List<String> meaning = new ArrayList<> ();
		List<String> synonyms = new ArrayList<> ();
		Map<String, String> sentences = new HashMap<> ();
		try {
			Document doc = Jsoup.connect (url).timeout (10000).get ();
			System.out.println ("\n" + url);
			switch (state) {
			case 0:
				Elements tds = doc.select ("td.kana_column");
				Element td = tds.get (0);
				yomikata = Jsoup.parse (td.html ()).text ();
				tds = doc.select ("td.meanings_column");
				td = tds.get (0);
				Elements delete = td.select ("strong");
				for (Element del : delete) {
					del.remove ();
				}
				String text = Jsoup.parse (td.html ()).text ();
				StringTokenizer tokens = new StringTokenizer (text, ";");
				meaning.add (StringUtils.ltrim (tokens.nextToken ()));
				System.out.println ("Yomikata: " + yomikata);
				System.out.println ("Romaji: " + StringUtils.convertToRomaji (yomikata));
				printMeaning (meaning);
				break;
			case 1:
				Elements jpSentence = doc.select ("td.japanese");
				Elements engSentence = doc.select ("td.english");
				int limit = 0;
				for (int i = 0 ; i < jpSentence.size () ; i++, limit++) {
					if (limit == 10) {
						break;
					}
					Element jpText = jpSentence.get (i);
					Element engText = engSentence.get (i);
					sentences.put (Jsoup.parse (jpText.html ()).text (),
							Jsoup.parse (engText.html ()).text ());
				}
				printSentences (sentences);
				break;
			case 2:
				Elements ps = doc.select ("p.wdntCL");
				for (int i = 0 ; i < ps.size () ; i++) {
					text = Jsoup.parse (ps.get (i).html ()).text ();
					tokens = new StringTokenizer (text, ", ");
					while (tokens.hasMoreTokens ()) {
						word = tokens.nextToken ();
						if (!synonyms.contains (word)) {
							synonyms.add (word);
						}
						if (synonyms.size () > 10) {
							break;
						}
					}
					if (synonyms.size () > 10) {
						break;
					}
				}
				printSynonyms (synonyms);
				break;
			}
		} catch (IOException e) {
			System.err.println (e);
		}
	}

	public static void main (String[] args) {
		List<String> urls = new ArrayList<> ();
		List<String> words = Words.getWords ("parsed-data");
		urls.add ("http://jisho.org/words?jap=*&eng=&dict=edict");
		urls.add ("http://jisho.org/sentences?jap=*");
		urls.add ("http://ejje.weblio.jp/english-thesaurus/content/*");
		for (int i = 0, n = words.size () ; i < n ; i++) {
			String word = words.get (i);
			for (int j = 0 ; j < urls.size () ; j++) {
				String url = urls.get (j).replaceAll ("\\*", word);
				startCrawling (url, word, j);
				try {
					Thread.sleep (500);
				} catch (InterruptedException e) {
					System.err.println (e);
				}
			}
		}
	}

	public static void printMeaning (List<String> meaning) {
		System.out.println ("Imi: ");
		for (int i = 0 ; i < meaning.size () ; i++) {
			System.out.println ("\t " + meaning.get (i));
		}
	}

	public static void printSentences (Map<String, String> sentences) {
		System.out.println ("Examples: ");
		for (Map.Entry<String, String> sentence : sentences.entrySet ()) {
			System.out.println ("\t " + sentence.getKey () + " " + sentence.getValue ());
		}
	}

	public static void printSynonyms (List<String> synonyms) {
		System.out.println ("Synonyms: ");
		for (int i = 0 ; i < synonyms.size () ; i++) {
			System.out.print ("\t " + synonyms.get (i) + " ");
			if (i != 0 && i % 5 == 0) {
				System.out.println ("");
			}
		}
	}
}
