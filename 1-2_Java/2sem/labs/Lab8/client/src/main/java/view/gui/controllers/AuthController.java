package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presenter.Presenter;
import util.i18n.I18nManager;
import view.gui.GraphicsView;
import view.gui.notification.ToastService;

public class AuthController {
    @FXML private Label welcomeLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label serverStatusLabel;
    @FXML private Label syncStatusLabel;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button languageButton;

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
    private void initialize() {
        passwordField.setOnAction(e -> handleLogin());
        usernameField.setOnAction(e -> passwordField.requestFocus());
    }

    public void bindTexts() {
        I18nManager i18n = I18nManager.get();
        welcomeLabel.setText(i18n.get("app.welcome"));
        usernameField.setPromptText(i18n.get("auth.login"));
        passwordField.setPromptText(i18n.get("auth.password"));
        loginButton.setText(i18n.get("auth.login.button"));
        registerButton.setText(i18n.get("auth.register.button"));
        languageButton.setText("🌐");
    }

    private void showError(String message) {
        ToastService.showError(stage, message);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        I18nManager i18n = I18nManager.get();

        if (username.isEmpty() || password.isEmpty()) {
            showError(i18n.get("auth.error.empty"));
            return;
        }

        if (presenter.login(new common.models.User(username, password, false))) {
            try {
                mainView.showHomeWindow();
            } catch (Exception e) {
                showError(e.getMessage());
            }
        } else {
            showError(i18n.get("auth.error.invalid"));
        }
    }

    @FXML
    private void handleRegister() {
        try {
            mainView.showRegisterWindow();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleLanguage() {
        mainView.cycleLanguage();
        bindTexts();
    }
}
