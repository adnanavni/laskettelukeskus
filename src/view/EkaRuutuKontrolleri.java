package view;

import java.io.IOException;

import controller.IKontrolleriMtoV;
import controller.IKontrolleriVtoM;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simu.framework.IMoottori;
import simu.model.OmaMoottori;

public class EkaRuutuKontrolleri implements IKontrolleriMtoV, IKontrolleriVtoM {

	@FXML
	private Button simuloiNappi;

	@FXML
	private TextField kestoField;

	@FXML
	private Label aikaLabel;

	@FXML
	private RadioButton minuutti;

	@FXML
	private RadioButton sekunti;

	@FXML
	private Label rinne1Label;

	@FXML
	private Label rinne2Label;

	@FXML
	private Label vuokraamoLabel;

	@FXML
	private Label kassaLabel;

	@FXML
	private Label kahvilaLabel;

	@FXML
	private TextField kassaSaapumisvali;

	@FXML
	private TextField kassaPalveluaika;

	@FXML
	private TextField kassaLippu;

	@FXML
	private TextField vuokraamoPalveluaika;

	@FXML
	private TextField vuokraamoHinnat;

	@FXML
	private TextField kahvilaPalveluaika;

	@FXML
	private TextField kahvilaHinnat;

	@FXML
	private TextField ekaRinnePalveluaika;

	@FXML
	private TextField tokaRinnePalveluaika;

	private double simulaatioAika;

	OmaGUI omaGUI;

	private IMoottori moottori;

	public void setMainApp(OmaGUI mainApp) {
		this.omaGUI = mainApp;
	}

	public void kaynnistaSimulointi() {
		moottori = new OmaMoottori(this); // luodaan uusi moottorisäie jokaista simulointia varten

		if (minuutti.isSelected()) {
			simulaatioAika = Double.parseDouble(kestoField.getText()) * 1000 * 60;
		} else if (sekunti.isSelected()) {
			simulaatioAika = Double.parseDouble(kestoField.getText()) * 1000;
		}

		moottori.setSimulointiaika(simulaatioAika);
		moottori.setViive(10);

		moottori.setSaapumisvaliKA(Double.parseDouble(kassaSaapumisvali.getText()));
		moottori.kassaSaapumisaika(Double.parseDouble(kassaPalveluaika.getText()));
		moottori.kassaHinta(Double.parseDouble(kassaLippu.getText()));

		moottori.vuokraamoPalveluaika(Double.parseDouble(vuokraamoPalveluaika.getText()));
		moottori.vuokraamoHinta(Double.parseDouble(vuokraamoHinnat.getText()));

		moottori.kahvilaPalveluaika(Double.parseDouble(kahvilaPalveluaika.getText()));
		moottori.kahvilaHinta(Double.parseDouble(kahvilaHinnat.getText()));

		moottori.ekaRinnePalveluaika(Double.parseDouble(ekaRinnePalveluaika.getText()));
		moottori.TokaRinnePalveluaika(Double.parseDouble(tokaRinnePalveluaika.getText()));

		// ui.getVisualisointi().tyhjennaNaytto();
		((Thread) moottori).start();
		// ((Thread)moottori).run(); // Ei missään tapauksessa näin. Miksi?
	}

	@FXML
	public void vaihdaTulosIkkunaan(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TulosRuutu.fxml"));
		Parent root = null;
		try {
			root = (Parent) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Stage stage = new Stage();
		stage.setTitle("Tulokset");
		stage.setScene(new Scene(root));
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.getIcons().add(new Image("file:resources/images/icon.png"));
		stage.show();
	}

	public void initialize() {

	}

	public Label getRinne1Label() {
		return rinne1Label;
	}

	public Label getRinne2Label() {
		return rinne2Label;
	}

	public Label getVuokraamoLabel() {
		return vuokraamoLabel;
	}

	public Label getKassaLabel() {
		return kassaLabel;
	}

	public Label getKahvilaLabel() {
		return kahvilaLabel;
	}

	@Override
	public void naytaLoppuaika(double aika) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visualisoiAsiakas() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nopeuta() {
		// TODO Auto-generated method stub
		moottori.setViive((long) (moottori.getViive() * 0.9));
	}

	@Override
	public void hidasta() {
		// TODO Auto-generated method stub
		moottori.setViive((long) (moottori.getViive() * 1.10));
	}

	@Override
	public void naytaJono(Label l, int jono) {
		Platform.runLater(() -> {
			l.setText(Integer.toString(jono));
		});

	}

}
