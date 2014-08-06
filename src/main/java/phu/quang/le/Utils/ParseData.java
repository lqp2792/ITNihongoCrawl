package phu.quang.le.Utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ParseData {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader (new InputStreamReader (
					ClassLoader.getSystemResourceAsStream ("data")));
			PrintWriter writer = new PrintWriter("parsed-data", "UTF-8");
			String line = null;
			int i = 0;
			line = br.readLine();
			while((line = br.readLine()) != null && i < 3000) {
				StringTokenizer tokenizer = new StringTokenizer(line, "\t ");
				String word = tokenizer.nextToken();
//				word = parseWord(word);
				System.out.println(i + " " + word);
				writer.write(word+System.getProperty("line.separator"));
				i++;
			}
			writer.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static String parseWord(String word) {
		String newWord = word;

		if((charType(word.charAt(0)) == 3) && (charType(word.charAt(1)) == 3)) {
			int index = 0;
			for(int i=0; i<word.length(); i++) {
				if(charType(word.charAt(i)) == 1) break;
				index++;
			}
			newWord = word.substring(0, index);
		}

		
		return newWord;
	}
	
	public static int charType(char c) {
		int type = -1;
		if (!JapaneseCharMatchers.HIRAGANA.retainFrom(
				Character.toString(c)).isEmpty()) {
			type = 1;
		} else if (!JapaneseCharMatchers.KATAKANA.retainFrom(
				Character.toString(c)).isEmpty()) {
			type = 2;
		} else if (!JapaneseCharMatchers.KANJI.retainFrom(
				Character.toString(c)).isEmpty()) {
			type = 3;
		}
		
		return type;
	}
}
