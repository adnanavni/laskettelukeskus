package simu.model;

import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class Palvelupiste {

	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>(); // Tietorakennetoteutus
	private ContinuousGenerator generator;
	private ContinuousGenerator hinta;
	double esimHinta;
	private double hintojenSumma;
	private Tapahtumalista tapahtumalista;
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;
	private double palveluaikaSumma;
	protected static final int KASSA = 0, VUOKRAAMO = 1, KAHVILA = 2, RINNE1 = 3, RINNE2 = 4, VUOKRAAMOEXIT = 5;

	// JonoStartegia strategia; //optio: asiakkaiden järjestys

	private boolean varattu = false;

	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;

	}

	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi,
			ContinuousGenerator hinta) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.hinta = hinta;

	}

	public void lisaaJonoon(Asiakas a) { // Jonon 1. asiakas aina palvelussa
		jono.add(a);

	}

	public Asiakas otaJonosta() { // Poistetaan palvelussa ollut
		varattu = false;
		return jono.poll();
	}

	public void aloitaPalvelu() { // Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		varattu = true;
		double palveluaika = generator.sample();
		palveluaikaSumma += palveluaika;

		if (hinta != null) {
			esimHinta = hinta.sample();
			hintojenSumma += esimHinta;
		}

		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
	}

	public int jononPituus() {
		return jono.size();
	}

	public double getPalveluaikaSumma() {
		return palveluaikaSumma;
	}

	public boolean onVarattu() {
		return varattu;
	}

	public boolean onJonossa() {
		return jono.size() != 0;
	}

	public double getHintojenSumma() {
		return hintojenSumma;
	}
	
	public double getEsimHinta() {
		return esimHinta;
	}
}
