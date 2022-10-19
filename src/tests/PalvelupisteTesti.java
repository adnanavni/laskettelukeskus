package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import eduni.distributions.Uniform;
import simu.model.Asiakas;
import simu.model.Palvelupiste;

class PalvelupisteTesti {

	@Test
	@DisplayName("Kokeilee palvelupisteen jonoa")
	void JonoTesti() {
		Asiakas a = new Asiakas(1);
		Palvelupiste p = new Palvelupiste(null, null, null);

		p.lisaaJonoon(a);
		assertEquals(a, p.otaJonosta(), "Jono ei palauttanut oikein");
	}

	@Test
	@DisplayName("Onko palvelupiste varattu")
	void VarattuTesti() {
		Asiakas a = new Asiakas(1);
		Palvelupiste p = new Palvelupiste(null, null, null);

		p.lisaaJonoon(a);
		assertFalse(p.onVarattu(), "Palvelupiste on varattu");

	}

	@Test
	@DisplayName("Onko palvelupiste varattu")
	void HintaTesti() {
		Palvelupiste p = new Palvelupiste(null, null, null, new Uniform(1, 2));

		assertNotNull(p.getHinta());
		assertNull(p.getGenerator());
	}

}
