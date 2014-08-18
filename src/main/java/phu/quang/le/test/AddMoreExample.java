package phu.quang.le.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import phu.quang.le.Utils.DBUtility;

public class AddMoreExample {

	public static void main (String[] args) {
		Connection connection = DBUtility.getConnection ();
		ExecutorService service = Executors.newFixedThreadPool (10);
		String sql = "Select 漢字, ひらがな, カタカナ from 語彙 where 例文 = 'not found'";
		String word = null;
		try {
			Statement stmt = connection.createStatement ();
			ResultSet rs = stmt.executeQuery (sql);
			int i = 1;
			while (rs.next ()) {
				if (rs.getString (2) != null) {
					System.out.println (i + " " + rs.getString (2));
					word = rs.getString (2);
				} else if (rs.getString (3) != null) { 
					System.out.println (i + " " + rs.getString (3));
					word = rs.getString (3);
				} else {
					System.out.println (i + " " + rs.getString (1));
					word = rs.getString (1);
				}
				String url = "http://ejje.weblio.jp/sentence/content/" + word;
				SimpleWorker w = new SimpleWorker (url, word);
				service.execute (w);
				i++;
			}
			service.shutdown ();
		} catch (SQLException e) {
			System.err.println (e);
		}
	}
}
