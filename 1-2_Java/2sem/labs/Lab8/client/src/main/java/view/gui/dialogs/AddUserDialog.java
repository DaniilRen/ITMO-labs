package view.gui.dialogs;

import java.util.function.Consumer;

import common.models.User;
import common.transfer.Status;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import presenter.Presenter;
import util.i18n.I18nManager;
import view.gui.GraphicsView;

public final class AddUserDialog {
  private AddUserDialog() {}

  public static void show(Stage owner, Presenter presenter, GraphicsView mainView) {
    Stage stage = new Stage();
    I18nManager i18n = I18nManager.get();

    Label title = new Label(i18n.get("dialog.add.user"));
    title.getStyleClass().add("title-label");

    TextField usernameField = new TextField();
    usernameField.setPromptText(i18n.get("auth.login"));
    usernameField.getStyleClass().add("text-field");
    usernameField.setMaxWidth(280);

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText(i18n.get("auth.password"));
    passwordField.getStyleClass().add("password-field");
    passwordField.setMaxWidth(280);

    CheckBox adminBox = new CheckBox(i18n.get("dialog.admin.rights"));
    adminBox.getStyleClass().add("check-box");

    Label errorBox = new Label(i18n.get("error.output"));
    errorBox.getStyleClass().add("dialog-error-box");
    errorBox.setWrapText(true);
    errorBox.setMaxWidth(320);

    Consumer<String> showError = msg -> errorBox.setText(msg);

    Button cancelBtn = new Button(i18n.get("button.cancel"));
    cancelBtn.getStyleClass().add("secondary-dark-button");
    Button addBtn = new Button(i18n.get("button.add"));
    addBtn.getStyleClass().add("accent-button");

    cancelBtn.setOnAction(e -> stage.close());

    addBtn.setOnAction(
        e -> {
          String username = usernameField.getText().trim();
          String password = passwordField.getText();
          if (username.isEmpty() || password.isEmpty()) {
            showError.accept(i18n.get("register.error.empty"));
            return;
          }
          User user = new User(username, password, adminBox.isSelected());
          mainView.setPendingAuthUser(user);
          Status status = presenter.executeCommandWithStatus("register", java.util.List.of(), false);
          if (status == Status.LOGIN || status == Status.OK) {
            stage.close();
            view.gui.notification.ToastService.showSuccess(owner, i18n.get("dialog.add.user.success"));
          } else {
            showError.accept(mainView.getLastError());
          }
        });

    HBox buttons = new HBox(12, cancelBtn, addBtn);
    buttons.setAlignment(Pos.CENTER);

    VBox root =
        new VBox(16, title, usernameField, passwordField, adminBox, buttons, errorBox);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(28));

    DialogStyles.setup(stage, root, 400, 420, owner);
    stage.setTitle(i18n.get("dialog.add.user"));
    stage.showAndWait();
  }
}
