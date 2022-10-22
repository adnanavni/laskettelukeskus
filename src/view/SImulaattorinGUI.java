package view;

 import java.io.IOException;
import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

 public class SImulaattorinGUI extends Application {
 	 private Stage primaryStage;
 	    private BorderPane rootLayout;
 	   private EkaRuutuKontrolleri kontrolleri;
 	   
 		private TextField aika;
 		private TextField viive;
 		private static Label tulos;

 	    @Override
 	    public void start(Stage primaryStage) {
 	        this.primaryStage = primaryStage;
 	        this.primaryStage.setTitle("Laskettelukeskus");
 	       this.primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));
 	       

 	        initRootLayout();
 	        showEkascene();
 	    }

 	    /**
 	     * Initializes the root layout.
 	     */
 	    public void initRootLayout() {
 	        try {
 	            // Load root layout from fxml file.
 	            FXMLLoader loader = new FXMLLoader();
 	            loader.setLocation(SImulaattorinGUI.class.getResource("RootLayout.fxml"));
 	            rootLayout = (BorderPane) loader.load();

 	            // Show the scene containing the root layout.
 	            Scene scene = new Scene(rootLayout);
 	            primaryStage.setScene(scene); 
 	            primaryStage.show();
 	        } catch (IOException e) {
 	            e.printStackTrace();
 	        }
 	    }


 	    /**
 	     * Shows the person overview inside the root layout.
 	     */
 	    public void showEkascene() {
 	        try {
 	            // Load person overview.
 	            FXMLLoader loader = new FXMLLoader();
 	            loader.setLocation(SImulaattorinGUI.class.getResource("EkaRuutu.fxml"));
 	            AnchorPane personOverview = (AnchorPane) loader.load();

 	            // Set person overview into the center of root layout.
 	            rootLayout.setCenter(personOverview);
 	            
 	            EkaRuutuKontrolleri kontrolleri = loader.getController();
 	            kontrolleri.setMainApp(this); 

 	            
 	        } catch (IOException e) {
 	            e.printStackTrace();
 	        }
 	    }

		public void simuloi() {
			kontrolleri.kaynnistaSimulointi();
		}
 	    
 		public double getAika() {
 			return Double.parseDouble(aika.getText());
 		}

 		public long getViive() {
 			return Long.parseLong(viive.getText());
 		}

 		public static void setLoppuaika(double aika) {
 			DecimalFormat formatter = new DecimalFormat("#0.00");
 			tulos.setText(formatter.format(aika));
 		}
 		/**
 		 * Returns the main stage.
 		 * @return
 		 */
 		public Stage getPrimaryStage() {
 			return primaryStage;
 		}

 	    public static void main(String[] args) {
 	        launch(args);
 	    }

 }
