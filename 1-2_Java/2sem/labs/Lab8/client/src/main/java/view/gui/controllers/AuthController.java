package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presenter.Presenter;
import view.gui.GraphicsView;

public class AuthController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
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
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }
        
        if (presenter.onLogin(new common.models.User(username, password))) {
            mainView.showHomeWindow();
        } else {
            errorLabel.setText("Invalid credentials");
        }
    }

    @FXML
    private void handleRegister() {
        mainView.showRegisterWindow();
    }
}