package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presenter.Presenter;
import view.gui.GraphicsView;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label errorLabel;
    
    private GraphicsView mainView;
    private Presenter presenter;
    private Stage stage;

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
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill all fields");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        if (presenter.register(new common.models.User(username, password))) {
            stage.close();
            mainView.showAuthWindow();
        } else {
            errorLabel.setText("Registration failed");
        }
    }

    @FXML
    private void handleBack() {
        stage.close();
        mainView.showAuthWindow();
    }
}