package view;

import java.io.IOException;
import java.text.DecimalFormat;

import controller.IKontrolleriMtoV;
import controller.IKontrolleriVtoM;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	Alert error = new Alert(AlertType.ERROR);

	OmaGUI omaGUI;

	private IMoottori moottori;

	public void setMainApp(OmaGUI mainApp) {
		this.omaGUI = mainApp;
	}

	public void kaynnistaSimulointi() {
		moottori = new OmaMoottori(this); // luodaan uusi moottorisäie jokaista simulointia varten

		radioButtonit();

		moottori.setSimulointiaika(simulaatioAika);
		moottori.setViive(10);

		try {
			alkuarvot();
		} catch (NumberFormatException e) {
			error.setContentText("Laita kenttiin vain numeroita!");
			error.show();
		}

		((Thread) moottori).start();
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
		stage.setResizable(false);
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

	@Override
	public void naytaAika(double aika) {
		Platform.runLater(() -> {
			DecimalFormat f = new DecimalFormat("00.00");
			aikaLabel.setText("Aika: " + f.format(aika));
		});

	}

	public Button simuloiNappi() {
		return simuloiNappi;
	}

	public void radioButtonit() {

		if (minuutti.isSelected()) {
			if (kestoField.getText().isEmpty()) {
				simulaatioAika = Double.parseDouble(kestoField.getPromptText()) * 1000 * 60;
			} else {
				simulaatioAika = Double.parseDouble(kestoField.getText()) * 1000 * 60;
			}
		} else if (sekunti.isSelected()) {
			if (kestoField.getText().isEmpty()) {
				simulaatioAika = Double.parseDouble(kestoField.getPromptText()) * 1000;
			} else {
				simulaatioAika = Double.parseDouble(kestoField.getText()) * 1000;
			}
		}
	}

	public void alkuarvot() {

		if (kassaSaapumisvali.getText().isEmpty()) {
			moottori.setSaapumisvaliKA(Double.parseDouble(kassaSaapumisvali.getPromptText()));
		} else {
			moottori.setSaapumisvaliKA(Double.parseDouble(kassaSaapumisvali.getText()));
		}

		if (kassaPalveluaika.getText().isEmpty()) {
			moottori.kassaSaapumisaika(Double.parseDouble(kassaPalveluaika.getPromptText()));
		} else {
			moottori.kassaSaapumisaika(Double.parseDouble(kassaPalveluaika.getText()));
		}

		if (kassaLippu.getText().isEmpty()) {
			moottori.kassaHinta(Double.parseDouble(kassaLippu.getPromptText()));
		} else {
			moottori.kassaHinta(Double.parseDouble(kassaLippu.getText()));
		}

		if (vuokraamoPalveluaika.getText().isEmpty()) {
			moottori.vuokraamoPalveluaika(Double.parseDouble(vuokraamoPalveluaika.getPromptText()));
		} else {
			moottori.vuokraamoPalveluaika(Double.parseDouble(vuokraamoPalveluaika.getText()));
		}

		if (vuokraamoHinnat.getText().isEmpty()) {
			moottori.vuokraamoHinta(Double.parseDouble(vuokraamoHinnat.getPromptText()));
		} else {
			moottori.vuokraamoHinta(Double.parseDouble(vuokraamoHinnat.getText()));
		}

		if (kahvilaPalveluaika.getText().isEmpty()) {
			moottori.kahvilaPalveluaika(Double.parseDouble(kahvilaPalveluaika.getPromptText()));
		} else {
			moottori.kahvilaPalveluaika(Double.parseDouble(kahvilaPalveluaika.getText()));
		}

		if (kahvilaHinnat.getText().isEmpty()) {
			moottori.kahvilaHinta(Double.parseDouble(kahvilaHinnat.getPromptText()));
		} else {
			moottori.kahvilaHinta(Double.parseDouble(kahvilaHinnat.getText()));
		}

		if (ekaRinnePalveluaika.getText().isEmpty()) {
			moottori.ekaRinnePalveluaika(Double.parseDouble(ekaRinnePalveluaika.getPromptText()));
		} else {
			moottori.ekaRinnePalveluaika(Double.parseDouble(ekaRinnePalveluaika.getText()));
		}

		if (tokaRinnePalveluaika.getText().isEmpty()) {
			moottori.TokaRinnePalveluaika(Double.parseDouble(tokaRinnePalveluaika.getPromptText()));
		} else {
			moottori.TokaRinnePalveluaika(Double.parseDouble(tokaRinnePalveluaika.getText()));
		}

	}

}
