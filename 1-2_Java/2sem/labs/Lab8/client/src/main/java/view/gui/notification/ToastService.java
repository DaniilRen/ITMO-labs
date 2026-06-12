package view.gui.notification;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.i18n.I18nManager;

public final class ToastService {
  public enum Type {
    ERROR("toast-error"),
    SUCCESS("toast-success"),
    INFO("toast-info");

    private final String styleClass;

    Type(String styleClass) {
      this.styleClass = styleClass;
    }

    String styleClass() {
      return styleClass;
    }
  }

  private static Popup activePopup;
  private static Timeline autoHideTimeline;

  private ToastService() {}

  public static void show(Stage stage, String message, Type type) {
    if (stage == null || message == null || message.isBlank()) {
      return;
    }
    Platform.runLater(() -> showPopup(stage, message, type));
  }

  public static void showError(Stage stage, String message) {
    show(stage, message, Type.ERROR);
  }

  public static void showSuccess(Stage stage, String message) {
    show(stage, message, Type.SUCCESS);
  }

  public static void showInfo(Stage stage, String message) {
    show(stage, message, Type.INFO);
  }

  private static void showPopup(Stage stage, String message, Type type) {
    dismissActive();

    Label label = new Label(message);
    label.setWrapText(true);
    label.setMaxWidth(300);
    label.getStyleClass().add("toast-label");
    HBox.setHgrow(label, Priority.ALWAYS);

    Button okBtn = new Button(I18nManager.get().get("button.ok"));
    okBtn.getStyleClass().add("toast-ok-button");
    okBtn.setOnAction(e -> dismissActive());

    HBox content = new HBox(10, label, okBtn);
    content.setAlignment(Pos.CENTER_LEFT);
    content.setPadding(new Insets(10, 12, 10, 14));
    content.setMaxWidth(380);
    content.setMaxHeight(Region.USE_PREF_SIZE);
    content.getStyleClass().addAll("toast", type.styleClass());
    var stylesheet = ToastService.class.getResource("/css/style.css");
    if (stylesheet != null) {
      content.getStylesheets().add(stylesheet.toExternalForm());
    }

    Popup popup = new Popup();
    popup.setAutoHide(false);
    popup.getContent().add(content);
    activePopup = popup;

    content.setOpacity(0);
    content.setTranslateY(-12);

    Runnable positionPopup =
        () -> {
          if (!stage.isShowing()) {
            return;
          }
          content.applyCss();
          double width = content.prefWidth(-1);
          double height = content.prefHeight(width);

          double stageX = stage.getX();
          double stageY = stage.getY();
          double stageW = stage.getWidth();
          double x = stageX + Math.max(12, (stageW - width) / 2);
          double y = stageY + 14;

          Rectangle2D screen = Screen.getPrimary().getVisualBounds();
          x = Math.max(screen.getMinX() + 8, Math.min(x, screen.getMaxX() - width - 8));
          y = Math.max(screen.getMinY() + 8, y);

          if (popup.isShowing()) {
            popup.setX(x);
            popup.setY(y);
          } else {
            popup.show(stage, x, y);
          }
        };

    positionPopup.run();

    Timeline slideIn =
        new Timeline(
            new KeyFrame(Duration.ZERO, e -> content.setOpacity(0)),
            new KeyFrame(Duration.millis(180), e -> content.setOpacity(1)),
            new KeyFrame(Duration.ZERO, e -> content.setTranslateY(-12)),
            new KeyFrame(Duration.millis(180), e -> content.setTranslateY(0)));
    slideIn.play();

    autoHideTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(2),
                e -> {
                  Timeline fadeOut =
                      new Timeline(
                          new KeyFrame(Duration.millis(160), ev -> content.setOpacity(0)),
                          new KeyFrame(Duration.millis(160), ev -> content.setTranslateY(-10)));
                  fadeOut.setOnFinished(ev -> dismissActive());
                  fadeOut.play();
                }));
    autoHideTimeline.play();
  }

  private static void dismissActive() {
    if (autoHideTimeline != null) {
      autoHideTimeline.stop();
      autoHideTimeline = null;
    }
    if (activePopup != null) {
      activePopup.hide();
      activePopup = null;
    }
  }
}
