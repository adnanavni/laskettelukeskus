package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import simu.model.OmaMoottori;

class OmaMoottoriTesti {

	OmaMoottori m = new OmaMoottori(null);

	@Test
	@Order(1)
	@DisplayName("Toimiiko hinnan kassaan asetus")
	public void kassaHintaTest() {
		m.kassaHinta(100);
		assertEquals(100, m.getkassaHinta(), "Hinta väärin");
	}

	@Test
	@Order(2)
	@DisplayName("Toimiiko hinnan kahvilaan asetus")
	public void kahvilaHintaTest() {
		m.kahvilaHinta(50);
		assertEquals(50, m.getkahvilaHinta(), "Hinta väärin");
	}

}
