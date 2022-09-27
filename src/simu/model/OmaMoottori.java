package simu.model;

import java.util.ArrayList;
import java.util.HashMap;
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
	private int reitinPituus = ThreadLocalRandom.current().nextInt(5, 25);
	private int saapuneetAsiakkaat = 0;
	private int palvellutAsiakkaat = 0;
	private HashMap<Integer,Double> palveluajat = new HashMap<>();

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
			palvelupisteet[Palvelupiste.KASSA].lisaaJonoon(new Asiakas(reitinPituus));
			saapuneetAsiakkaat++;
			saapumisprosessi.generoiSeuraava();
			kontrolleri.visualisoiAsiakas();
			break;
		case DEP1:
			a = palvelupisteet[Palvelupiste.KASSA].otaJonosta();
			palveluajat.put(Palvelupiste.KASSA, palvelupisteet[Palvelupiste.KASSA].getPalveluaikaSumma());
			System.out.println(a.getId() + " KASSA" + a);
			palvelupisteet[Palvelupiste.VUOKRAAMO].lisaaJonoon(a);
			break;
		case DEP2:
			a = palvelupisteet[Palvelupiste.VUOKRAAMO].otaJonosta();
			palveluajat.put(Palvelupiste.VUOKRAAMO, palvelupisteet[Palvelupiste.VUOKRAAMO].getPalveluaikaSumma());
			System.out.println(a.getId() + " VUOKRAAMO" + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP3:
			a = palvelupisteet[Palvelupiste.KAHVILA].otaJonosta();
			palveluajat.put(Palvelupiste.KAHVILA, palvelupisteet[Palvelupiste.KAHVILA].getPalveluaikaSumma());
			System.out.println(a.getId() + " KAHVILA" + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP4:
			a = palvelupisteet[Palvelupiste.RINNE1].otaJonosta();
			palveluajat.put(Palvelupiste.RINNE1, palvelupisteet[Palvelupiste.RINNE1].getPalveluaikaSumma());
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP5:
			a = palvelupisteet[Palvelupiste.RINNE2].otaJonosta();
			palveluajat.put(Palvelupiste.RINNE2, palvelupisteet[Palvelupiste.RINNE2].getPalveluaikaSumma());
			System.out.println(a.getId() + " RINNE 2" + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP6:
			a = palvelupisteet[Palvelupiste.VUOKRAAMOEXIT].otaJonosta();
			palveluajat.put(Palvelupiste.VUOKRAAMOEXIT, palvelupisteet[Palvelupiste.VUOKRAAMOEXIT].getPalveluaikaSumma());
			palvellutAsiakkaat++;
			System.out.println(a.getId() + " LOPPU" + a);
			a.setPoistumisaika(Kello.getInstance().getAika());
			a.raportti();
			tulokset();
			break;
		}
	}

	@Override
	protected void tulokset() {

		// VANHAA tekstipohjaista
		System.out.println("Simulointi päättyi kello " +
		Kello.getInstance().getAika());
		//System.out.println("Tulokset ... puuttuvat vielä");
		System.out.println("A: " + saapuneetAsiakkaat);
		System.out.println("B: " + palveluajat);
		System.out.println("C: " + palvellutAsiakkaat);
		

		// UUTTA graafista
		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());

	}

}
