package controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import simu.framework.IMoottori;
import simu.model.OmaMoottori;
import view.ISimulaattorinUI;

public class Kontrolleri implements IKontrolleriVtoM, IKontrolleriMtoV { // UUSI

	private IMoottori moottori;
	private ISimulaattorinUI ui;

	public Kontrolleri(ISimulaattorinUI ui) {
		this.ui = ui;
	}

	// Moottorin ohjausta:

	@Override
	public void kaynnistaSimulointi() {
		moottori = new OmaMoottori(this); // luodaan uusi moottorisäie jokaista simulointia varten
		moottori.setSimulointiaika(ui.getAika());
		moottori.setViive(ui.getViive());
		ui.getVisualisointi().tyhjennaNaytto();
		((Thread) moottori).start();
		// ((Thread)moottori).run(); // Ei missään tapauksessa näin. Miksi?
	}

	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		moottori.setViive((long) (moottori.getViive() * 1.10));
	}

	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		moottori.setViive((long) (moottori.getViive() * 0.9));
	}

	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska FX-ui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata
	// JavaFX-säikeeseen:

	@Override
	public void naytaLoppuaika(double aika) {
		Platform.runLater(() -> ui.setLoppuaika(aika));
	}

	@Override
	public void visualisoiAsiakas() {
		Platform.runLater(new Runnable() {
			public void run() {
				ui.getVisualisointi().uusiAsiakas();
			}
		});
	}

	@Override
	public Label getRinne1Label() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getRinne2Label() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getVuokraamoLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getKassaLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getKahvilaLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void naytaJono(Label l, int jono) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void naytaAika(double aika) {
		// TODO Auto-generated method stub

	}

}
