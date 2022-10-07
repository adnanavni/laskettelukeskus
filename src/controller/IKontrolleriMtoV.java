package controller;

import javafx.scene.control.Label;

public interface IKontrolleriMtoV {

	// Rajapinta, joka tarjotaan moottorille:

	public void naytaLoppuaika(double aika);

	public void visualisoiAsiakas();

	public void naytaJono1(Label l, int jono);

	public Label getRinne1Label();

	public Label getRinne2Label();

	public Label getVuokraamoLabel();

	public Label getKassaLabel();

	public Label getKahvilaLabel();

}
