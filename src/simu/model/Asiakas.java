package simu.model;

import java.util.LinkedList;

import eduni.distributions.Normal;
import simu.framework.Kello;
import simu.framework.Trace;

/**
 * Luokka sisältää yksittäisen asiakkaan asioimiseen liittyvät tiedot. Asiakas
 * liikkuu simulaattorissa palvelupisteeltä seuraavalle.
 * 
 * @author Adnan Avni
 * @version 1.0
 */

public class Asiakas {

	private double saapumisaika;
	private double poistumisaika;
	private int id;
	private static int i = 1;
	private static long sum = 0;

	/**
	 * Asiakaskohtainen reitti tallennetaan tähän listaksi.
	 */
	LinkedList<Integer> reitti = new LinkedList<>();

	/**
	 * Jakauma, jonka mukaan reitti listaan asetetaan numeroita. Rinne 1 on
	 * suosituin ja jakauma on tehty sen mukaisesti.
	 */
	Normal jakauma = new Normal(3, 1);

	/**
	 * Asiakkaan konstruktori, joka ottaa parametrina kuinka pitkä reitti sille
	 * tehdään.
	 * 
	 * @param reitinpituus määrittää reitti listan pituuden, eli monessako
	 *                     palvelupisteessä kyseinen asiakas tulee asioimaan.
	 */
	public Asiakas(int reitinpituus) {
		id = i++;

		for (int i = 0; i < reitinpituus; i++) {
			int jakaumaLuku = (int) jakauma.sample();

			if (jakaumaLuku > 4 || jakaumaLuku < 2) {
				jakaumaLuku = 4;
			}

			reitti.add(jakaumaLuku);

			// Ei haluta kahta kahvilaa peräkkäin(epärealistinen)
			if (reitti.size() > 1 && reitti.get(i) == 2 && reitti.get(i - 1) == 2) {
				reitti.set(i, 3);
			}

		}
		reitti.add(5);

		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas:" + id + ":" + saapumisaika);
	}

	/**
	 * @return palauttaa reitistä seuraavan palvelupisteen ja poistaa sen samalla,
	 *         jotta reitti jatkuvasti lyhenee
	 */
	public int seuraava() {
		return reitti.poll();
	}

	public double getPoistumisaika() {
		return poistumisaika;
	}

	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	public double getSaapumisaika() {
		return saapumisaika;
	}

	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}

	public int getId() {
		return id;
	}

	public long getKokonaisAika() {
		return (long) (poistumisaika - saapumisaika);
	}

	/**
	 * Asiakaskohtainen raportti, jossa tulostetaan konsoliin asiakaskohtaisia
	 * tietoja.
	 */
	public void raportti() {
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " saapui:" + saapumisaika);
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " poistui:" + poistumisaika);
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " viipyi:" + (poistumisaika - saapumisaika));
		sum += (poistumisaika - saapumisaika);
		double keskiarvo = sum / id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo " + keskiarvo);
	}
}
