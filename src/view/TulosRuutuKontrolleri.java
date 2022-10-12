package view;

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

		simulaationKesto.setText(Double.toString(dao.haeLKData(ID).get(0)));
		kokonaisTulot.setText(Double.toString(dao.haeLKData(ID).get(1)));
		asiakkaidenMaara.setText(Double.toString(dao.haeLKData(ID).get(2)));
		poistuneetAsiakkaat.setText(Double.toString(dao.haeLKData(ID).get(3)));
		lapimenoAikaKA.setText(Double.toString(dao.haeLKData(ID).get(4)));
		suoritusTeho.setText(Double.toString(dao.haeLKData(ID).get(5)));
	}

	@FXML
	public void tyhjennaTietokanta(ActionEvent event) {
		dao.tyhjennaTietokanta();
	}
}
