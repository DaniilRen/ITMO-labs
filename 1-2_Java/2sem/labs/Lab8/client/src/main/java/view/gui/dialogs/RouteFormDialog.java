package view.gui.dialogs;

import java.time.LocalDateTime;
import java.util.Optional;

import common.models.Coordinates;
import common.models.LocationFrom;
import common.models.LocationTo;
import common.models.Route;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.i18n.I18nManager;

public final class RouteFormDialog {
  private RouteFormDialog() {}

  public static Optional<Route> show(Stage owner, String author, Route existing) {
    Stage stage = new Stage();
    I18nManager i18n = I18nManager.get();

    TextField nameField = new TextField();
    nameField.getStyleClass().add("text-field");
    TextField xField = new TextField();
    xField.getStyleClass().add("text-field");
    TextField yField = new TextField();
    yField.getStyleClass().add("text-field");
    TextField distanceField = new TextField();
    distanceField.getStyleClass().add("text-field");
    TextField fromXField = new TextField();
    fromXField.getStyleClass().add("text-field");
    TextField fromYField = new TextField();
    fromYField.getStyleClass().add("text-field");
    TextField fromNameField = new TextField();
    fromNameField.getStyleClass().add("text-field");
    TextField toXField = new TextField();
    toXField.getStyleClass().add("text-field");
    TextField toYField = new TextField();
    toYField.getStyleClass().add("text-field");
    TextField toZField = new TextField();
    toZField.getStyleClass().add("text-field");
    TextField toNameField = new TextField();
    toNameField.getStyleClass().add("text-field");
    Label errorLabel = new Label();
    errorLabel.getStyleClass().add("error-label");

    if (existing != null) {
      nameField.setText(existing.getName());
      if (existing.getCoordinates() != null) {
        xField.setText(String.valueOf(existing.getCoordinates().getX()));
        yField.setText(String.valueOf(existing.getCoordinates().getY()));
      }
      distanceField.setText(String.valueOf(existing.getDistance()));
      if (existing.getLocationFrom() != null) {
        fromXField.setText(String.valueOf(existing.getLocationFrom().getX()));
        fromYField.setText(String.valueOf(existing.getLocationFrom().getY()));
        fromNameField.setText(existing.getLocationFrom().getName());
      }
      if (existing.getLocationTo() != null) {
        toXField.setText(String.valueOf(existing.getLocationTo().getX()));
        toYField.setText(String.valueOf(existing.getLocationTo().getY()));
        toZField.setText(String.valueOf(existing.getLocationTo().getZ()));
        toNameField.setText(existing.getLocationTo().getName());
      }
    }

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(8);
    int row = 0;
    row = addRow(grid, row, i18n.get("route.name"), nameField);
    row = addRow(grid, row, i18n.get("route.coord.x"), xField);
    row = addRow(grid, row, i18n.get("route.coord.y"), yField);
    row = addRow(grid, row, i18n.get("route.distance"), distanceField);
    row = addRow(grid, row, i18n.get("route.from.x"), fromXField);
    row = addRow(grid, row, i18n.get("route.from.y"), fromYField);
    row = addRow(grid, row, i18n.get("route.from.name"), fromNameField);
    row = addRow(grid, row, i18n.get("route.to.x"), toXField);
    row = addRow(grid, row, i18n.get("route.to.y"), toYField);
    row = addRow(grid, row, i18n.get("route.to.z"), toZField);
    row = addRow(grid, row, i18n.get("route.to.name"), toNameField);

    final Route[] result = {null};

    Button saveBtn = new Button(i18n.get("button.execute"));
    saveBtn.getStyleClass().add("accent-button");
    Button cancelBtn = new Button(i18n.get("button.cancel"));
    cancelBtn.getStyleClass().add("secondary-dark-button");

    saveBtn.setOnAction(
        e -> {
          try {
            Long yVal = yField.getText().isBlank() ? null : Long.parseLong(yField.getText().trim());
            Double fromY = fromYField.getText().isBlank() ? null : Double.parseDouble(fromYField.getText().trim());
            Double toX = toXField.getText().isBlank() ? null : Double.parseDouble(toXField.getText().trim());

            Route route =
                new Route(
                    nameField.getText().trim(),
                    new Coordinates(Float.parseFloat(xField.getText().trim()), yVal),
                    LocalDateTime.now(),
                    new LocationFrom(
                        Integer.parseInt(fromXField.getText().trim()),
                        fromY,
                        fromNameField.getText().trim()),
                    new LocationTo(
                        toX,
                        Double.parseDouble(toYField.getText().trim()),
                        Integer.parseInt(toZField.getText().trim()),
                        toNameField.getText().trim()),
                    Integer.parseInt(distanceField.getText().trim()),
                    author);
            if (!route.validate()) {
              errorLabel.setText(i18n.get("route.error.invalid"));
              return;
            }
            result[0] = route;
            stage.close();
          } catch (Exception ex) {
            errorLabel.setText(i18n.get("route.error.invalid"));
          }
        });

    cancelBtn.setOnAction(e -> stage.close());

    HBox buttons = new HBox(10, cancelBtn, saveBtn);
    buttons.setAlignment(Pos.CENTER_RIGHT);

    Label title = new Label(i18n.get("route.form.title"));
    title.getStyleClass().add("title-label");

    VBox root = new VBox(12, title, grid, errorLabel, buttons);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(20));

    DialogStyles.setup(stage, root, 440, 540, owner);
    stage.setTitle(i18n.get("route.form.title"));
    stage.showAndWait();

    return Optional.ofNullable(result[0]);
  }

  private static int addRow(GridPane grid, int row, String label, TextField field) {
    Label lbl = new Label(label);
    lbl.getStyleClass().add("field-label");
    grid.add(lbl, 0, row);
    grid.add(field, 1, row);
    field.setPrefWidth(220);
    return row + 1;
  }
}
