package view.gui.dialogs;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.filter.EntityFilter;
import util.i18n.I18nManager;

public final class AddFilterFieldDialog {
  private AddFilterFieldDialog() {}

  public static Optional<EntityFilter.Condition> show(Stage owner) {
    Stage stage = new Stage();
    if (owner != null) {
      stage.initOwner(owner);
    }
    stage.initModality(Modality.APPLICATION_MODAL);
    I18nManager i18n = I18nManager.get();

    ComboBox<String> fieldBox = new ComboBox<>();
    fieldBox.getItems().addAll("id", "name", "x", "y", "distance", "author", "creationDate");
    fieldBox.getSelectionModel().selectFirst();

    ComboBox<String> actionBox = new ComboBox<>();
    actionBox.getItems().addAll("=", "!=", ">", "<", ">=", "<=", "contains");
    actionBox.getSelectionModel().selectFirst();

    TextField valueField = new TextField();
    Label errorLabel = new Label();
    errorLabel.getStyleClass().add("error-label");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(8);
    grid.add(new Label(i18n.get("filter.field")), 0, 0);
    grid.add(fieldBox, 1, 0);
    grid.add(new Label(i18n.get("filter.action")), 0, 1);
    grid.add(actionBox, 1, 1);
    grid.add(new Label(i18n.get("filter.value")), 0, 2);
    grid.add(valueField, 1, 2);

    final EntityFilter.Condition[] result = {null};

    Button applyBtn = new Button(i18n.get("button.execute"));
    applyBtn.getStyleClass().add("accent-button");
    Button cancelBtn = new Button(i18n.get("button.cancel"));
    cancelBtn.getStyleClass().add("secondary-dark-button");

    applyBtn.setOnAction(
        e -> {
          if (valueField.getText().isBlank()) {
            errorLabel.setText(i18n.get("auth.error.empty"));
            return;
          }
          result[0] =
              new EntityFilter.Condition(
                  fieldBox.getValue(),
                  EntityFilter.Operator.fromSymbol(actionBox.getValue()),
                  valueField.getText().trim());
          stage.close();
        });
    cancelBtn.setOnAction(e -> stage.close());

    HBox buttons = new HBox(10, cancelBtn, applyBtn);
    buttons.setAlignment(Pos.CENTER_RIGHT);

    VBox root =
        new VBox(
            12,
            new Label(i18n.get("filter.add.title")),
            grid,
            errorLabel,
            buttons,
            new Label(i18n.get("error.output")));
    root.setPadding(new Insets(16));
    root.getStyleClass().add("dialog-root");

    stage.setTitle(i18n.get("filter.add.title"));
    stage.setScene(new javafx.scene.Scene(root, 380, 280));
    stage.getScene().getStylesheets().add(AddFilterFieldDialog.class.getResource("/css/style.css").toExternalForm());
    stage.showAndWait();

    return Optional.ofNullable(result[0]);
  }
}
