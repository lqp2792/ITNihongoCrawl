package phu.quang.le.Utils;
import com.google.common.base.CharMatcher;

public class JapaneseCharMatchers {
	public static final CharMatcher HIRAGANA = CharMatcher.inRange(
			(char) 0x3040, (char) 0x309f);

	public static final CharMatcher KATAKANA = CharMatcher.inRange(
			(char) 0x30a0, (char) 0x30ff);

	public static final CharMatcher KANA = HIRAGANA.or(KATAKANA);

	public static final CharMatcher KANJI = CharMatcher.inRange((char) 0x4e00,
			(char) 0x9faf);
}