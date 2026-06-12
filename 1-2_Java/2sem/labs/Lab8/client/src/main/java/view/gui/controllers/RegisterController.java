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

public class RegisterController {
    @FXML private Label titleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;
    @FXML private Button backButton;

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

    public void bindTexts() {
        I18nManager i18n = I18nManager.get();
        titleLabel.setText(i18n.get("register.title"));
        usernameField.setPromptText(i18n.get("auth.login"));
        passwordField.setPromptText(i18n.get("auth.password"));
        confirmField.setPromptText(i18n.get("register.confirm"));
        registerButton.setText(i18n.get("auth.register.button"));
        backButton.setText(i18n.get("register.back"));
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmField.getText();
        I18nManager i18n = I18nManager.get();

        if (username.isEmpty() || password.isEmpty()) {
            ToastService.showError(stage, i18n.get("register.error.empty"));
            return;
        }

        if (!password.equals(confirm)) {
            ToastService.showError(stage, i18n.get("register.error.mismatch"));
            return;
        }

        if (presenter.register(new common.models.User(username, password, false))) {
            ToastService.showSuccess(stage, i18n.get("register.success"));
            stage.close();
        } else {
            ToastService.showError(stage, i18n.get("register.error.failed"));
        }
    }

    @FXML
    private void handleBack() {
        stage.close();
    }
}
