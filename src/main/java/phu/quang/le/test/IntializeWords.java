package phu.quang.le.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import phu.quang.le.Utils.DBUtility;
import phu.quang.le.Utils.WordService;

public class IntializeWords {

	public static void main (String args[]) throws IOException, SQLException {
		BufferedReader br = new BufferedReader (new InputStreamReader (
				ClassLoader.getSystemResourceAsStream ("result.txt")));
		String kanji = null;
		String hiragana = null;
		String katakana = null;
		String romaji = null;
		String imi = null;
		String reibun = null;
		int rank = 0;
		String pathSound = null;
		String synonyms = null;
		String temp[] = null;
		String word = null;
		String yomikata = null;
		boolean inExample = false;
		boolean inSynonyms = false;
		int typeWord;
		try {
			Connection connection = DBUtility.getConnection ();
			String sql = "INSERT INTO 語彙 values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pst = connection.prepareStatement (sql);
			connection.setAutoCommit (false);
			try {
				int x = 1;
				StringBuilder sb = new StringBuilder ();
				String line = br.readLine ();
				while (line != null) {
					temp = line.split ("\t");
					for (int i = 0 ; i < temp.length ; i++) {
						if (temp[i].contains ("word:")) {
							word = temp[i].split ("word:")[1];
							pathSound = "/swf/" + word + ".swf";
							typeWord = WordService.getWordType (word);
							if (typeWord == 1) {
								kanji = null;
								hiragana = word;
								katakana = null;
							} else if (typeWord == 2) {
								kanji = null;
								katakana = word;
								hiragana = null;
							} else if (typeWord == 3) {
								kanji = word;
								katakana = null;
							} else {
							}
							inSynonyms = false;
						} else if (temp[i].contains ("Yomikata:")) {
							yomikata = temp[i].split ("Yomikata:")[1];
							if (kanji != null) {
								hiragana = yomikata;
							}
						} else if (temp[i].contains ("Romaji:")) {
							romaji = temp[i].split ("Romaji:")[1];
						} else if (temp[i].contains ("Imi:")) {
							imi = temp[i].split ("Imi:")[1];
						} else if (temp[i].contains ("Examples:")
								&& (temp[i].contains ("Synonyms:") == false)) {
							System.out.println (temp[i]);
							if (temp[i].split ("Examples:").length > 1) {
								if (temp[i].split ("Examples:")[1] != null) {
									reibun = ConverseWord.converse (temp[i].split ("Examples:")[1]);
									inExample = true;
								} else {
									reibun = " not found";
								}
							} else {
								reibun = "not found ";
							}
						} else if (temp[i].contains ("Synonyms:")) {
							if (temp[i].split ("Synonyms:").length > 1) {
								synonyms = temp[i].split ("Synonyms:")[1];
								inSynonyms = true;
								inExample = false;
							} else {
								synonyms = " not found";
							}
						} else {
							if (inExample == true) {
								if (temp[i] != null && (reibun.length () < 800)) {
									reibun += "|" + ConverseWord.converse (temp[i]);
								}
							} else if (inSynonyms == true) {
								synonyms += "|" + temp[i];
							}
						}
					}
					line = br.readLine ();
					pst.setString (1, kanji);
					pst.setString (2, hiragana);
					pst.setString (3, katakana);
					pst.setString (4, romaji);
					pst.setString (5, imi);
					pst.setString (6, reibun);
					pst.setInt (7, x);
					pst.setString (8, pathSound);
					pst.setString (9, synonyms);
					pst.addBatch ();
					// pst.executeUpdate();
					inExample = false;
					inSynonyms = false;
					System.out.println ("line" + x++);
					// if(x>3000){
					// break;
					// }
				}
				pst.executeBatch ();
				connection.commit ();
				System.out.println ("Record is inserted into DBUSER table!");
			} finally {
				br.close ();
			}
		} catch (SQLException e) {
			System.out.println ((imi).length ());
			System.out.println (imi);
			e.printStackTrace ();
		} 
	}
}
