package simu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import controller.IKontrolleriMtoV;
import dao.DAO;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import eduni.distributions.Uniform;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;

public class OmaMoottori extends Moottori {

	private Saapumisprosessi saapumisprosessi;

	private int reitinPituus = ThreadLocalRandom.current().nextInt(5, 20);
	private HashMap<Integer, Double> palveluajat = new HashMap<>();
	private HashMap<Integer, Double> kayttoaste = new HashMap<>();
	private HashMap<Integer, Double> palveluAikaKA = new HashMap<>();
	private ArrayList<Double> asiakkaidenHinnat = new ArrayList<>();
	private ArrayList<Integer> reitinPituudet = new ArrayList<>();

	private int saapuneetAsiakkaat;
	private int palvellutAsiakkaat;

	private double kokonaisaika, suoritusteho, saapumisaikavali, jononpituusKA, reitinPituudetKA, lapimenoKA,
			kassaPalveluaika, kassaHinta, vuokraamoAika, vuokraamoHinta, kahvilaAika, kahvilaHinta, ekaRinneAika,
			tokaRinneAika, kokonaislapimenoaika, asiakkaidenHinnatKA, asiakkaidenVietettyAika;

	private int kassaAsiakasCounter = 0;
	private int vuokraamoAsiakasCounter = 0;
	private int kahvilaAsiakasCounter = 0;
	private int rinne1AsiakasCounter = 0;
	private int rinne2AsiakasCounter = 0;

	private DAO dao = new DAO();

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

		saapumisprosessi = new Saapumisprosessi(new Negexp(1, 1), tapahtumalista, TapahtumanTyyppi.ARR1);
	}

	@Override
	protected void alustukset() {
		Kello.getInstance().setAika(0);
		saapumisprosessi.setGeneraattori(new Negexp(saapumisaikavali));

		palvelupisteet[Palvelupiste.KASSA].setGenerator(new Normal(kassaPalveluaika, 5));
		palvelupisteet[Palvelupiste.KASSA].setHinta(new Uniform(kassaHinta - 1, kassaHinta + 1));

		palvelupisteet[Palvelupiste.VUOKRAAMO].setGenerator(new Normal(vuokraamoAika, 10));
		palvelupisteet[Palvelupiste.VUOKRAAMO].setHinta(new Uniform(vuokraamoHinta - 10, vuokraamoHinta + 10));

		palvelupisteet[Palvelupiste.KAHVILA].setGenerator(new Normal(kahvilaAika, 10));
		palvelupisteet[Palvelupiste.KAHVILA].setHinta(new Uniform(kahvilaHinta - 10, kahvilaHinta + 10));

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
			reitinPituudet.add(reitinPituus);
			saapuneetAsiakkaat++;
			saapumisprosessi.generoiSeuraava();
			break;
		case DEP1:
			a = palvelupisteet[Palvelupiste.KASSA].otaJonosta();
			kassaAsiakasCounter++;
			kontrolleri.naytaJono(kontrolleri.getKassaLabel(), palvelupisteet[Palvelupiste.KASSA].jononPituus());

			palveluajat.put(Palvelupiste.KASSA, palvelupisteet[Palvelupiste.KASSA].getPalveluaikaSumma());
			asiakkaidenHinnat.add(palvelupisteet[Palvelupiste.KASSA].getEsimHinta());

			System.out.println(a.getId() + " KASSA " + a);
			palvelupisteet[Palvelupiste.VUOKRAAMO].lisaaJonoon(a);
			break;
		case DEP2:
			a = palvelupisteet[Palvelupiste.VUOKRAAMO].otaJonosta();
			vuokraamoAsiakasCounter++;
			kontrolleri.naytaJono(kontrolleri.getVuokraamoLabel(),
					palvelupisteet[Palvelupiste.VUOKRAAMO].jononPituus());

			palveluajat.put(Palvelupiste.VUOKRAAMO, palvelupisteet[Palvelupiste.VUOKRAAMO].getPalveluaikaSumma());
			asiakkaidenHinnat.add(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta());

			System.out.println(a.getId() + " VUOKRAAMO " + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);

			break;
		case DEP3:
			a = palvelupisteet[Palvelupiste.KAHVILA].otaJonosta();
			kahvilaAsiakasCounter++;
			kontrolleri.naytaJono(kontrolleri.getKahvilaLabel(), palvelupisteet[Palvelupiste.KAHVILA].jononPituus());

			palveluajat.put(Palvelupiste.KAHVILA, palvelupisteet[Palvelupiste.KAHVILA].getPalveluaikaSumma());
			asiakkaidenHinnat.add(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta());

			System.out.println(a.getId() + " KAHVILA " + a);
			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP4:
			a = palvelupisteet[Palvelupiste.RINNE1].otaJonosta();
			rinne1AsiakasCounter++;
			kontrolleri.naytaJono(kontrolleri.getRinne1Label(), palvelupisteet[Palvelupiste.RINNE1].jononPituus());

			palveluajat.put(Palvelupiste.RINNE1, palvelupisteet[Palvelupiste.RINNE1].getPalveluaikaSumma());

			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP5:
			a = palvelupisteet[Palvelupiste.RINNE2].otaJonosta();
			rinne2AsiakasCounter++;
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

			kokonaislapimenoaika += a.getSum();
			a.raportti();
			tulokset();
			break;

		}
	}

	@Override
	protected void tulokset() {
		System.out.println("Simulointi päättyi kello " + Kello.getInstance().getAika());

		kokonaisaika = Kello.getInstance().getAika();

		for (int i = 0; i < 5; i++) {
			kayttoaste.put(i, (palveluajat.get(i) / kokonaisaika * 100));
		}

		for (int i = 0; i < 5; i++) {
			palveluAikaKA.put(i, (palveluajat.get(i) / palvellutAsiakkaat));
		}

		suoritusteho = palvellutAsiakkaat / kokonaisaika;

		lapimenoKA = kokonaislapimenoaika / palvellutAsiakkaat;

		jononpituusKA = kokonaislapimenoaika / kokonaisaika;

		double summa = 0;
		for (int i = 0; i < reitinPituudet.size(); i++) {
			summa += reitinPituudet.get(i);
		}
		reitinPituudetKA = summa / reitinPituudet.size();

		double hinnatSumma = palvelupisteet[Palvelupiste.KASSA].getHintojenSumma()
				+ palvelupisteet[Palvelupiste.VUOKRAAMO].getHintojenSumma()
				+ palvelupisteet[Palvelupiste.KAHVILA].getHintojenSumma();

		double asiakasHinnatSumma = 0;
		for (int i = 0; i < asiakkaidenHinnat.size(); i++) {
			asiakasHinnatSumma += asiakkaidenHinnat.get(i);
		}
		asiakkaidenHinnatKA = asiakasHinnatSumma / asiakkaidenHinnat.size();

		asiakkaidenVietettyAika = kokonaislapimenoaika / palvellutAsiakkaat;

		System.out.println("A: " + saapuneetAsiakkaat);
		System.out.println("B: " + palveluajat);
		System.out.println("C: " + palvellutAsiakkaat);
		System.out.println("U: " + kayttoaste);
		System.out.println("X: " + suoritusteho + " asiakasta/sekunnissa");
		System.out.println("S: " + palveluAikaKA);
		System.out.println("W: " + kokonaislapimenoaika);
		System.out.println("R: " + lapimenoKA);
		System.out.println("N: " + jononpituusKA);
		System.out.println("Hinnat Kassa: " + palvelupisteet[Palvelupiste.KASSA].getHintojenSumma());
		System.out.println("Hinnat Vuokraamo: " + palvelupisteet[Palvelupiste.VUOKRAAMO].getHintojenSumma());
		System.out.println("Hinnat Kahvila: " + palvelupisteet[Palvelupiste.KAHVILA].getHintojenSumma());
		System.out.println("Asiakkaiden hinnat: " + asiakkaidenHinnat);

		dao.setLKkokonaisaika(kokonaisaika);
		dao.setLKasiakkaaidenMaara(saapuneetAsiakkaat);
		dao.setLKPoistuneetAsiakkaat(palvellutAsiakkaat);
		dao.setLKtulot(hinnatSumma);
		dao.setLKlapimenoaikaAVG(lapimenoKA);
		dao.setLKsuoritusteho(suoritusteho);

		dao.setPPtulot(palvelupisteet[Palvelupiste.KASSA].getHintojenSumma());
		dao.setPPpalvellutAsiakkaat(kassaAsiakasCounter);
		dao.setPPaktiiviAika(palveluajat.get(0));
		dao.setPPpalveluaikaAVG(palveluAikaKA.get(0));
		dao.setPPkayttoAste(kayttoaste.get(0));

		dao.setVuokraamoTulot(palvelupisteet[Palvelupiste.VUOKRAAMO].getHintojenSumma());
		dao.setVuokraamoPalvellutAsiakkaat(vuokraamoAsiakasCounter);
		dao.setVuokraamoAktiiviAika(palveluajat.get(1));
		dao.setVuokraamoPalveluaikaAVG(palveluAikaKA.get(1));
		dao.setVuokraamoKayttoAste(kayttoaste.get(1));

		dao.setKahvilaTulot(palvelupisteet[Palvelupiste.KAHVILA].getHintojenSumma());
		dao.setKahvilaPalvellutAsiakkaat(kahvilaAsiakasCounter);
		dao.setKahvilaAktiiviAika(palveluajat.get(2));
		dao.setKahvilaPalveluaikaAVG(palveluAikaKA.get(2));
		dao.setKahvilaKayttoAste(kayttoaste.get(2));

		dao.setRinne1Tulot(palvelupisteet[Palvelupiste.RINNE1].getHintojenSumma());
		dao.setRinne1PalvellutAsiakkaat(rinne1AsiakasCounter);
		dao.setRinne1AktiiviAika(palveluajat.get(3));
		dao.setRinne1PalveluaikaAVG(palveluAikaKA.get(3));
		dao.setRinne1KayttoAste(kayttoaste.get(3));

		dao.setRinne2Tulot(palvelupisteet[Palvelupiste.RINNE2].getHintojenSumma());
		dao.setRinne2PalvellutAsiakkaat(rinne2AsiakasCounter);
		dao.setRinne2AktiiviAika(palveluajat.get(4));
		dao.setRinne2PalveluaikaAVG(palveluAikaKA.get(4));
		dao.setRinne2KayttoAste(kayttoaste.get(4));

		dao.setApalvelupisteidenMaara(reitinPituudetKA);
		dao.setArahaaKaytetty(asiakkaidenHinnatKA);
		dao.setAvietettyAika(asiakkaidenVietettyAika);

		dao.setSaapumisvali(saapumisaikavali);
		dao.setKassaPalveluAjanKA(kassaPalveluaika);
		dao.setLipunHinta(kassaHinta);
		dao.setVuokraamoPalveluAjanKA(vuokraamoAika);
		dao.setVuokraamoOstostenKA(vuokraamoHinta);
		dao.setKahvilaPalveluAjanKA(kahvilaAika);
		dao.setKahvilaOstostenKA(kahvilaHinta);
		dao.setRinne1PalveluAjanKA(ekaRinneAika);
		dao.setRinne2PalveluAjanKA(tokaRinneAika);

	}

	@Override
	public void setSaapumisvaliKA(double aika) {
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

	public double getAika() {
		return kokonaisaika;
	}

}
