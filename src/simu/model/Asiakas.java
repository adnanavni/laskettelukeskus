package simu.model;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

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
	
	public Asiakas(int reitinpituus){
	    id = i++;
	    
	    for (int i = 0; i < reitinpituus; i++) {
	    	reitti.add(ThreadLocalRandom.current().nextInt(2, 5));
	    	
	    }
	    reitti.add(5);
	    
		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas:" + id + ":"+saapumisaika);
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
	


	public void raportti(){
		Trace.out(Trace.Level.INFO, "Asiakas "+id+ " saapui:" +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " poistui:" +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " viipyi:" +(poistumisaika-saapumisaika));
		sum += (poistumisaika-saapumisaika);
		double keskiarvo = sum/id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo "+ keskiarvo);
	}
	
	@Override
	public String toString() {
		return "Asiakas [saapumisaika=" + saapumisaika + ", poistumisaika=" + poistumisaika + ", id=" + id + ", reitti="
				+ reitti + "]";
	}

}
