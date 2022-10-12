package view;

import java.text.DecimalFormat;

import dao.DAO;
import dao.IDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class TulosRuutuKontrolleri {

	@FXML
	ComboBox<String> ppChoiceBox;

	@FXML
	ComboBox<Integer> simulointiChoiceBox;

	@FXML
	private Label kokonaisTulot;

	@FXML
	private Label asiakkaidenMaara;

	@FXML
	private Label poistuneetAsiakkaat;

	@FXML
	private Label lapimenoAikaKA;

	@FXML
	private Label suoritusTeho;

	@FXML
	private Label rahaaKaytettyKA;

	@FXML
	private Label vietettyAikaKA;

	@FXML
	private Label kaytyjenPisteidenMaaraKA;

	@FXML
	private Label ppTulot;

	@FXML
	private Label palvelunKayttomaara;

	@FXML
	private Label aktiiviAika;

	@FXML
	private Label palveluAikaKA;

	@FXML
	private Label kayttoAste;

	@FXML
	private Label aikaaAsiakkaidenValissa;

	@FXML
	private Label kassaPalveluajanKA;

	@FXML
	private Label lipunHinta;

	@FXML
	private Label simulaationKesto;

	@FXML
	private Label vuokraakoPalveluajanKA;

	@FXML
	private Label vuokraamoOstostenKA;

	@FXML
	private Label kahvilaPalveluajanKA;

	@FXML
	private Label kahvilaOstostenKA;

	@FXML
	private Label rinne1PalveluajanKA;

	@FXML
	private Label rinne2PalveluajanKA;

	@FXML
	private Button tyhjennysNappi;

	Integer ID;

	private IDAO dao = new DAO();

	DecimalFormat df = new DecimalFormat("###.###");

	public void initialize() {
		ObservableList<String> availableChoices = FXCollections.observableArrayList("Kassa", "Vuokraamo", "Kahvila",
				"Rinne 1", "Rinne2");
		ppChoiceBox.setItems(availableChoices);

		ObservableList<Integer> IDt = FXCollections.observableArrayList(dao.IDpituus());
		simulointiChoiceBox.setItems(IDt);
	}

	@FXML
	public void asetaData() {
		ID = simulointiChoiceBox.getSelectionModel().getSelectedItem();

		// Laskettelukeskus taulukon tulokset
		simulaationKesto.setText(df.format(dao.haeLKData(ID).get(0)));
		kokonaisTulot.setText(df.format(dao.haeLKData(ID).get(1)));
		asiakkaidenMaara.setText(df.format(dao.haeLKData(ID).get(2)));
		poistuneetAsiakkaat.setText(df.format(dao.haeLKData(ID).get(3)));
		lapimenoAikaKA.setText(df.format(dao.haeLKData(ID).get(4)));
		suoritusTeho.setText(df.format(dao.haeLKData(ID).get(5)));

		// Asiakas taulukon tulokset
		rahaaKaytettyKA.setText(df.format(dao.haeAData(ID).get(0)));
		vietettyAikaKA.setText(df.format(dao.haeAData(ID).get(1)));
		kaytyjenPisteidenMaaraKA.setText(df.format(dao.haeAData(ID).get(2)));

		// Syötteet taulukon tulokset
		simulaationKesto.setText(df.format(dao.haeSyotteet(ID).get(0)));
		aikaaAsiakkaidenValissa.setText(df.format(dao.haeSyotteet(ID).get(1)));
		kassaPalveluajanKA.setText(df.format(dao.haeSyotteet(ID).get(2)));
		lipunHinta.setText(df.format(dao.haeSyotteet(ID).get(3)));
		vuokraakoPalveluajanKA.setText(df.format(dao.haeSyotteet(ID).get(4)));
		vuokraamoOstostenKA.setText(df.format(dao.haeSyotteet(ID).get(5)));
		kahvilaPalveluajanKA.setText(df.format(dao.haeSyotteet(ID).get(6)));
		kahvilaOstostenKA.setText(df.format(dao.haeSyotteet(ID).get(7)));
		rinne1PalveluajanKA.setText(df.format(dao.haeSyotteet(ID).get(8)));
		rinne2PalveluajanKA.setText(df.format(dao.haeSyotteet(ID).get(9)));

	}

	@FXML
	public void tyhjennaTietokanta(ActionEvent event) {
		dao.tyhjennaTietokanta();
		try {
			ObservableList<Integer> IDt = FXCollections.observableArrayList(dao.IDpituus());
			simulointiChoiceBox.setItems(IDt);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		tyhjennaTulokset();
	}

	public void tyhjennaTulokset() {

		simulaationKesto.setText("0");
		kokonaisTulot.setText("0");
		asiakkaidenMaara.setText("0");
		poistuneetAsiakkaat.setText("0");
		lapimenoAikaKA.setText("0");
		suoritusTeho.setText("0");
		rahaaKaytettyKA.setText("0");
		vietettyAikaKA.setText("0");
		kaytyjenPisteidenMaaraKA.setText("0");
		simulaationKesto.setText("0");
		aikaaAsiakkaidenValissa.setText("0");
		kassaPalveluajanKA.setText("0");
		lipunHinta.setText("0");
		vuokraakoPalveluajanKA.setText("0");
		vuokraamoOstostenKA.setText("0");
		kahvilaPalveluajanKA.setText("0");
		kahvilaOstostenKA.setText("0");
		rinne1PalveluajanKA.setText("0");
		rinne2PalveluajanKA.setText("0");
	}
}
