package simu.model;

import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;

/**
 * Sisältää palvelupisteiden toiminnallisuuden. Palvelupiste huolehtii
 * asiakkaiden lisäämisestä jonoon, jonosta poistamisen ja palvelunaloittamisen.
 * 
 * @author Adnan Avni, Perttu Vaarala
 * @version 1.0
 */

public class Palvelupiste {

	/**
	 * Lista, joka toimii jonona FIFO periaatteella. Listaan laitetaan
	 * asiakas-olioita.
	 */
	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();

	private ContinuousGenerator generator;

	/**
	 * Jakauma, joka määrittää hinnan
	 */
	private ContinuousGenerator hinta;

	/**
	 * Hinta jakaumasta saatu esimerkkihinta tallennetaan tähän muuttujaan.
	 */
	private double esimHinta;

	/**
	 * Käytetään tietokantoja varten
	 */
	private double hintojenSumma, palveluaikaSumma;

	private Tapahtumalista tapahtumalista;
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;

	/**
	 * Tehty helpottakseen palvelupistelistan numerointia omassa moottorissa.
	 */
	protected static final int KASSA = 0, VUOKRAAMO = 1, KAHVILA = 2, RINNE1 = 3, RINNE2 = 4, VUOKRAAMOEXIT = 5;

	private boolean varattu = false;

	/**
	 * Palvelupisteen konstrukotri, jonka avulla palvelupiste luodaan omassa
	 * moottorissa.
	 * 
	 * @param generator      jakauma palveluaikaa varten
	 * @param tapahtumalista lista tapahtumista
	 * @param tyyppi         tapahtumantyyppi, kertoo mikä tapahtumista on kyseessä
	 *                       tietyllä palvelupisteellä
	 */
	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;

	}

	/**
	 * Samanlainen konstrukoti kuin aikasemmassa, mutta tätä käytetään sellaisissa
	 * tilanteissa missä tarvitaan hinta ominaisuutta.
	 * 
	 * @param generator      jakauma palveluaikaa varten
	 * @param tapahtumalista lista tapahtumista
	 * @param tyyppi         tapahtumantyyppi, kertoo mikä tapahtumista on kyseessä
	 *                       tietyllä palvelupisteellä
	 * @param hinta          jakauma hintaa varten
	 */
	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi,
			ContinuousGenerator hinta) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.hinta = hinta;

	}

	/**
	 * Lisää asiakkaan palvelupisteen jonoon
	 * 
	 * @param a asiakas joka lisätään jonoon
	 */
	public void lisaaJonoon(Asiakas a) { // Jonon 1. asiakas aina palvelussa
		jono.add(a);

	}

	/**
	 * Ottaa ensimmäisen asiakkaan jonosta palvelupisteelle palveltavaksi
	 * 
	 * @return poistaa ensimmäisen asiakkaan jonosta
	 */
	public Asiakas otaJonosta() { // Poistetaan palvelussa ollut
		varattu = false;
		return jono.poll();
	}

	/**
	 * Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
	 */
	public void aloitaPalvelu() {
		varattu = true;
		double palveluaika = generator.sample();
		palveluaikaSumma += palveluaika;

		if (hinta != null) {
			esimHinta = hinta.sample();
			hintojenSumma += esimHinta;
		}

		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
	}

	/**
	 * Metodi on visualisointia varten.
	 * 
	 * @return jonon pituuden
	 */
	public int jononPituus() {
		return jono.size();
	}

	public boolean onVarattu() {
		return varattu;
	}

	public boolean onJonossa() {
		return jono.size() != 0;
	}

	public double getPalveluaikaSumma() {
		return palveluaikaSumma;
	}

	public double getHintojenSumma() {
		return hintojenSumma;
	}

	public double getEsimHinta() {
		return esimHinta;
	}

	public void setGenerator(ContinuousGenerator generator) {
		this.generator = generator;
	}

	public void setHinta(ContinuousGenerator hinta) {
		this.hinta = hinta;
	}

	public ContinuousGenerator getGenerator() {
		return generator;
	}

	public ContinuousGenerator getHinta() {
		return hinta;
	}
}
