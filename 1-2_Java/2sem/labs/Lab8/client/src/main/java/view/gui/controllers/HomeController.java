package view.gui.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import common.models.Entity;
import common.models.Route;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presenter.Presenter;
import util.access.ClientAccess;
import util.filter.EntityFilter;
import util.i18n.I18nManager;
import view.gui.GraphicsView;
import view.gui.dialogs.AddUserDialog;
import view.gui.dialogs.ExecuteFileDialog;
import view.gui.dialogs.FilterDialog;
import view.gui.dialogs.RouteFormDialog;

public class HomeController {
    @FXML private Label userLabel;
    @FXML private Label sortLabel;
    @FXML private Label serverStatusLabel;
    @FXML private Label syncStatusLabel;
    @FXML private TableView<Entity> tableView;
    @FXML private Button filterButton;
    @FXML private Button historyButton;
    @FXML private Button helpButton;
    @FXML private Button infoButton;
    @FXML private Button logoutButton;
    @FXML private Button clearButton;
    @FXML private Button filterStartsButton;
    @FXML private Button uniqueDistancesButton;
    @FXML private Button descendingDistanceButton;
    @FXML private Button addUserButton;
    @FXML private Button addElementButton;
    @FXML private Button executeFileButton;
    @FXML private Button visualisationButton;

    private GraphicsView mainView;
    private Presenter presenter;
    private Stage stage;
    private List<EntityFilter.Condition> activeFilters = new ArrayList<>();
    private String sortField = "id";
    private boolean sortAscending = true;
    private boolean tableInitialized;

    public void setMainView(GraphicsView mainView) {
        this.mainView = mainView;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void bindTexts() {
        I18nManager i18n = I18nManager.get();
        if (presenter.getCurrentUser() != null && presenter.getCurrentUser().getName() != null) {
            userLabel.setText(i18n.format("main.user", presenter.getCurrentUser().getName()));
        }
        filterButton.setText(i18n.get("main.filter"));
        updateSortStatusLabel();
        historyButton.setText(i18n.get("main.history"));
        helpButton.setText(i18n.get("main.help"));
        infoButton.setText(i18n.get("main.info"));
        logoutButton.setText(i18n.get("main.logout"));
        clearButton.setText(i18n.get("main.clear"));
        filterStartsButton.setText(i18n.get("main.filter.starts_with"));
        uniqueDistancesButton.setText(i18n.get("main.unique.distances"));
        descendingDistanceButton.setText(i18n.get("main.descending.distance"));
        addUserButton.setText(i18n.get("main.add.user"));
        addElementButton.setText(i18n.get("main.add.element"));
        executeFileButton.setText(i18n.get("main.execute.file"));
        visualisationButton.setText(i18n.get("main.visualisation"));
        boolean isAdmin =
                presenter.getCurrentUser() != null && presenter.getCurrentUser().getIsAdmin();
        addUserButton.setVisible(isAdmin);
        addUserButton.setManaged(isAdmin);
        if (tableInitialized) {
            refreshColumnTitles();
            tableView.setPlaceholder(new Label(i18n.get("table.empty")));
        }
    }

    public void updateStatus(String serverStatus, String syncStatus) {
        serverStatusLabel.setText(serverStatus);
        syncStatusLabel.setText(syncStatus);
    }

    public void initializeTable() {
        if (tableInitialized) {
            return;
        }
        tableView.setItems(mainView.getCollectionStore().getItems());
        tableView.setPlaceholder(new Label(I18nManager.get().get("table.empty")));
        tableView.setSortPolicy(table -> false);
        tableView.getColumns().add(createActionsColumn());
        tableView.getColumns().add(createColumn("table.id", "id"));
        tableView.getColumns().add(createColumn("table.coord.x", "x"));
        tableView.getColumns().add(createColumn("table.coord.y", "y"));
        tableView.getColumns().add(createColumn("table.name", "name"));
        tableView.getColumns().add(createColumn("table.distance", "distance"));
        tableView.getColumns().add(createColumn("table.author", "author"));
        tableView.getColumns().add(createDateColumn());
        tableView.getColumns().add(createObjectColumn("table.from", "from"));
        tableView.getColumns().add(createObjectColumn("table.to", "to"));
        tableInitialized = true;
        refreshColumnTitles();
        updateSortStatusLabel();
    }

    public void applyCollection() {
        List<Entity> master = mainView.getCollectionStore().getMaster();
        List<Entity> filtered = EntityFilter.apply(master, activeFilters);
        List<Entity> sorted = EntityFilter.sort(filtered, sortField, sortAscending);
        mainView.getCollectionStore().applyFiltered(sorted);
        refreshColumnTitles();
        updateSortStatusLabel();
        bindTexts();
    }

    @FXML
    private void handleHistory() {
        presenter.executeCommand("history", List.of(), false);
    }

    @FXML
    private void handleHelp() {
        presenter.executeCommand("help", List.of(), false);
    }

    @FXML
    private void handleInfo() {
        presenter.executeCommand("info", List.of(), false);
    }

    @FXML
    private void handleLogout() {
        presenter.logOut();
        mainView.onLogOut();
    }

    @FXML
    private void handleFilter() {
        Optional<List<EntityFilter.Condition>> result = FilterDialog.show(stage, activeFilters);
        result.ifPresent(
                conditions -> {
                    activeFilters = new ArrayList<>(conditions);
                    applyCollection();
                });
    }

    @FXML
    private void handleClear() {
        I18nManager i18n = I18nManager.get();
        Stage modal = new Stage();
        modal.initOwner(stage);
        modal.initModality(Modality.APPLICATION_MODAL);

        Button confirmBtn = new Button(i18n.get("button.execute"));
        confirmBtn.getStyleClass().add("danger-button");
        Button cancelBtn = new Button(i18n.get("button.cancel"));
        cancelBtn.getStyleClass().add("secondary-dark-button");

        confirmBtn.setOnAction(
                e -> {
                    presenter.executeCommand("clear", List.of(), false);
                    modal.close();
                });
        cancelBtn.setOnAction(e -> modal.close());

        VBox root =
                new VBox(
                        12,
                        new Label(i18n.get("dialog.confirm.clear")),
                        new HBox(10, cancelBtn, confirmBtn));
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("dialog-root");
        Scene scene = new Scene(root, 360, 140);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        modal.setScene(scene);
        modal.show();
    }

    @FXML
    private void handleFilterStartsWith() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(I18nManager.get().get("main.filter.starts_with"));
        dialog.setHeaderText(I18nManager.get().get("main.filter.starts_with"));
        dialog.showAndWait()
                .ifPresent(
                        prefix ->
                                presenter.executeCommand(
                                        "filter_starts_with_name", List.of(prefix), false));
    }

    @FXML
    private void handleUniqueDistances() {
        presenter.executeCommand("print_unique_distance", List.of(), false);
    }

    @FXML
    private void handleDescendingDistance() {
        sortField = "distance";
        sortAscending = false;
        applyCollection();
        presenter.executeCommand("print_field_descending_distance", List.of(), false);
    }

    @FXML
    private void handleAddUser() {
        if (presenter.getCurrentUser() == null || !presenter.getCurrentUser().getIsAdmin()) {
            mainView.displayError(I18nManager.get().get("error.not.admin"));
            return;
        }
        AddUserDialog.show(stage, presenter, mainView);
    }

    @FXML
    private void handleAddElement() {
        Optional<Route> route =
                RouteFormDialog.show(stage, presenter.getCurrentUser().getName(), null);
        route.ifPresent(
                value -> {
                    presenter.executeCommand("add", List.of(value), false);
                    presenter.refreshCollection();
                });
    }

    @FXML
    private void handleExecuteFile() {
        ExecuteFileDialog.show(stage, presenter, mainView);
    }

    @FXML
    private void handleVisualisation() {
        try {
            mainView.showVisualisationWindow();
        } catch (Exception e) {
            mainView.displayError(e.getMessage());
        }
    }

    private TableColumn<Entity, Void> createActionsColumn() {
        TableColumn<Entity, Void> actionCol = new TableColumn<>();
        actionCol.setPrefWidth(120);
        actionCol.setSortable(false);
        actionCol.setUserData("table.actions");
        actionCol.setCellFactory(
                col ->
                        new TableCell<>() {
                            private final Button deleteBtn = new Button("X");
                            private final Button updateBtn = new Button("UPDATE");
                            private final HBox buttons = new HBox(6, deleteBtn, updateBtn);

                            {
                                deleteBtn.getStyleClass().add("danger-button");
                                updateBtn.getStyleClass().add("update-button");
                                deleteBtn.setOnAction(
                                        e -> {
                                            Entity entity = getTableView().getItems().get(getIndex());
                                            confirmDelete(entity);
                                        });
                                updateBtn.setOnAction(
                                        e -> {
                                            Entity entity = getTableView().getItems().get(getIndex());
                                            updateEntity(entity);
                                        });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    return;
                                }
                                Entity entity = getTableView().getItems().get(getIndex());
                                boolean canModify = canModifyEntity(entity);
                                deleteBtn.setDisable(!canModify);
                                updateBtn.setDisable(!canModify);
                                setGraphic(buttons);
                            }
                        });
        return actionCol;
    }

    private boolean canModifyEntity(Entity entity) {
        if (!(entity instanceof Route route)) {
            return false;
        }
        return ClientAccess.canModify(route, presenter.getCurrentUser());
    }

    private TableColumn<Entity, String> createColumn(String titleKey, String field) {
        TableColumn<Entity, String> column = new TableColumn<>();
        column.setPrefWidth(90);
        column.setCellValueFactory(
                data -> new SimpleStringProperty(extractField((Route) data.getValue(), field)));
        configureSortableColumn(column, field);
        column.setUserData(titleKey);
        return column;
    }

    private void configureSortableColumn(TableColumn<Entity, ?> column, String field) {
        column.getProperties().put("sortField", field);
        column.setSortable(false);
        Label headerLabel = new Label();
        headerLabel.getStyleClass().add("sortable-header-label");
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setOnMouseClicked(e -> handleColumnHeaderSort(column));
        StackPane headerPane = new StackPane(headerLabel);
        headerPane.getStyleClass().add("sortable-column-header");
        column.setGraphic(headerPane);
        column.setText("");
        column.getProperties().put("headerLabel", headerLabel);
    }

    private void handleColumnHeaderSort(TableColumn<Entity, ?> column) {
        Object field = column.getProperties().get("sortField");
        if (!(field instanceof String sortKey)) {
            return;
        }
        if (sortKey.equals(sortField)) {
            sortAscending = !sortAscending;
        } else {
            sortField = sortKey;
            sortAscending = true;
        }
        applyCollection();
    }

    private TableColumn<Entity, String> createDateColumn() {
        TableColumn<Entity, String> column = new TableColumn<>();
        column.setPrefWidth(160);
        column.setCellValueFactory(
                data -> {
                    Route route = (Route) data.getValue();
                    if (route.getCreationDate() == null) {
                        return new SimpleStringProperty("");
                    }
                    return new SimpleStringProperty(
                            I18nManager.get().dateTimeFormatter().format(route.getCreationDate()));
                });
        configureSortableColumn(column, "creationDate");
        column.setUserData("table.date");
        return column;
    }

    private TableColumn<Entity, String> createObjectColumn(String titleKey, String field) {
        TableColumn<Entity, String> column = new TableColumn<>();
        column.setPrefWidth(140);
        column.setCellValueFactory(
                data -> {
                    Route route = (Route) data.getValue();
                    Object value =
                            "from".equals(field) ? route.getLocationFrom() : route.getLocationTo();
                    return new SimpleStringProperty(value == null ? "" : value.toString());
                });
        configureSortableColumn(column, field);
        column.setUserData(titleKey);
        return column;
    }

    private void refreshColumnTitles() {
        if (!tableInitialized) {
            return;
        }
        I18nManager i18n = I18nManager.get();
        String arrow =
                sortAscending ? i18n.get("table.sort.asc") : i18n.get("table.sort.desc");
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            if (!(column.getUserData() instanceof String key)) {
                continue;
            }
            String title = i18n.get(key);
            Object field = column.getProperties().get("sortField");
            boolean sorted = field instanceof String sortKey && sortKey.equals(sortField);
            if (sorted) {
                title = title + " " + arrow;
            }
            Object headerLabelObj = column.getProperties().get("headerLabel");
            if (headerLabelObj instanceof Label headerLabel) {
                headerLabel.setText(title);
                if (sorted) {
                    if (!headerLabel.getStyleClass().contains("sorted-header-label")) {
                        headerLabel.getStyleClass().add("sorted-header-label");
                    }
                } else {
                    headerLabel.getStyleClass().remove("sorted-header-label");
                }
                continue;
            }
            column.setText(title);
        }
    }

    private void updateSortStatusLabel() {
        if (sortLabel == null) {
            return;
        }
        I18nManager i18n = I18nManager.get();
        sortLabel.setText(i18n.get("table.sort.hint"));
        sortLabel.setTooltip(
                new javafx.scene.control.Tooltip(
                        i18n.format(
                                "table.sort.current",
                                i18n.get(sortFieldTitleKey(sortField)),
                                sortAscending ? i18n.get("table.sort.asc") : i18n.get("table.sort.desc"))));
    }

    private String sortFieldTitleKey(String field) {
        return switch (field) {
            case "id" -> "table.id";
            case "x" -> "table.coord.x";
            case "y" -> "table.coord.y";
            case "name" -> "table.name";
            case "distance" -> "table.distance";
            case "author" -> "table.author";
            case "creationDate" -> "table.date";
            case "from" -> "table.from";
            case "to" -> "table.to";
            default -> "table.id";
        };
    }

    private String extractField(Route route, String field) {
        if (route == null) {
            return "";
        }
        return switch (field) {
            case "id" -> String.valueOf(route.getId());
            case "name" -> route.getName();
            case "x" ->
                    route.getCoordinates() == null
                        ? ""
                        : I18nManager.get().numberFormat().format(route.getCoordinates().getX());
            case "y" ->
                    route.getCoordinates() == null
                        ? ""
                        : I18nManager.get().numberFormat().format(route.getCoordinates().getY());
            case "distance" -> I18nManager.get().numberFormat().format(route.getDistance());
            case "author" -> route.getAuthor();
            default -> "";
        };
    }

    private void confirmDelete(Entity entity) {
        I18nManager i18n = I18nManager.get();
        Stage modal = new Stage();
        modal.initOwner(stage);
        modal.initModality(Modality.APPLICATION_MODAL);

        Button confirmBtn = new Button(i18n.get("button.delete"));
        confirmBtn.getStyleClass().add("danger-button");
        Button cancelBtn = new Button(i18n.get("button.cancel"));
        cancelBtn.getStyleClass().add("secondary-dark-button");

        confirmBtn.setOnAction(
                e -> {
                    presenter.executeCommand("remove_by_id", List.of(entity.getId()), false);
                    presenter.refreshCollection();
                    modal.close();
                });
        cancelBtn.setOnAction(e -> modal.close());

        VBox root =
                new VBox(
                        12,
                        new Label(i18n.format("dialog.confirm.delete", entity.getId())),
                        new HBox(10, cancelBtn, confirmBtn));
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("dialog-root");
        Scene scene = new Scene(root, 360, 140);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        modal.setScene(scene);
        modal.show();
    }

    private void updateEntity(Entity entity) {
        if (!(entity instanceof Route route)) {
            return;
        }
        RouteFormDialog.show(stage, route.getAuthor(), route)
                .ifPresent(
                        updated -> {
                            presenter.executeCommand(
                                    "update", List.of(updated, entity.getId()), false);
                            presenter.refreshCollection();
                        });
    }
}
