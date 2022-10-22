package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import simu.model.Asiakas;

/**
 * Asiakas-oliolle tehtävät testit.
 * 
 * @author Adnan Avni
 * @version 1.0
 */
class AsiakasTesti {

	@Test
	@Order(1)
	@DisplayName("Toimiiko ID:n kasvatus")
	void IdTest() {

		for (int i = 1; i < 10; i++) {
			Asiakas idTesti = new Asiakas(1);
			assertEquals(i, idTesti.getId(), "Id ei kasvanut...");
		}
	}

	@Test
	@Order(2)
	@DisplayName("Aika setterit ja getterit")
	void TestKulunutAika() {
		Asiakas kulunutAika = new Asiakas(1);
		kulunutAika.setSaapumisaika(1000);
		kulunutAika.setPoistumisaika(2000);

		assertEquals((kulunutAika.getPoistumisaika() - kulunutAika.getSaapumisaika()), kulunutAika.getKokonaisAika(),
				"Aika ei täsmännyt");
	}
}
