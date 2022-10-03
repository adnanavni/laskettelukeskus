package view;

import java.io.IOException;

import controller.IKontrolleriMtoV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simu.framework.IMoottori;
import simu.model.OmaMoottori;

public class EkaRuutuKontrolleri implements IKontrolleriMtoV {
	
	
	@FXML
	private Button simuloiNappi;
	
	@FXML
	private TextField kestoField;
	
	@FXML
	private Label aikaLabel;
	
	private OmaGUI omaGUI;
	
	private IMoottori moottori;

	
	public void setMainApp(OmaGUI mainApp) {
		this.omaGUI = mainApp;
	} 
	
	public void kaynnistaSimulointi() {
		moottori = new OmaMoottori(this); // luodaan uusi moottorisäie jokaista simulointia varten
		moottori.setSimulointiaika(Double.parseDouble(kestoField.getText()));
		moottori.setViive(10);
	//	ui.getVisualisointi().tyhjennaNaytto();
		((Thread)moottori).start();
		System.out.println("JEee");
		//((Thread)moottori).run(); // Ei missään tapauksessa näin. Miksi?
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
	

	@Override
	public void naytaLoppuaika(double aika) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visualisoiAsiakas() {
		// TODO Auto-generated method stub
		
	}


}
