package phu.quang.le.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

	public static List<String> hiragana = new ArrayList<> (Arrays.asList ("あ", "い", "う",
			"え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ",
			"て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む",
			"め", "も", "や", "ゆ", "よ", "わ", "を", "ら", "り", "る", "れ", "ろ", "が", "ぎ", "ぐ",
			"げ", "ご", "ざ", "じ", "ず", "ぜ", "ぞ", "だ", "ぢ", "づ", "で", "ど", "ば", "び", "ぶ",
			"べ", "ぼ", "ぱ", "ぴ", "ぷ", "ぺ", "ぽ", "きゃ", "きゅ", "きょ", "しゃ", "しゅ", "しょ", "ちゃ",
			"ちゅ", "ちょ", "にゃ", "にゅ", "にょ", "ひゃ", "ひゅ", "ひょ", "みゃ", "みゅ", "みょ", "りゃ", "りゅ",
			"りょ", "ぎゃ", "ぎゅ", "ぎょ", "じゃ", "じゅ", "じょ", "びゃ", "びゅ", "びょ", "ぴゃ", "ぴゅ", "ぴょ",
			"ん"));
	public static List<String> katakana = new ArrayList<> (Arrays.asList ("ア", "イ", "ウ",
			"エ", "オ", "カ", "キ", "ク", "ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ",
			"テ", "ト", "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ", "マ", "ミ", "ム",
			"メ", "モ", "ヤ", "ユ", "ヨ", "ワ", "ヲ", "ラ", "リ", "ル", "レ", "ロ", "ガ", "ギ", "グ",
			"ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ", "ダ", "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ",
			"ベ", "ボ", "パ", "ピ", "プ", "ペ", "ポ", "キャ", "キュ", "キョ", "シャ", "シュ", "ショ", "チャ",
			"チュ", "チョ", "ニャ", "ニュ", "ニョ", "ヒャ", "ヒュ", "ヒョ", "ミャ", "ミュ", "ミョ", "リャ", "リュ",
			"リョ", "ギャ", "ギュ", "ギョ", "ジャ", "ジュ", "ジョ", "ビャ", "ビュ", "ビョ", "ピャ", "ピュ", "ピョ",
			"ン", "ファ", "ウェ", "ジェ"));
	public static List<String> romaji = new ArrayList<> (Arrays.asList ("a", "i", "u",
			"e", "o", "ka", "ki", "ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta",
			"chi", "tsu", "te", "to", "na", "ni", "nu", "ne", "no", "ha", "hi", "fu",
			"he", "ho", "ma", "mi", "mu", "me", "mo", "ya", "yu", "yo", "wa", "wo", "ra",
			"ri", "ru", "re", "ro", "ga", "gi", "gu", "ge", "go", "za", "ji", "zu", "ze",
			"zo", "da", "ji", "zu", "de", "do", "ba", "bi", "bu", "be", "bo", "pa", "pi",
			"pu", "pe", "po", "kya", "kyu", "kyo", "sha", "shu", "sho", "cha", "chu",
			"cho", "nya", "nyu", "nyo", "hya", "hyu", "hyo", "mya", "myu", "myo", "rya",
			"ryu", "ryo", "gya", "gyu", "gyo", "ja", "ju", "jo", "bya", "byu", "byo",
			"pya", "pyu", "pyo", "n", "fua", "we", "jie"));

	public static int getWordType (String word) {
		int type = 0;
		if (!JapaneseCharMatchers.HIRAGANA.retainFrom (
				Character.toString (word.charAt (0))).isEmpty ()) {
			type = 1;
		} else if (!JapaneseCharMatchers.KATAKANA.retainFrom (
				Character.toString (word.charAt (0))).isEmpty ()) {
			type = 2;
		} else if (!JapaneseCharMatchers.KANJI.retainFrom (
				Character.toString (word.charAt (0))).isEmpty ()) {
			type = 3;
		} else {
			type = 4;
		}
		return type;
	}

	public static String ltrim (String s) {
		int i = 0;
		while (i < s.length () && Character.isWhitespace (s.charAt (i))) {
			i++;
		}
		return s.substring (i);
	}

	public static String rtrim (String s) {
		int i = s.length () - 1;
		while (i > 0 && Character.isWhitespace (s.charAt (i))) {
			i--;
		}
		return s.substring (0, i + 1);
	}

	public static String convertToRomaji (String word) {
		StringBuffer convertedWord = new StringBuffer ();
		int type = getWordType (word);
		int index = -1;
		for (int i = 0 ; i < word.length () ; i++) {
			Character c = word.charAt (i);
			if (i + 1 < word.length ()) {
				String post = c.toString () + word.charAt (i + 1);
				if (type == 1) {
					index = hiragana.indexOf (post);
				}
				if (type == 2) {
					index = katakana.indexOf (post);
				}
				if (index != -1) {
					convertedWord.append (romaji.get (index));
					++i;
					continue;
				}
			}
			if (type == 1) {
				index = hiragana.indexOf (c.toString ());
			}
			if (type == 2) {
				index = katakana.indexOf (c.toString ());
			}
			if (index != -1) {
				convertedWord.append (romaji.get (index));
			} else {
				if (c.toString ().equals ("っ") || c.toString ().equals ("ッ")) {
					Character nextC = word.charAt (i + 1);
					if (type == 1) {
						index = hiragana.indexOf (nextC.toString ());
					}
					if (type == 2) {
						index = katakana.indexOf (nextC.toString ());
					}
					String roma = romaji.get (index);
					Character romaC = roma.charAt (0);
					convertedWord.append (romaC.toString ());
				} else if(c.toString ().equals ("ー")) { 
					convertedWord.append ("-");
				} else {
					convertedWord.append (c.toString ());
				}
			}
		}
		return convertedWord.toString ();
	}

	public static void main (String[] args) {
		System.out.println (convertToRomaji ("せってい"));
	}
}
