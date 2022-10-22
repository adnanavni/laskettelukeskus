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
	private HashMap<Integer, Integer> asiakkaidenPp = new HashMap<>();
	private ArrayList<Double> asiakkaidenHinnat = new ArrayList<>();
	private ArrayList<Integer> reitinPituudet = new ArrayList<>();

	private int saapuneetAsiakkaat;
	private int palvellutAsiakkaat;

	private double kokonaisaika, kokonaislapimenoaika, suoritusteho, jononpituusKA, reitinPituudetKA, lapimenoKA,
			asiakkaidenHinnatKA, asiakkaidenVietettyAika;

	private double saapumisaikavali, kassaPalveluaika, kassaHinta, vuokraamoAika, vuokraamoHinta, kahvilaAika,
			kahvilaHinta, ekaRinneAika, tokaRinneAika;

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

		palvelupisteet[Palvelupiste.KASSA].setGenerator(new Normal(kassaPalveluaika, 1));
		palvelupisteet[Palvelupiste.KASSA].setHinta(new Uniform(kassaHinta - 1, kassaHinta + 1));

		palvelupisteet[Palvelupiste.VUOKRAAMO].setGenerator(new Normal(vuokraamoAika, 1));
		palvelupisteet[Palvelupiste.VUOKRAAMO].setHinta(new Uniform(vuokraamoHinta - 10, vuokraamoHinta + 10));

		palvelupisteet[Palvelupiste.KAHVILA].setGenerator(new Normal(kahvilaAika, 1));
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
			kontrolleri.naytaJono(kontrolleri.getKassaLabel(), palvelupisteet[Palvelupiste.KASSA].jononPituus());

			int n = 0;
			if (asiakkaidenPp.get(0) != null) {
				n = asiakkaidenPp.get(0);
			}
			asiakkaidenPp.put(0, n + 1);
			palveluajat.put(Palvelupiste.KASSA, palvelupisteet[Palvelupiste.KASSA].getPalveluaikaSumma());
			asiakkaidenHinnat.add(palvelupisteet[Palvelupiste.KASSA].getEsimHinta());

			palvelupisteet[Palvelupiste.VUOKRAAMO].lisaaJonoon(a);
			break;
		case DEP2:
			a = palvelupisteet[Palvelupiste.VUOKRAAMO].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getVuokraamoLabel(),
					palvelupisteet[Palvelupiste.VUOKRAAMO].jononPituus());

			n = 0;
			if (asiakkaidenPp.get(1) != null) {
				n = asiakkaidenPp.get(1);
			}
			asiakkaidenPp.put(1, n + 1);
			palveluajat.put(Palvelupiste.VUOKRAAMO, palvelupisteet[Palvelupiste.VUOKRAAMO].getPalveluaikaSumma());
			asiakkaidenHinnat.add(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta());

			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP3:
			a = palvelupisteet[Palvelupiste.KAHVILA].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getKahvilaLabel(), palvelupisteet[Palvelupiste.KAHVILA].jononPituus());

			n = 0;
			if (asiakkaidenPp.get(2) != null) {
				n = asiakkaidenPp.get(2);
			}
			asiakkaidenPp.put(2, n + 1);
			palveluajat.put(Palvelupiste.KAHVILA, palvelupisteet[Palvelupiste.KAHVILA].getPalveluaikaSumma());
			asiakkaidenHinnat.add(palvelupisteet[Palvelupiste.VUOKRAAMO].getEsimHinta());

			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP4:
			a = palvelupisteet[Palvelupiste.RINNE1].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getRinne1Label(), palvelupisteet[Palvelupiste.RINNE1].jononPituus());

			n = 0;
			if (asiakkaidenPp.get(3) != null) {
				n = asiakkaidenPp.get(3);
			}
			asiakkaidenPp.put(3, n + 1);
			palveluajat.put(Palvelupiste.RINNE1, palvelupisteet[Palvelupiste.RINNE1].getPalveluaikaSumma());

			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP5:
			a = palvelupisteet[Palvelupiste.RINNE2].otaJonosta();
			kontrolleri.naytaJono(kontrolleri.getRinne2Label(), palvelupisteet[Palvelupiste.RINNE2].jononPituus());

			n = 0;
			if (asiakkaidenPp.get(4) != null) {
				n = asiakkaidenPp.get(4);
			}
			asiakkaidenPp.put(4, n + 1);
			palveluajat.put(Palvelupiste.RINNE2, palvelupisteet[Palvelupiste.RINNE2].getPalveluaikaSumma());

			palvelupisteet[a.seuraava()].lisaaJonoon(a);
			break;
		case DEP6:
			a = palvelupisteet[Palvelupiste.VUOKRAAMOEXIT].otaJonosta();

			palveluajat.put(Palvelupiste.VUOKRAAMOEXIT,
					palvelupisteet[Palvelupiste.VUOKRAAMOEXIT].getPalveluaikaSumma());

			palvellutAsiakkaat++;
			a.setPoistumisaika(Kello.getInstance().getAika());
			kokonaislapimenoaika += a.getKokonaisAika();
			a.raportti();
			tulokset();
			break;

		}
	}

	@Override
	protected void tulokset() {

		kokonaisaika = Kello.getInstance().getAika();

		for (int i = 0; i < 5; i++) {
			kayttoaste.put(i, (palveluajat.get(i) / kokonaisaika * 100));
		}

		for (int i = 0; i < 5; i++) {
			palveluAikaKA.put(i, (palveluajat.get(i) / asiakkaidenPp.get(i)));
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

		dao.setLKkokonaisaika(kokonaisaika);
		dao.setLKasiakkaaidenMaara(saapuneetAsiakkaat);
		dao.setLKPoistuneetAsiakkaat(palvellutAsiakkaat);
		dao.setLKtulot(hinnatSumma);
		dao.setLKlapimenoaikaAVG(lapimenoKA);
		dao.setLKsuoritusteho(suoritusteho);

		dao.setPPtulot(palvelupisteet[Palvelupiste.KASSA].getHintojenSumma());
		dao.setPPpalvellutAsiakkaat(asiakkaidenPp.get(0));
		dao.setPPaktiiviAika(palveluajat.get(0));
		dao.setPPpalveluaikaAVG(palveluAikaKA.get(0));
		dao.setPPkayttoAste(kayttoaste.get(0));

		dao.setVuokraamoTulot(palvelupisteet[Palvelupiste.VUOKRAAMO].getHintojenSumma());
		dao.setVuokraamoPalvellutAsiakkaat(asiakkaidenPp.get(1));
		dao.setVuokraamoAktiiviAika(palveluajat.get(1));
		dao.setVuokraamoPalveluaikaAVG(palveluAikaKA.get(1));
		dao.setVuokraamoKayttoAste(kayttoaste.get(1));

		dao.setKahvilaTulot(palvelupisteet[Palvelupiste.KAHVILA].getHintojenSumma());
		dao.setKahvilaPalvellutAsiakkaat(asiakkaidenPp.get(2));
		dao.setKahvilaAktiiviAika(palveluajat.get(2));
		dao.setKahvilaPalveluaikaAVG(palveluAikaKA.get(2));
		dao.setKahvilaKayttoAste(kayttoaste.get(2));

		dao.setRinne1Tulot(palvelupisteet[Palvelupiste.RINNE1].getHintojenSumma());
		dao.setRinne1PalvellutAsiakkaat(asiakkaidenPp.get(3));
		dao.setRinne1AktiiviAika(palveluajat.get(3));
		dao.setRinne1PalveluaikaAVG(palveluAikaKA.get(3));
		dao.setRinne1KayttoAste(kayttoaste.get(3));

		dao.setRinne2Tulot(palvelupisteet[Palvelupiste.RINNE2].getHintojenSumma());
		dao.setRinne2PalvellutAsiakkaat(asiakkaidenPp.get(4));
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

	public double getkassaHinta() {
		return kassaHinta;
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

	public double getkahvilaHinta() {
		return kahvilaHinta;
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
