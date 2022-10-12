package dao;

import java.util.ArrayList;

public interface IDAO {

	public void tallennaLKData();

	public void tyhjennaTietokanta();

	public ArrayList<Integer> IDpituus();

	public ArrayList<Double> haeLKData(int simuloinninID);

	public ArrayList<Double> haeAData(int simuloinninID);

	public ArrayList<Double> haePPData(int simuloinninID, String nimi);
	
	public ArrayList<Double> haeSyotteet(int simuloinninID);

}
