package phu.quang.le.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WordService {
	/**
	 * Get word ID
	 * 
	 * @param connection
	 *            connection to database
	 * @param word
	 *            word
	 * @return
	 */
	public static int getWordID(Connection connection, String word) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int id = 0;

		String sql = getWordSelectSQLString(word);
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, word);
			rs = pst.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			pst.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	/**
	 * Get sql String depend on word type (hiragana, katakana, kanji)
	 * 
	 * @param word
	 * @return
	 */
	public static String getWordSelectSQLString(String word) {
		String sql = null;
		switch (getWordType(word)) {
		case 1:
			sql = "SELECT * FROM 語彙 where ひらがな = ?";
			break;
		case 2:
			sql = "SELECT * FROM 語彙 where カタカナ = ?";
			break;
		case 3:
			sql = "SELECT * FROM 語彙 where 漢字 = ?";
			break;
		case 4:
			sql = "SELECT * FROM 語彙 where romaji = ?";
			break;
		}

		return sql;
	}

	/**
	 * Get type of word (hiragana, katakana, kanji)
	 * 
	 * @param word
	 * @return
	 */
	public static int getWordType(String word) {
		int type = 0;
		if (!JapaneseCharMatchers.HIRAGANA.retainFrom(
				Character.toString(word.charAt(0))).isEmpty()) {
			type = 1;
		} else if (!JapaneseCharMatchers.KATAKANA.retainFrom(
				Character.toString(word.charAt(0))).isEmpty()) {
			type = 2;
		} else if (!JapaneseCharMatchers.KANJI.retainFrom(
				Character.toString(word.charAt(0))).isEmpty()) {
			type = 3;
		} else {
			type = 4;
		}

		return type;
	}

}
