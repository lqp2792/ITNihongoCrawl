package phu.quang.le.CrawlNihongo;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Tag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.ParserUtils;
import org.htmlparser.visitors.NodeVisitor;

public class SimpleVisitor extends NodeVisitor {

	public int state;
	public int yomikata = 0;
	public int imi = 0;
	public Example example;
	int limit = 0;

	public SimpleVisitor (int state) {
		this.state = state;
	}

	@Override
	public void beginParsing () {
		super.beginParsing ();
	}

	@Override
	public void visitTag (Tag tag) {
		if (state == 1) {
			getWordMeaning (tag);
		}
		if (state == 2) {
			getWordExamples (tag);
		}
		if (state == 3) {
			getSynonymWords (tag);
		}
	}

	public void getWordMeaning (Tag tag) {
		if (tag instanceof HeadingTag) {
			HeadingTag ht = (HeadingTag) tag;
			if (ht.getAttribute ("class") != null
					&& ht.getAttribute ("class").equals ("midashigo")) {
				yomikata++;
				if (yomikata == 1) {
					System.out.println (ParserUtils.trimAllTags (ht.getStringText (),
							true));
				}
			}
		} else if (tag instanceof Div) {
			if (tag.getAttribute ("class") != null) {
				if (tag.getAttribute ("class").equals ("level1")) {
					Div div = (Div) tag;
					int a = div.findPositionOf ("《");
					int b = div.findPositionOf ("》");
					if (a != -1 && b != -1) {
						System.out.println (div.getChildCount ());
						while (a < b) {
							div.removeChild (a);
							a++;
						}
					}
					imi++;
				}
			}
		} else if (tag instanceof LinkTag) {
			LinkTag lt = (LinkTag) tag;
			if (yomikata == 1 && imi == 1) {
				System.out.println (ParserUtils.trimAllTags (lt.getStringText (), false));
			}
		}
	}

	public void getWordExamples (Tag tag) {
		List<Example> examples = new ArrayList<> ();
		if (tag instanceof Div) {
			if (tag.getAttribute ("class") != null) {
				if (tag.getAttribute ("class").equals ("qotC")) {
					limit++;
					if (limit < 10) {
						if (example != null) {
							System.out.println (example.toString ());
						}
						example = new Example ();
					}
				}
			}
		} else if (tag instanceof ParagraphTag) {
			if (tag.getAttribute ("class") != null) {
				ParagraphTag pt = (ParagraphTag) tag;
				if (pt.getAttribute ("class").equals ("qotCJJ")) {
					example.setExample (ParserUtils.trimAllTags (pt.getStringText (),
							false));
				}
				if (pt.getAttribute ("class").equals ("qotCJE")) {
					example.setMean (ParserUtils.trimAllTags (pt.getStringText (), true));
				}
			}
		}
	}

	public void getSynonymWords (Tag tag) {
		if (tag instanceof ParagraphTag) {
			if (tag.getParent () instanceof Div) {
				Div divParent = (Div) tag.getParent ();
				if (divParent.getAttribute ("class").equals ("wdntTCRW")) {
					ParagraphTag pt = (ParagraphTag) tag;
					if (pt.getAttribute ("class").equals ("wdntCL")) {
						System.out.println (pt.getStringText ());
					}
				}
			}
		}
	}

	class Example {

		private String example;
		private String mean;

		public String getExample () {
			return example;
		}

		public void setExample (String example) {
			this.example = example;
		}

		public String getMean () {
			return mean;
		}

		public void setMean (String mean) {
			this.mean = mean;
		}

		@Override
		public String toString () {
			return getExample () + " " + getMean ();
		}
	}
}