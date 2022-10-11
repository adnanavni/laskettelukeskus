package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public interface IKontrolleriMtoV {

	// Rajapinta, joka tarjotaan moottorille:

	void naytaJono(Label l, int jono);

	public Label getRinne1Label();

	public Label getRinne2Label();

	public Label getVuokraamoLabel();

	public Label getKassaLabel();

	public Label getKahvilaLabel();

	public void naytaAika(double aika);

	public Button simuloiNappi();
}
