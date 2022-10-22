package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import dao.DAO;

class DAOGetterSetterTest {

	@Test
	@Order(1)
	@DisplayName("Toimiiko getterit ja setterit")
	void saapimisValiTest() {
		DAO d = new DAO();
		d.setSaapumisvali(50);

		assertEquals(50, d.getSaapumisvali(), "Saapumisväli ei ollut oikea");
	}

	@Test
	@Order(2)
	@DisplayName("Toimiiko getterit ja setterit")
	void AsiakasRahaaKaytettyTest() {
		DAO d = new DAO();
		d.setArahaaKaytetty(1000);
		assertEquals(1000, d.getArahaaKaytetty(), "Asiakkaan käyttämä raha ei ollut oikea");
	}

}
