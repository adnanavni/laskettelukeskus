package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootKontrolleri {

	@FXML
	public void vaihdaInfoIkkunaan(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoRuutu.fxml"));
		Parent root = null;
		try {
			root = (Parent) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Stage stage = new Stage();
		stage.setTitle("Info");
		stage.setResizable(false);
		stage.setScene(new Scene(root));
		stage.initModality(Modality.WINDOW_MODAL);
		stage.getIcons().add(new Image("file:resources/images/icon.png"));
		stage.show();
	}
}
