package com.chiknas.application.components;

import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * com.chiknas.application.components.CustomScene, created on 13/09/2019 22:34 <p>
 * @author NikolaosK
 */
public class CustomScene extends Scene {
  public CustomScene(Parent root) {
    super(root);
    this.setCursor(new ImageCursor(new Image(getClass().getResource("/images/chiknasLogo.png").toExternalForm())));
  }
}
