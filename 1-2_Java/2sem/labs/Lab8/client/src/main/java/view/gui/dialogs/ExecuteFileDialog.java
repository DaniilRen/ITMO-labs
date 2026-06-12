package view.gui.dialogs;

import common.transfer.Status;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presenter.Presenter;
import util.i18n.I18nManager;
import view.gui.GraphicsView;

public final class ExecuteFileDialog {
  private ExecuteFileDialog() {}

  public static void show(Stage owner, Presenter presenter, GraphicsView mainView) {
    Stage stage = new Stage();
    if (owner != null) {
      stage.initOwner(owner);
    }
    stage.initModality(Modality.APPLICATION_MODAL);
    I18nManager i18n = I18nManager.get();

    Label title = new Label(i18n.get("dialog.execute.file.title"));
    title.getStyleClass().add("title-label");

    Label prompt = new Label(i18n.get("dialog.file.enter"));
    prompt.getStyleClass().add("field-label");

    TextField fileField = new TextField();
    fileField.setPromptText(i18n.get("dialog.file.name"));
    fileField.getStyleClass().add("text-field");
    fileField.setMaxWidth(320);

    Label errorBox = new Label(i18n.get("error.output"));
    errorBox.getStyleClass().add("dialog-error-box");
    errorBox.setWrapText(true);
    errorBox.setMaxWidth(340);
    errorBox.setMinHeight(48);

    Button cancelBtn = new Button(i18n.get("button.cancel"));
    cancelBtn.getStyleClass().add("secondary-dark-button");
    Button executeBtn = new Button(i18n.get("button.execute"));
    executeBtn.getStyleClass().add("accent-button");

    cancelBtn.setOnAction(e -> stage.close());

    executeBtn.setOnAction(
        e -> {
          String filename = fileField.getText().trim();
          if (filename.isEmpty()) {
            errorBox.setText(i18n.get("auth.error.empty"));
            return;
          }
          Status status =
              presenter.executeCommandWithStatus("execute_script", java.util.List.of(filename), true);
          if (status == Status.OK) {
            stage.close();
            presenter.refreshCollection();
            view.gui.notification.ToastService.showSuccess(
                owner, i18n.get("dialog.execute.file.success"));
          } else {
            String err = mainView.getLastError();
            errorBox.setText(err != null && !err.isBlank() ? err : i18n.get("error.unknown"));
          }
        });

    HBox buttons = new HBox(12, cancelBtn, executeBtn);
    buttons.setAlignment(Pos.CENTER);

    VBox root = new VBox(16, title, prompt, fileField, errorBox, buttons);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(28));
    root.getStyleClass().add("dialog-root");

    stage.setTitle(i18n.get("dialog.execute.file.title"));
    stage.setScene(new javafx.scene.Scene(root, 420, 320));
    stage
        .getScene()
        .getStylesheets()
        .add(ExecuteFileDialog.class.getResource("/css/style.css").toExternalForm());
    stage.showAndWait();
  }
}
