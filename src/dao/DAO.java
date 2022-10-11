package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAO implements IDAO {

	private static String IPOSOITE = "localhost";
	private static String DATABASE = "laskettelukeskus";
	private static String KAYTTAJA = "olso";
	private static String SALASANA = "olso";

	private Connection myCon;

	public DAO() {
		try {
			myCon = DriverManager.getConnection(
					"jdbc:mariadb://" + IPOSOITE + "/" + DATABASE + "?user=" + KAYTTAJA + "&password=" + SALASANA);
		} catch (SQLException e) {
			printSQLExceptions("DAO(): ", e);
			System.exit(-1);
		}
	}

	private void printSQLExceptions(String methodinNimi, SQLException e) {
		System.err.println("Metodi: " + methodinNimi);
		do {
			System.err.println("Viesti: " + e.getMessage());
			System.err.println("Virhekoodi: " + e.getErrorCode());
			System.err.println("SQL-tilakoodi: " + e.getSQLState());
		} while (e.getNextException() != null);
	}

	public void tallennaDataa() {
		String query = "INSERT INTO laskettelukeskus(kokonaisaika, tulot, asiakkaidenMaara, poistuneetAsiakkaat, lapimenoaikaAVG)"
				+ "VALUES(?,?,?,?,?)";
		try {
			PreparedStatement st = myCon.prepareStatement(query);
			st.setDouble(1, 1);
			st.setDouble(2, 1);
			st.setDouble(3, 1);
			st.setDouble(4, 1);
			st.setDouble(5, 1);

			st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
