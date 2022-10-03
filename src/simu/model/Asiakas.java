package simu.model;

import java.util.LinkedList;

import eduni.distributions.Normal;
import simu.framework.Kello;
import simu.framework.Trace;

// TODO:
// Asiakas koodataan simulointimallin edellyttämällä tavalla (data!)
public class Asiakas {
	private double saapumisaika;
	private double poistumisaika;
	private int id;
	private static int i = 1;
	private static long sum = 0;
	LinkedList<Integer> reitti = new LinkedList<>();
	private double maksu;
	// rinne 1 on suosituin, joten sen mukainen jakauma
	Normal jakauma = new Normal(3.5, 0.75);

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

	public void raportti() {
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " saapui:" + saapumisaika);
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " poistui:" + poistumisaika);
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " viipyi:" + (poistumisaika - saapumisaika));
		sum += (poistumisaika - saapumisaika);
		double keskiarvo = sum / id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo " + keskiarvo);
	}

	public long getSum() {
		return (long) (poistumisaika - saapumisaika);
	}

	public double maksa(double raha) {
		maksu += raha;
		if (maksu > 100) {
			maksu -= 20;
		}
		return maksu;
	}

	@Override
	public String toString() {
		return "Asiakas [saapumisaika=" + saapumisaika + ", poistumisaika=" + poistumisaika + ", id=" + id + ", reitti="
				+ reitti + "]";
	}
}
