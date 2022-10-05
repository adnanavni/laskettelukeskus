package simu.model;

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

public class OmaMoottori extends Moottori {

	private Saapumisprosessi saapumisprosessi;
	private int reitinPituus = ThreadLocalRandom.current().nextInt(5, 25);
	private int saapuneetAsiakkaat;
	private int palvellutAsiakkaat;
	private HashMap<Integer, Double> palveluajat = new HashMap<>();
	private HashMap<Integer, Double> kayttoaste = new HashMap<>();
	private double kokonaisaika;
	private double suoritusteho;
	private double hinta = 0;
	private HashMap<Integer, Double> palveluAikaKA = new HashMap<>();
	private HashMap<Integer, Double> asiakkaidenHinnat = new HashMap<>();
	private double saapumisaikavali;

	public OmaMoottori(IKontrolleriMtoV kontrolleri) { // UUSI

		super(kontrolleri); // UUSI

		palvelupisteet = new Palvelupiste[6];

		palvelupisteet[Palvelupiste.KASSA] = new Palvelupiste(new Normal(20, 6), tapahtumalista, TapahtumanTyyppi.DEP1,
				new Uniform(10, 20));
		palvelupisteet[Palvelupiste.VUOKRAAMO] = new Palvelupiste(new Normal(50, 15), tapahtumalista,
				TapahtumanTyyppi.DEP2, new Normal(50, 5));
		palvelupisteet[Palvelupiste.KAHVILA] = new Palvelupiste(new Normal(40, 10), tapahtumalista,
				TapahtumanTyyppi.DEP3, new Normal(20, 5));
		palvelupisteet[Palvelupiste.RINNE1] = new Palvelupiste(new Uniform(30, 35), tapahtumalista,
				TapahtumanTyyppi.DEP4);
		palvelupisteet[Palvelupiste.RINNE2] = new Palvelupiste(new Uniform(25, 28), tapahtumalista,
				TapahtumanTyyppi.DEP5);
		palvelupisteet[Palvelupiste.VUOKRAAMOEXIT] = new Palvelupiste(new Uniform(25, 28), tapahtumalista,
				TapahtumanTyyppi.DEP6);

		saapumisprosessi = new Saapumisprosessi(new Negexp(10), tapahtumalista, TapahtumanTyyppi.ARR1);

	}

	@Override
	protected void alustukset() {
		saapumisprosessi.setGeneraattori(new Negexp(saapumisaikavali));
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
			asiakkaidenHinnat.put(a.getId(), a.maksa(palvelupisteet[Palvelupiste.KASSA].getEsimHinta()));

			System.out.println(a.getId() + " KASSA " + a);
			palvelupisteet[Palvelupiste.VUOKRAAMO].lisaaJonoon(a);
			break;
		case DEP2:
			a = palvelupisteet[Palvelupiste.VUOKRAAMO].otaJonosta();

			palveluajat.put(Palvelupiste.VUOKRAAMO, palvelupisteet[Palvelupiste.VUOKRAAMO].getPalveluaikaSumma());
			asiakkaidenHinnat.put(a.getId(), a.maksa(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta() + hinta));

			System.out.println(a.getId() + " VUOKRAAMO " + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP3:
			a = palvelupisteet[Palvelupiste.KAHVILA].otaJonosta();

			palveluajat.put(Palvelupiste.KAHVILA, palvelupisteet[Palvelupiste.KAHVILA].getPalveluaikaSumma());
			asiakkaidenHinnat.put(a.getId(), a.maksa(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta() + hinta));

			System.out.println(a.getId() + " KAHVILA " + a);
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

			System.out.println(a.getId() + " RINNE 2 " + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP6:
			a = palvelupisteet[Palvelupiste.VUOKRAAMOEXIT].otaJonosta();

			palveluajat.put(Palvelupiste.VUOKRAAMOEXIT,
					palvelupisteet[Palvelupiste.VUOKRAAMOEXIT].getPalveluaikaSumma());

			System.out.println(a.getId() + " LOPPU " + a);
			palvellutAsiakkaat++;
			a.setPoistumisaika(Kello.getInstance().getAika());
			a.raportti();
			tulokset();
			break;

		}
	}

	@Override
	protected void tulokset() {

		// VANHAA tekstipohjaista
		System.out.println("Simulointi päättyi kello " + Kello.getInstance().getAika());

		kokonaisaika = Kello.getInstance().getAika();

		for (int i = 0; i < 5; i++) {
			kayttoaste.put(i, (palveluajat.get(i) / kokonaisaika * 100));
		}

		suoritusteho = palvellutAsiakkaat / kokonaisaika;

		for (int i = 0; i < 5; i++) {
			palveluAikaKA.put(i, (palveluajat.get(i) / palvellutAsiakkaat));
		}

		System.out.println("A: " + saapuneetAsiakkaat);
		System.out.println("B: " + palveluajat);
		System.out.println("C: " + palvellutAsiakkaat);
		System.out.println("U: " + kayttoaste);
		System.out.println("X: " + suoritusteho + " asiakasta/sekunnissa");
		System.out.println("S: " + palveluAikaKA);
		System.out.println("Hinnat Kassa: " + palvelupisteet[Palvelupiste.KASSA].getHintojenSumma());
		System.out.println("Hinnat Vuokraamo: " + palvelupisteet[Palvelupiste.VUOKRAAMO].getHintojenSumma());
		System.out.println("Hinnat Kahvila: " + palvelupisteet[Palvelupiste.KAHVILA].getHintojenSumma());
		System.out.println("Asiakkaiden hinnat: " + asiakkaidenHinnat);

		kontrolleri.naytaLoppuaika(Kello.getInstance().getAika());
	}

	@Override
	public void setSaapumisvaliKA(double aika) {
		// kesken, pitäisi keksiä miten laskea KA!
		this.saapumisaikavali = aika;
	}
}
