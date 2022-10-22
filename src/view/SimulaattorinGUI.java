package view;

import java.io.IOException;
import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Simulaattorin view
 * 
 * @author Roope Kylli ja Perttu Vaarala
 * @version 1.0
 *
 */
public class SimulaattorinGUI extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private EkaRuutuKontrolleri kontrolleri;

	private static Label tulos;

	/**
	 * Käynnistää simulaattorin visualisuuden
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Laskettelukeskus");
		this.primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));

		initRootLayout();
		showEkascene();
	}

	/**
	 * Rootlayoutin käynnistys
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SimulaattorinGUI.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Käynnistää EkaRuutuFXML:n
	 */
	public void showEkascene() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SimulaattorinGUI.class.getResource("EkaRuutu.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(personOverview);

			EkaRuutuKontrolleri kontrolleri = loader.getController();
			kontrolleri.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Käynnistää simuloinnin
	 */
	public void simuloi() {
		kontrolleri.kaynnistaSimulointi();
	}

	/**
	 * Asettaa ajan näkyviin.
	 * 
	 * @param aika aika joka asetetaan näkymään. Päivittyy jatkuvasti.
	 */
	public static void setLoppuaika(double aika) {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		tulos.setText(formatter.format(aika));
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
