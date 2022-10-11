package simu.framework;

public interface IMoottori { // UUSI

	// Kontrolleri käyttää tätä rajapintaa

	public void setSimulointiaika(double aika);

	public void setViive(long aika);

	public long getViive();

	public void setSaapumisvaliKA(double aika);

	public void kassaSaapumisaika(double aika);

	public void kassaHinta(double raha);

	public void vuokraamoPalveluaika(double aika);

	public void vuokraamoHinta(double raha);

	public void kahvilaPalveluaika(double aika);

	public void kahvilaHinta(double raha);

	public void ekaRinnePalveluaika(double aika);

	public void TokaRinnePalveluaika(double aika);

	public double getAika();
}
