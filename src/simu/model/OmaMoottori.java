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
	private double kassaPalveluaika;
	private double kassaHinta;
	private double vuokraamoAika;
	private double vuokraamoHinta;
	private double kahvilaAika;
	private double kahvilaHinta;
	private double ekaRinneAika;
	private double tokaRinneAika;

	public OmaMoottori(IKontrolleriMtoV kontrolleri) { // UUSI

		super(kontrolleri); // UUSI

		palvelupisteet = new Palvelupiste[6];

		palvelupisteet[Palvelupiste.KASSA] = new Palvelupiste(new Normal(1, 2), tapahtumalista, TapahtumanTyyppi.DEP1,
				new Uniform(1, 2));
		palvelupisteet[Palvelupiste.VUOKRAAMO] = new Palvelupiste(new Normal(1, 2), tapahtumalista,
				TapahtumanTyyppi.DEP2, new Uniform(1, 2));
		palvelupisteet[Palvelupiste.KAHVILA] = new Palvelupiste(new Normal(1, 2), tapahtumalista, TapahtumanTyyppi.DEP3,
				new Normal(1, 2));
		palvelupisteet[Palvelupiste.RINNE1] = new Palvelupiste(new Uniform(1, 2), tapahtumalista,
				TapahtumanTyyppi.DEP4);
		palvelupisteet[Palvelupiste.RINNE2] = new Palvelupiste(new Uniform(1, 2), tapahtumalista,
				TapahtumanTyyppi.DEP5);
		palvelupisteet[Palvelupiste.VUOKRAAMOEXIT] = new Palvelupiste(new Uniform(1, 2), tapahtumalista,
				TapahtumanTyyppi.DEP6);

		saapumisprosessi = new Saapumisprosessi(new Negexp(15, 5), tapahtumalista, TapahtumanTyyppi.ARR1);
	}

	@Override
	protected void alustukset() {
		saapumisprosessi.setGeneraattori(new Negexp(saapumisaikavali));

		palvelupisteet[Palvelupiste.KASSA].setGenerator(new Normal(kassaPalveluaika, 5));
		palvelupisteet[Palvelupiste.KASSA].setHinta(new Uniform(kassaHinta, kassaHinta + 1));

		palvelupisteet[Palvelupiste.VUOKRAAMO].setGenerator(new Normal(vuokraamoAika, 10));
		palvelupisteet[Palvelupiste.VUOKRAAMO].setHinta(new Uniform(vuokraamoHinta, vuokraamoHinta + 10));

		palvelupisteet[Palvelupiste.KAHVILA].setGenerator(new Normal(kahvilaAika, 10));
		palvelupisteet[Palvelupiste.KAHVILA].setHinta(new Uniform(kahvilaHinta, kahvilaHinta + 10));

		palvelupisteet[Palvelupiste.RINNE1].setGenerator(new Uniform(ekaRinneAika, ekaRinneAika + 1));
		palvelupisteet[Palvelupiste.RINNE2].setGenerator(new Uniform(tokaRinneAika, tokaRinneAika + 1));

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
			kontrolleri.naytaJono(kontrolleri.getKassaLabel(), palvelupisteet[Palvelupiste.KASSA].jononPituus());
			palveluajat.put(Palvelupiste.KASSA, palvelupisteet[Palvelupiste.KASSA].getPalveluaikaSumma());
			asiakkaidenHinnat.put(a.getId(), a.maksa(palvelupisteet[Palvelupiste.KASSA].getEsimHinta()));

			System.out.println(a.getId() + " KASSA " + a);
			palvelupisteet[Palvelupiste.VUOKRAAMO].lisaaJonoon(a);
			break;
		case DEP2:
			a = palvelupisteet[Palvelupiste.VUOKRAAMO].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getVuokraamoLabel(),
					palvelupisteet[Palvelupiste.VUOKRAAMO].jononPituus());
			palveluajat.put(Palvelupiste.VUOKRAAMO, palvelupisteet[Palvelupiste.VUOKRAAMO].getPalveluaikaSumma());
			asiakkaidenHinnat.put(a.getId(), a.maksa(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta() + hinta));

			System.out.println(a.getId() + " VUOKRAAMO " + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);

			break;
		case DEP3:
			a = palvelupisteet[Palvelupiste.KAHVILA].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getKahvilaLabel(), palvelupisteet[Palvelupiste.KAHVILA].jononPituus());
			palveluajat.put(Palvelupiste.KAHVILA, palvelupisteet[Palvelupiste.KAHVILA].getPalveluaikaSumma());
			asiakkaidenHinnat.put(a.getId(), a.maksa(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta() + hinta));

			System.out.println(a.getId() + " KAHVILA " + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP4:
			a = palvelupisteet[Palvelupiste.RINNE1].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getRinne1Label(), palvelupisteet[Palvelupiste.RINNE1].jononPituus());
			palveluajat.put(Palvelupiste.RINNE1, palvelupisteet[Palvelupiste.RINNE1].getPalveluaikaSumma());

			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP5:
			a = palvelupisteet[Palvelupiste.RINNE2].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getRinne2Label(), palvelupisteet[Palvelupiste.RINNE2].jononPituus());
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

	public void kassaSaapumisaika(double aika) {
		kassaPalveluaika = aika;
	}

	public void kassaHinta(double raha) {
		kassaHinta = raha;
	}

	public void vuokraamoPalveluaika(double aika) {
		vuokraamoAika = aika;
	}

	public void vuokraamoHinta(double raha) {
		vuokraamoHinta = raha;
	}

	public void kahvilaPalveluaika(double aika) {
		kahvilaAika = aika;

	}

	public void kahvilaHinta(double raha) {
		kahvilaHinta = raha;
	}

	public void ekaRinnePalveluaika(double aika) {
		ekaRinneAika = aika;
	}

	public void TokaRinnePalveluaika(double aika) {
		tokaRinneAika = aika;
	}

}
