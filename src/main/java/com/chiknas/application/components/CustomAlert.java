package com.chiknas.application.components;

import javafx.scene.ImageCursor;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * com.chiknas.application.Helper, created on 29/06/2019 17:58 <p>
 * @author NikolaosK
 */
public class CustomAlert extends Alert {
  public CustomAlert(AlertType alertType) {
    super(alertType);

    //add icon to alert
    Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
    stage.getIcons().add(
        new Image(getClass().getResource("/gameIcons/logo.jpg").toString()));

    DialogPane dialogPane = this.getDialogPane();
    dialogPane.setCursor(new ImageCursor(new Image(getClass().getResource("/images/chiknasLogo.png").toExternalForm())));
    dialogPane.getStylesheets().add(
        getClass().getResource("/application.css").toExternalForm());
    dialogPane.getStyleClass().add("alertDialog");
  }
}
