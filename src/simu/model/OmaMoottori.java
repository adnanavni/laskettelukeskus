package simu.model;

import java.util.concurrent.ThreadLocalRandom;

import controller.IKontrolleriMtoV;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import eduni.distributions.Uniform;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;
import simu.framework.Trace;

public class OmaMoottori extends Moottori {

	private Saapumisprosessi saapumisprosessi;

	public OmaMoottori(IKontrolleriMtoV kontrolleri) { // UUSI

		super(kontrolleri); // UUSI

		palvelupisteet = new Palvelupiste[6];

		palvelupisteet[Palvelupiste.KASSA] = new Palvelupiste(new Normal(20, 6), tapahtumalista, TapahtumanTyyppi.DEP1);
		palvelupisteet[Palvelupiste.VUOKRAAMO] = new Palvelupiste(new Normal(50, 15), tapahtumalista,
				TapahtumanTyyppi.DEP2);
		palvelupisteet[Palvelupiste.KAHVILA] = new Palvelupiste(new Normal(40, 10), tapahtumalista,
				TapahtumanTyyppi.DEP3);
		palvelupisteet[Palvelupiste.RINNE1] = new Palvelupiste(new Uniform(30, 35), tapahtumalista,
				TapahtumanTyyppi.DEP4);
		palvelupisteet[Palvelupiste.RINNE2] = new Palvelupiste(new Uniform(25, 28), tapahtumalista,
				TapahtumanTyyppi.DEP5);
		palvelupisteet[Palvelupiste.VUOKRAAMOEXIT] = new Palvelupiste(new Uniform(25, 28), tapahtumalista,
				TapahtumanTyyppi.DEP6);

		saapumisprosessi = new Saapumisprosessi(new Negexp(10, 5), tapahtumalista, TapahtumanTyyppi.ARR1);

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
			palvelupisteet[Palvelupiste.KASSA].lisaaJonoon(new Asiakas(ThreadLocalRandom.current().nextInt(5, 10)));
			saapumisprosessi.generoiSeuraava();
			kontrolleri.visualisoiAsiakas();
			break;
		case DEP1:
			a = palvelupisteet[Palvelupiste.KASSA].otaJonosta();
			System.out.println(a.getId() + " KASSA" + a);
			palvelupisteet[Palvelupiste.VUOKRAAMO].lisaaJonoon(a);
			break;
		case DEP2:
			a = palvelupisteet[Palvelupiste.VUOKRAAMO].otaJonosta();
			System.out.println(a.getId() + " VUOKRAAMO" + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP3:
			a = palvelupisteet[Palvelupiste.KAHVILA].otaJonosta();
			System.out.println(a.getId() + " KAHVILA" + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP4:
			a = palvelupisteet[Palvelupiste.RINNE1].otaJonosta();
			System.out.println(a.getId() + " RINNE 1" + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP5:
			a = palvelupisteet[Palvelupiste.RINNE2].otaJonosta();
			System.out.println(a.getId() + " RINNE 2" + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP6:
			a = palvelupisteet[Palvelupiste.VUOKRAAMOEXIT].otaJonosta();
			System.out.println(a.getId() + " LOPPU" + a);
			a.setPoistumisaika(Kello.getInstance().getAika());
			a.raportti();
			break;
		}
	}

	@Override
	protected void tulokset() {

		// VANHAA tekstipohjaista
		// System.out.println("Simulointi päättyi kello " +
		// Kello.getInstance().getAika());
		// System.out.println("Tulokset ... puuttuvat vielä");

		// UUTTA graafista
		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());

	}

}
