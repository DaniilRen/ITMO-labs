package view.gui.dialogs;

import java.util.function.Consumer;

import common.models.Route;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.i18n.I18nManager;

public final class EntityInfoDialog {
  private EntityInfoDialog() {}

  public static void show(Stage owner, Route route, boolean canEdit, Consumer<Route> onEdit) {
    Stage stage = new Stage();
    if (owner != null) {
      stage.initOwner(owner);
    }
    stage.initModality(Modality.NONE);
    I18nManager i18n = I18nManager.get();

    String content =
        i18n.format(
            "entity.info",
            route.getId(),
            route.getName(),
            route.getAuthor(),
            i18n.numberFormat().format(route.getDistance()));

    Label title = new Label(route.getName());
    title.getStyleClass().add("title-label");
    Label body = new Label(content);
    body.getStyleClass().add("field-label");
    body.setWrapText(true);

    Button closeBtn = new Button(i18n.get("button.ok"));
    closeBtn.getStyleClass().add("secondary-dark-button");
    closeBtn.setOnAction(e -> stage.close());

    HBox buttons = new HBox(10);
    buttons.setAlignment(Pos.CENTER_RIGHT);
    buttons.getChildren().add(closeBtn);

    if (canEdit && onEdit != null) {
      Button editBtn = new Button(i18n.get("entity.edit"));
      editBtn.getStyleClass().add("accent-button");
      editBtn.setOnAction(
          e -> {
            stage.close();
            onEdit.accept(route);
          });
      buttons.getChildren().add(0, editBtn);
    }

    VBox root = new VBox(14, title, body, buttons);
    root.setAlignment(Pos.CENTER_LEFT);
    root.setPadding(new Insets(20));
    root.getStyleClass().add("dialog-root");
    root.setMaxWidth(360);

    stage.setTitle(i18n.get("dialog.info"));
    stage.setScene(new javafx.scene.Scene(root));
    stage.getScene().getStylesheets().add(EntityInfoDialog.class.getResource("/css/style.css").toExternalForm());
    stage.show();
  }
}
