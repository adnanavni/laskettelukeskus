package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class TulosRuutuKontrolleri {
	
	@FXML
	ChoiceBox<String> ppChoiceBox;
	
	@FXML
	ChoiceBox<String> simulointiChoiceBox;
	
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
	

	public void initialize() {
		ObservableList<String> availableChoices = FXCollections.observableArrayList("Kassa", "Vuokraamo","Kahvila","Rinne 1","Rinne2"); 
		ppChoiceBox.setItems(availableChoices);
	}

}
