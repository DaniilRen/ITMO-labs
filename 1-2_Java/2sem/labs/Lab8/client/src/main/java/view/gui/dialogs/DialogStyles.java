package view.gui.dialogs;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class DialogStyles {
  private DialogStyles() {}

  public static void setup(Stage stage, Parent root, double width, double height) {
    setup(stage, root, width, height, null);
  }

  public static void setup(Stage stage, Parent root, double width, double height, Stage owner) {
    stage.initModality(Modality.NONE);
    if (owner != null) {
      stage.initOwner(owner);
      owner.setOpacity(1.0);
    }

    if (!root.getStyleClass().contains("dialog-root")) {
      root.getStyleClass().add("dialog-root");
    }
    Scene scene = new Scene(root, width, height);
    scene.setFill(Color.web("#222733"));
    scene.getStylesheets().add(DialogStyles.class.getResource("/css/style.css").toExternalForm());
    stage.setScene(scene);
    stage.setOpacity(1.0);
  }
}
