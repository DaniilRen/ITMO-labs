package view.gui.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.filter.EntityFilter;
import util.i18n.I18nManager;

public final class FilterDialog {
  public static class FilterRow {
    private final EntityFilter.Condition condition;
    private boolean selected;

    public FilterRow(EntityFilter.Condition condition) {
      this.condition = condition;
    }

    public String getField() {
      return condition.field();
    }

    public String getAction() {
      return condition.operator().symbol();
    }

    public String getValue() {
      return condition.value();
    }

    public boolean isSelected() {
      return selected;
    }

    public void setSelected(boolean selected) {
      this.selected = selected;
    }

    public EntityFilter.Condition getCondition() {
      return condition;
    }
  }

  private FilterDialog() {}

  public static Optional<List<EntityFilter.Condition>> show(Stage owner, List<EntityFilter.Condition> existing) {
    Stage stage = new Stage();
    if (owner != null) {
      stage.initOwner(owner);
    }
    stage.initModality(Modality.APPLICATION_MODAL);
    I18nManager i18n = I18nManager.get();

    ObservableTable data = new ObservableTable(existing);
    TableView<FilterRow> table = buildTable(data, i18n);

    Button addBtn = new Button(i18n.get("filter.add.field"));
    addBtn.getStyleClass().add("secondary-dark-button");
    Button removeBtn = new Button(i18n.get("button.delete"));
    removeBtn.getStyleClass().add("danger-button");
    Button cancelBtn = new Button(i18n.get("button.cancel"));
    cancelBtn.getStyleClass().add("secondary-dark-button");
    Button applyBtn = new Button(i18n.get("button.execute"));
    applyBtn.getStyleClass().add("accent-button");

    final List<EntityFilter.Condition>[] result = new List[] {null};

    addBtn.setOnAction(
        e -> {
          Optional<EntityFilter.Condition> created = AddFilterFieldDialog.show(stage);
          created.ifPresent(
              condition -> data.rows.add(new FilterRow(condition)));
        });

    removeBtn.setOnAction(
        e -> data.rows.removeIf(FilterRow::isSelected));

    cancelBtn.setOnAction(e -> stage.close());

    applyBtn.setOnAction(
        e -> {
          result[0] =
              data.rows.stream().map(FilterRow::getCondition).toList();
          stage.close();
        });

    HBox top = new HBox(10, addBtn, removeBtn);
    HBox bottom = new HBox(10, cancelBtn, applyBtn);
    bottom.setAlignment(Pos.CENTER_RIGHT);

    VBox root = new VBox(12, new Label(i18n.get("filter.title")), top, table, bottom);
    root.setPadding(new Insets(16));
    root.getStyleClass().add("dialog-root");

    stage.setTitle(i18n.get("filter.title"));
    stage.setScene(new javafx.scene.Scene(root, 520, 360));
    stage.getScene().getStylesheets().add(FilterDialog.class.getResource("/css/style.css").toExternalForm());
    stage.showAndWait();

    return Optional.ofNullable(result[0]);
  }

  private static TableView<FilterRow> buildTable(ObservableTable data, I18nManager i18n) {
    TableView<FilterRow> table = new TableView<>(data.rows);
    TableColumn<FilterRow, Boolean> selectCol = new TableColumn<>("");
    selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
    selectCol.setCellFactory(
        col ->
            new javafx.scene.control.TableCell<>() {
              private final CheckBox checkBox = new CheckBox();

              {
                checkBox.selectedProperty()
                    .addListener(
                        (obs, oldVal, newVal) -> {
                          FilterRow row = getTableView().getItems().get(getIndex());
                          row.setSelected(newVal);
                        });
              }

              @Override
              protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  FilterRow row = getTableView().getItems().get(getIndex());
                  checkBox.setSelected(row.isSelected());
                  setGraphic(checkBox);
                }
              }
            });

    TableColumn<FilterRow, String> nameCol = new TableColumn<>(i18n.get("filter.field.name"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("field"));
    TableColumn<FilterRow, String> actionCol = new TableColumn<>(i18n.get("filter.field.action"));
    actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
    TableColumn<FilterRow, String> valueCol = new TableColumn<>(i18n.get("filter.field.value"));
    valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

    table.getColumns().addAll(selectCol, nameCol, actionCol, valueCol);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    return table;
  }

  private static class ObservableTable {
    private final javafx.collections.ObservableList<FilterRow> rows;

    ObservableTable(List<EntityFilter.Condition> existing) {
      rows = FXCollections.observableArrayList();
      if (existing != null) {
        existing.forEach(condition -> rows.add(new FilterRow(condition)));
      }
    }
  }
}
