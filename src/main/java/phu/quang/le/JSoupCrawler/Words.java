package phu.quang.le.JSoupCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Words {

	public static List<String> getWords (String filename) {
		List<String> words = new ArrayList<> ();
		BufferedReader br = new BufferedReader (new InputStreamReader (
				ClassLoader.getSystemResourceAsStream (filename)));
		String word = null;
		try {
			while ((word = br.readLine ()) != null) {
				words.add (word);
			}
			br.close ();
		} catch (IOException e) {
			System.err.println (e);
		}
		return words;
	}
}
