package view.gui.controllers;

import java.util.List;

import common.models.Entity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import presenter.Presenter;
import view.gui.GraphicsView;

public class HomeController {
    @FXML private Label userLabel;
    @FXML private TableView<Entity> tableView;
    
    private GraphicsView mainView;
    private Presenter presenter;
    private Stage stage;
    private ObservableList<Entity> data;

    @FXML
    private void initialize() {
        data = FXCollections.observableArrayList();
        tableView.setItems(data);
    }

    public void setMainView(GraphicsView mainView) {
        this.mainView = mainView;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleHistory() {
        presenter.executeCommand("history", null, false);
    }

    @FXML
    private void handleHelp() {
        presenter.executeCommand("help", null, false);
    }

    @FXML
    private void handleInfo() {
        presenter.executeCommand("info", null, false);
    }

    @FXML
    private void handleAddUser() {
        Stage modal = new Stage();
        VBox root = new VBox(15);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(20));
        
        Label label = new Label("Register new user");
        Button registerBtn = new Button("Register");
        Button cancelBtn = new Button("Cancel");
        
        registerBtn.setOnAction(e -> {
            mainView.onRegister();
            modal.close();
        });
        
        cancelBtn.setOnAction(e -> modal.close());
        
        root.getChildren().addAll(label, registerBtn, cancelBtn);
        Scene scene = new Scene(root, 300, 150);
        modal.setTitle("Add User");
        modal.setScene(scene);
        modal.show();
    }

    @FXML
    private void handleAddElement() {
        Stage modal = new Stage();
        VBox root = new VBox(15);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(20));
        
        Label label = new Label("Add new element");
        Button addBtn = new Button("Add");
        Button cancelBtn = new Button("Cancel");
        
        addBtn.setOnAction(e -> {
            Entity entity = mainView.onEntityAdd(presenter.getCurrentUser().getName());
            if (entity != null) {
                presenter.executeCommand("add", List.of(entity), false);
                refreshTable();
            }
            modal.close();
        });
        
        cancelBtn.setOnAction(e -> modal.close());
        
        root.getChildren().addAll(label, addBtn, cancelBtn);
        Scene scene = new Scene(root, 300, 150);
        modal.setTitle("Add Element");
        modal.setScene(scene);
        modal.show();
    }

    @FXML
    private void handleExecuteFile() {
        Stage modal = new Stage();
        VBox root = new VBox(15);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(20));
        
        TextField fileField = new TextField();
        fileField.setPromptText("Enter script filename");
        fileField.setMaxWidth(250);
        
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        
        Button executeBtn = new Button("Execute");
        Button cancelBtn = new Button("Cancel");
        
        executeBtn.setOnAction(e -> {
            String filename = fileField.getText();
            if (filename.isEmpty()) {
                errorLabel.setText("Please enter filename");
                return;
            }
            presenter.executeCommand("execute_script", List.of(filename), true);
            modal.close();
        });
        
        cancelBtn.setOnAction(e -> modal.close());
        
        root.getChildren().addAll(new Label("Execute Script File"), fileField, errorLabel, executeBtn, cancelBtn);
        Scene scene = new Scene(root, 350, 200);
        modal.setTitle("Execute File");
        modal.setScene(scene);
        modal.show();
    }

    @FXML
    private void handleVisualisation() {
        try {
            mainView.showVisualisationWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        tableView.getColumns().clear();
        presenter.executeCommand("show", null, false);
        
        userLabel.setText("Current user: " + presenter.getCurrentUser().getName());
        
        TableColumn<Entity, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Entity, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Entity, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        
        TableColumn<Entity, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        
        tableView.getColumns().addAll(idCol, nameCol, authorCol, dateCol);
        
        TableColumn<Entity, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<Entity, Void>() {
            private final Button deleteBtn = new Button("Delete");
            private final Button updateBtn = new Button("Update");
            private final HBox buttons = new HBox(5, deleteBtn, updateBtn);

            {
                deleteBtn.getStyleClass().add("danger-button");
                updateBtn.getStyleClass().add("primary-button");
                
                deleteBtn.setOnAction(e -> {
                    Entity entity = getTableView().getItems().get(getIndex());
                    showDeleteModal(entity);
                });
                updateBtn.setOnAction(e -> {
                    Entity entity = getTableView().getItems().get(getIndex());
                    showUpdateModal(entity);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        tableView.getColumns().add(actionCol);
    }

    private void showDeleteModal(Entity entity) {
        Stage modal = new Stage();
        VBox root = new VBox(15);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(20));
        
        Label label = new Label("Are you sure you want to delete item ID: " + entity.getId() + "?");
        Button confirmBtn = new Button("Delete");
        Button cancelBtn = new Button("Cancel");
        
        HBox buttons = new HBox(10, confirmBtn, cancelBtn);
        buttons.setAlignment(javafx.geometry.Pos.CENTER);
        
        confirmBtn.getStyleClass().add("danger-button");
        cancelBtn.getStyleClass().add("primary-button");
        
        confirmBtn.setOnAction(e -> {
            presenter.executeCommand("remove", List.of(entity.getId()), false);
            refreshTable();
            modal.close();
        });
        
        cancelBtn.setOnAction(e -> modal.close());
        
        root.getChildren().addAll(label, buttons);
        Scene scene = new Scene(root, 350, 150);
        modal.setTitle("Confirm Delete");
        modal.setScene(scene);
        modal.show();
    }

    private void showUpdateModal(Entity entity) {
        Stage modal = new Stage();
        VBox root = new VBox(15);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(20));
        
        Label label = new Label("Update item ID: " + entity.getId());
        Button updateBtn = new Button("Update");
        Button cancelBtn = new Button("Cancel");
        
        updateBtn.getStyleClass().add("secondary-button");
        cancelBtn.getStyleClass().add("primary-button");
        
        updateBtn.setOnAction(e -> {
            Entity updatedEntity = mainView.onEntityAdd(presenter.getCurrentUser().getName());
            if (updatedEntity != null) {
                presenter.executeCommand("update", List.of(entity.getId(), updatedEntity), false);
                refreshTable();
            }
            modal.close();
        });
        
        cancelBtn.setOnAction(e -> modal.close());
        
        root.getChildren().addAll(label, updateBtn, cancelBtn);
        Scene scene = new Scene(root, 300, 150);
        modal.setTitle("Update Item");
        modal.setScene(scene);
        modal.show();
    }

    public void setData(List<Entity> entities) {
        data.clear();
        data.addAll(entities);
    }
} 