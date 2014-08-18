package phu.quang.le.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import phu.quang.le.Utils.DBUtility;
import phu.quang.le.Utils.StringUtils;
import phu.quang.le.Utils.WordService;

public class FixRomaji {

	public static void main (String[] args) {
		Connection connection = DBUtility.getConnection ();
		String sql = "Select romaji from 語彙";
		try {
			Statement stmt = connection.createStatement ();
			ResultSet rs = stmt.executeQuery (sql);
			while (rs.next ()) {
				String sql1 = "UPDATE 語彙 SET romaji = ? where romaji = ?";
				PreparedStatement pst = connection.prepareStatement (sql1);
				pst.setString (1, fix (rs.getString (1)));
				pst.setString (2, rs.getString (1));
				pst.executeUpdate ();
			}
		} catch (SQLException e) {
			System.err.println (e);
		}
	}

	public static String fix (String romaji) {
		romaji = romaji.replaceAll ("ィ", "i").replaceAll ("ェ", "e")
				.replaceAll ("ォ", "o").replaceAll ("ュ", "yu");

		int index = -1;
		for (int i = 0 ; i < romaji.length () ; i++) {
			Character c = romaji.charAt (i);
			if (WordService.getWordType (c.toString ()) == 1
					|| WordService.getWordType (c.toString ()) == 2
					|| WordService.getWordType (c.toString ()) == 3) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			System.out.println (romaji);
			String cutRomaji = romaji.substring (index);
			cutRomaji = StringUtils.convertToRomaji (cutRomaji);
			romaji = romaji.substring (0, index) + cutRomaji;
			System.out.println (romaji);
		}
		return romaji;
	}
}
