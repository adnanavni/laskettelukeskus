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

	private static double LKkokonaisaika, LKtulot, LKasiakkaaidenMaara, LKPoistuneetAsiakkaat, LKlapimenoaikaAVG,
			LKsuoritusteho;

	private static double kassaTulot, kassaPalvellutAsiakkaat, kassaAktiiviAika, kassaPalveluaikaAVG, kassaKayttoAste;

	private static double vuokraamoTulot, vuokraamoPalvellutAsiakkaat, vuokraamoAktiiviAika, vuokraamoPalveluaikaAVG,
			vuokraamoKayttoAste;

	private static double kahvilaTulot, kahvilaPalvellutAsiakkaat, kahvilaAktiiviAika, kahvilaPalveluaikaAVG,
			kahvilaKayttoAste;

	public static double rinne1Tulot, rinne1PalvellutAsiakkaat, rinne1AktiiviAika, rinne1PalveluaikaAVG,
			rinne1KayttoAste;

	public static double rinne2Tulot, rinne2PalvellutAsiakkaat, rinne2AktiiviAika, rinne2PalveluaikaAVG,
			rinne2KayttoAste;

	private static double ArahaaKaytetty, AvietettyAika, ApalvelupisteidenMaara;

	private static double saapumisvali, kassaPalveluAjanKA, lipunHinta, vuokraamoPalveluAjanKA, vuokraamoOstostenKA,
			kahvilaPalveluAjanKA, kahvilaOstostenKA, rinne1PalveluAjanKA, rinne2PalveluAjanKA;

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

	public void tallenna() {
		tallennaLKData();
		tallennaPPData();
		tallennaAData();
		tallennaInputData();
	}

	public void tallennaLKData() {
		String query = "INSERT INTO laskettelukeskus(kokonaisaika, tulot, asiakkaidenMaara, poistuneetAsiakkaat, lapimenoaikaAVG, suoritusteho)"
				+ "VALUES(?,?,?,?,?,?)";
		try {
			PreparedStatement st = myCon.prepareStatement(query);
			st.setDouble(1, LKkokonaisaika);
			st.setDouble(2, LKtulot);
			st.setDouble(3, LKasiakkaaidenMaara);
			st.setDouble(4, LKPoistuneetAsiakkaat);
			st.setDouble(5, LKlapimenoaikaAVG);
			st.setDouble(6, LKsuoritusteho);

			st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void tallennaPPData() {

		String[] lista = { "kassa", "vuokraamo", "kahvila", "rinne1", "rinne2" };
		for (int i = 0; i < lista.length; i++) {
			String query = "INSERT INTO " + lista[i]
					+ " (tulot, palvellutAsiakkaat, aktiiviAika, palveluAikaAVG, kayttoaste)" + "VALUES(?,?,?,?,?)";
			try {
				PreparedStatement st = myCon.prepareStatement(query);

				if (lista[i].equals("kassa")) {
					st.setDouble(1, kassaTulot);
					st.setInt(2, (int) kassaPalvellutAsiakkaat);
					st.setDouble(3, kassaAktiiviAika);
					st.setDouble(4, kassaPalveluaikaAVG);
					st.setDouble(5, kassaKayttoAste);
				} else if (lista[i].equals("vuokraamo")) {
					st.setDouble(1, vuokraamoTulot);
					st.setInt(2, (int) vuokraamoPalvellutAsiakkaat);
					st.setDouble(3, vuokraamoAktiiviAika);
					st.setDouble(4, vuokraamoPalveluaikaAVG);
					st.setDouble(5, vuokraamoKayttoAste);
				} else if (lista[i].equals("kahvila")) {
					st.setDouble(1, kahvilaTulot);
					st.setInt(2, (int) kahvilaPalvellutAsiakkaat);
					st.setDouble(3, kahvilaAktiiviAika);
					st.setDouble(4, kahvilaPalveluaikaAVG);
					st.setDouble(5, kahvilaKayttoAste);
				} else if (lista[i].equals("rinne1")) {
					st.setDouble(1, rinne1Tulot);
					st.setInt(2, (int) rinne1PalvellutAsiakkaat);
					st.setDouble(3, rinne1AktiiviAika);
					st.setDouble(4, rinne1PalveluaikaAVG);
					st.setDouble(5, rinne1KayttoAste);
				} else if (lista[i].equals("rinne2")) {
					st.setDouble(1, rinne2Tulot);
					st.setInt(2, (int) rinne2PalvellutAsiakkaat);
					st.setDouble(3, rinne2AktiiviAika);
					st.setDouble(4, rinne2PalveluaikaAVG);
					st.setDouble(5, rinne2KayttoAste);
				}

				st.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void tallennaAData() {
		String query = "INSERT INTO Asiakas(rahaaKaytetty, vietettyAika, palvelupisteidenMaara)" + "VALUES(?,?,?)";
		try {
			PreparedStatement st = myCon.prepareStatement(query);

			st.setDouble(1, ArahaaKaytetty);
			st.setDouble(2, AvietettyAika);
			st.setDouble(3, ApalvelupisteidenMaara);

			st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void tallennaInputData() {
		String query = "INSERT INTO syotteet (simulaationKesto, saapumisvali, kassaPalveluAjanKA,lipunHinta,vuokraamoPalveluAjanKA,vuokraamoOstostenKA,"
				+ "kahvilaPalveluAjanKA,kahvilaOstostenKA,rinne1PalveluAjanKA,rinne2PalveluAjanKA)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement st = myCon.prepareStatement(query);

			st.setDouble(1, LKkokonaisaika);
			st.setDouble(2, saapumisvali);
			st.setDouble(3, kassaPalveluAjanKA);
			st.setDouble(4, lipunHinta);
			st.setDouble(5, vuokraamoPalveluAjanKA);
			st.setDouble(6, vuokraamoOstostenKA);
			st.setDouble(7, kahvilaPalveluAjanKA);
			st.setDouble(8, kahvilaOstostenKA);
			st.setDouble(9, rinne1PalveluAjanKA);
			st.setDouble(10, rinne2PalveluAjanKA);

			st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSaapumisvali(double saapumisvali) {
		DAO.saapumisvali = saapumisvali;
	}

	public void setKassaPalveluAjanKA(double kassaPalveluAjanKA) {
		DAO.kassaPalveluAjanKA = kassaPalveluAjanKA;
	}

	public void setLipunHinta(double lipunHinta) {
		DAO.lipunHinta = lipunHinta;
	}

	public void setVuokraamoPalveluAjanKA(double vuokraamoPalveluAjanKA) {
		DAO.vuokraamoPalveluAjanKA = vuokraamoPalveluAjanKA;
	}

	public void setVuokraamoOstostenKA(double vuokraamoOstostenKA) {
		DAO.vuokraamoOstostenKA = vuokraamoOstostenKA;
	}

	public void setKahvilaPalveluAjanKA(double kahvilaPalveluAjanKA) {
		DAO.kahvilaPalveluAjanKA = kahvilaPalveluAjanKA;
	}

	public void setKahvilaOstostenKA(double kahvilaOstostenKA) {
		DAO.kahvilaOstostenKA = kahvilaOstostenKA;
	}

	public void setRinne1PalveluAjanKA(double rinne1PalveluAjanKA) {
		DAO.rinne1PalveluAjanKA = rinne1PalveluAjanKA;
	}

	public void setRinne2PalveluAjanKA(double rinne2PalveluAjanKA) {
		DAO.rinne2PalveluAjanKA = rinne2PalveluAjanKA;
	}

	public void setKassaTulot(double kassaTulot) {
		DAO.kassaTulot = kassaTulot;
	}

	public void setKassaPalvellutAsiakkaat(double kassaPalvellutAsiakkaat) {
		DAO.kassaPalvellutAsiakkaat = kassaPalvellutAsiakkaat;
	}

	public void setKassaAktiiviAika(double kassaAktiiviAika) {
		DAO.kassaAktiiviAika = kassaAktiiviAika;
	}

	public void setKassaPalveluaikaAVG(double kassaPalveluaikaAVG) {
		DAO.kassaPalveluaikaAVG = kassaPalveluaikaAVG;
	}

	public void setKassaKayttoAste(double kassaKayttoAste) {
		DAO.kassaKayttoAste = kassaKayttoAste;
	}

	public void setVuokraamoTulot(double vuokraamoTulot) {
		DAO.vuokraamoTulot = vuokraamoTulot;
	}

	public void setVuokraamoPalvellutAsiakkaat(double vuokraamoPalvellutAsiakkaat) {
		DAO.vuokraamoPalvellutAsiakkaat = vuokraamoPalvellutAsiakkaat;
	}

	public void setVuokraamoAktiiviAika(double vuokraamoAktiiviAika) {
		DAO.vuokraamoAktiiviAika = vuokraamoAktiiviAika;
	}

	public void setVuokraamoPalveluaikaAVG(double vuokraamoPalveluaikaAVG) {
		DAO.vuokraamoPalveluaikaAVG = vuokraamoPalveluaikaAVG;
	}

	public void setVuokraamoKayttoAste(double vuokraamoKayttoAste) {
		DAO.vuokraamoKayttoAste = vuokraamoKayttoAste;
	}

	public void setKahvilaTulot(double kahvilaTulot) {
		DAO.kahvilaTulot = kahvilaTulot;
	}

	public void setKahvilaPalvellutAsiakkaat(double kahvilaPalvellutAsiakkaat) {
		DAO.kahvilaPalvellutAsiakkaat = kahvilaPalvellutAsiakkaat;
	}

	public void setKahvilaAktiiviAika(double kahvilaAktiiviAika) {
		DAO.kahvilaAktiiviAika = kahvilaAktiiviAika;
	}

	public void setKahvilaPalveluaikaAVG(double kahvilaPalveluaikaAVG) {
		DAO.kahvilaPalveluaikaAVG = kahvilaPalveluaikaAVG;
	}

	public void setKahvilaKayttoAste(double kahvilaKayttoAste) {
		DAO.kahvilaKayttoAste = kahvilaKayttoAste;
	}

	public void setRinne1Tulot(double rinne1Tulot) {
		DAO.rinne1Tulot = rinne1Tulot;
	}

	public void setRinne1PalvellutAsiakkaat(double rinne1PalvellutAsiakkaat) {
		DAO.rinne1PalvellutAsiakkaat = rinne1PalvellutAsiakkaat;
	}

	public void setRinne1AktiiviAika(double rinne1AktiiviAika) {
		DAO.rinne1AktiiviAika = rinne1AktiiviAika;
	}

	public void setRinne1PalveluaikaAVG(double rinne1PalveluaikaAVG) {
		DAO.rinne1PalveluaikaAVG = rinne1PalveluaikaAVG;
	}

	public void setRinne1KayttoAste(double rinne1KayttoAste) {
		DAO.rinne1KayttoAste = rinne1KayttoAste;
	}

	public void setRinne2Tulot(double rinne2Tulot) {
		DAO.rinne2Tulot = rinne2Tulot;
	}

	public void setRinne2PalvellutAsiakkaat(double rinne2PalvellutAsiakkaat) {
		DAO.rinne2PalvellutAsiakkaat = rinne2PalvellutAsiakkaat;
	}

	public void setRinne2AktiiviAika(double rinne2AktiiviAika) {
		DAO.rinne2AktiiviAika = rinne2AktiiviAika;
	}

	public void setRinne2PalveluaikaAVG(double rinne2PalveluaikaAVG) {
		DAO.rinne2PalveluaikaAVG = rinne2PalveluaikaAVG;
	}

	public void setRinne2KayttoAste(double rinne2KayttoAste) {
		DAO.rinne2KayttoAste = rinne2KayttoAste;
	}

	public void setArahaaKaytetty(double arahaaKaytetty) {
		ArahaaKaytetty = arahaaKaytetty;
	}

	public void setApalvelupisteidenMaara(double apalvelupisteidenMaara) {
		ApalvelupisteidenMaara = apalvelupisteidenMaara;
	}

	public void setAvietettyAika(double avietettyAika) {
		AvietettyAika = avietettyAika;
	}

	public void setPPtulot(double pPtulot) {
		kassaTulot = pPtulot;
	}

	public void setPPpalvellutAsiakkaat(double pPpalvellutAsiakkaat) {
		kassaPalvellutAsiakkaat = pPpalvellutAsiakkaat;
	}

	public void setPPaktiiviAika(double pPaktiiviAika) {
		kassaAktiiviAika = pPaktiiviAika;
	}

	public void setPPpalveluaikaAVG(double pPpalveluaikaAVG) {
		kassaPalveluaikaAVG = pPpalveluaikaAVG;
	}

	public void setPPkayttoAste(double pPkayttoAste) {
		kassaKayttoAste = pPkayttoAste;
	}

	public void setLKsuoritusteho(double pPsuoritusteho) {
		LKsuoritusteho = pPsuoritusteho;
	}

	public void setLKkokonaisaika(double lKkokonaisaika) {
		LKkokonaisaika = lKkokonaisaika;
	}

	public void setLKtulot(double lKtulot) {
		LKtulot = lKtulot;
	}

	public void setLKasiakkaaidenMaara(double lKasiakkaaidenMaara) {
		LKasiakkaaidenMaara = lKasiakkaaidenMaara;
	}

	public void setLKPoistuneetAsiakkaat(double lKPoistuneetAsiakkaat) {
		LKPoistuneetAsiakkaat = lKPoistuneetAsiakkaat;
	}

	public void setLKlapimenoaikaAVG(double lKlapimenoaikaAVG) {
		LKlapimenoaikaAVG = lKlapimenoaikaAVG;
	}
}
