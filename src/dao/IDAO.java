package dao;

import java.util.ArrayList;

public interface IDAO {

	public void tallennaLKData();

	public void tyhjennaTietokanta();
	
	public ArrayList<Double> getLKtaulu();
	
	public ArrayList<Integer> IDpituus();
	

}
