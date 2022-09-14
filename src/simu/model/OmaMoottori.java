package simu.model;

import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import eduni.distributions.Uniform;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;

public class OmaMoottori extends Moottori {

	private Saapumisprosessi saapumisprosessi;

	public OmaMoottori() {

		palvelupisteet = new Palvelupiste[5];

		palvelupisteet[0] = new Palvelupiste(new Normal(20, 6), tapahtumalista, TapahtumanTyyppi.DEP1);
		palvelupisteet[1] = new Palvelupiste(new Normal(50, 15), tapahtumalista, TapahtumanTyyppi.DEP2);
		palvelupisteet[2] = new Palvelupiste(new Normal(40, 10), tapahtumalista, TapahtumanTyyppi.DEP3);
		palvelupisteet[3] = new Palvelupiste(new Uniform(30, 35), tapahtumalista, TapahtumanTyyppi.DEP4);
		palvelupisteet[4] = new Palvelupiste(new Uniform(25, 28), tapahtumalista, TapahtumanTyyppi.DEP5);

		saapumisprosessi = new Saapumisprosessi(new Negexp(15, 5), tapahtumalista, TapahtumanTyyppi.ARR1);

	}

	@Override
	protected void alustukset() {
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void suoritaTapahtuma(Tapahtuma t) { // B-vaiheen tapahtumat

		Asiakas a;
		switch (t.getTyyppi()) {

		case ARR1:
			palvelupisteet[0].lisaaJonoon(new Asiakas());
			saapumisprosessi.generoiSeuraava();
			break;
		case DEP1:
			a = palvelupisteet[0].otaJonosta();
			palvelupisteet[1].lisaaJonoon(a);
			break;
		case DEP2:
			a = palvelupisteet[1].otaJonosta();
			palvelupisteet[2].lisaaJonoon(a);
			break;
		case DEP3:
			a = palvelupisteet[2].otaJonosta();
			palvelupisteet[3].lisaaJonoon(a);
			break;
		case DEP4:
			a = palvelupisteet[3].otaJonosta();
			palvelupisteet[4].lisaaJonoon(a);
			break;
		case DEP5:
			a = palvelupisteet[4].otaJonosta();
			a.setPoistumisaika(Kello.getInstance().getAika());
			a.raportti();
		}
	}

	@Override
	protected void tulokset() {
		System.out.println("Simulointi päättyi kello " + Kello.getInstance().getAika());
		System.out.println("Tulokset ... puuttuvat vielä");
	}

}
