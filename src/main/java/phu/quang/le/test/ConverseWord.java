package phu.quang.le.test;

import phu.quang.le.Utils.WordService;

public class ConverseWord {
	public static String converse(String text) {
		int index = -1;
		for (int i = 0 ; i < text.length () ; i++) {
			Character c = text.charAt (i);
			if (c.toString ().equals (" ")) {
				continue;
			}
			if (WordService.getWordType (c.toString ()) == 4
					&& !c.toString ().equals ("。") && !c.toString ().equals ("、")) {
				index = i;
				break;
			}
		}
		text = text.substring (0, index) + "*" + text.substring (index);
		return text;
	}
}
