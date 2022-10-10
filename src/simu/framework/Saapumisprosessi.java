package simu.framework;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.DiscreteGenerator;
import eduni.distributions.Poisson;
import simu.model.TapahtumanTyyppi;

public class Saapumisprosessi {

	private DiscreteGenerator generaattori;
	private Tapahtumalista tapahtumalista;
	private TapahtumanTyyppi tyyppi;

	public Saapumisprosessi(DiscreteGenerator g, Tapahtumalista tl, TapahtumanTyyppi tyyppi) {
		this.generaattori = g;
		this.tapahtumalista = tl;
		this.tyyppi = tyyppi;
	}

	public void setGeneraattori(DiscreteGenerator generaattori) {
		this.generaattori = generaattori;
	}

	public void generoiSeuraava() {
		Tapahtuma t = new Tapahtuma(tyyppi, Kello.getInstance().getAika() + generaattori.sample());
		tapahtumalista.lisaa(t);
	}

}
