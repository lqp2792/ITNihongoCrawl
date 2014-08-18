package phu.quang.le.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import phu.quang.le.Utils.DBUtility;
import phu.quang.le.Utils.WordService;


public class SimpleWorker implements Runnable {
	String url, word;
	
	public SimpleWorker (String url, String word) {
		this.url = url;
		this.word = word;
	}
	@Override
	public void run () {
		try {
			Document doc = Jsoup.connect (url).timeout (20000).get ();
			Element div = doc.select ("div.kijiWrp").first ();
			Elements pjps = div.select ("p.qotCJJ");
			Elements pengs = div.select ("p.qotCJE");
			String example = null;
			int size = 5; 
			if(pengs.size () < 5) {
				size = pengs.size ();
			}
			for (int i = 1 ; i < size ; i++) {
				Element pjp = pjps.get (i);
				Element peng = pengs.get (i);
				String jp = Jsoup.parse (pjp.html ()).text ();
				String eng = peng.ownText ();
				if (example == null) {
					example = jp + "*" + eng;
				} else {
					example += "|" + jp + "*" + eng;
				}
			}
			System.out.println (example);
			Connection connection = DBUtility.getConnection ();
			System.out.println (word);
			int type = WordService.getWordType (word);
			String sql = null;
			if (type == 1) {
				sql = "UPDATE 語彙 set 例文=? where ひらがな=?";
			}
			if (type == 2) {
				sql = "UPDATE 語彙 set 例文=? where カタカナ=?";
			}
			if (type == 3) {
				sql = "UPDATE 語彙 set 例文=? where 漢字=?";
			}
			PreparedStatement pst = connection.prepareStatement (sql);
			pst.setString (1, example);
			pst.setString (2, word);
			pst.executeUpdate ();
		} catch (IOException e) {
			System.err.println (e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
