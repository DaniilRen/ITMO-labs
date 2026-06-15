package view.gui;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import common.models.Entity;
import common.models.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.local.CollectionStore;
import presenter.Presenter;
import presenter.PresenterFactory;
import util.forms.buidler.GraphicsFormBuilder;
import util.i18n.I18nManager;
import view.View;
import view.gui.notification.ToastService;
import view.gui.controllers.AuthController;
import view.gui.controllers.HomeController;
import view.gui.controllers.RegisterController;
import view.gui.controllers.VisualisationController;

public class GraphicsView extends Application implements View {
    private static final String HOST = "localhost";
    private static final int PORT = 9000;

    private Presenter presenter;
    private GraphicsFormBuilder formBuilder;
    private Stage primaryStage;
    private HomeController homeController;
    private VisualisationController visualisationController;
    private AuthController authController;
    private final CollectionStore collectionStore = new CollectionStore();
    private User pendingAuthUser;
    private String lastError = "";
    private int scriptQuietDepth;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void onCreate() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.presenter = PresenterFactory.providePresenter(this);
        this.formBuilder = new GraphicsFormBuilder();
        formBuilder.setOwnerStage(primaryStage);

        I18nManager.get().addListener(locale -> Platform.runLater(this::reloadTexts));
        presenter.connect(HOST, PORT);
        scheduler.scheduleAtFixedRate(
                () -> {
                    if (presenter.isAuthenticated()) {
                        Platform.runLater(this::refreshCollectionView);
                    }
                },
                5,
                5,
                TimeUnit.SECONDS);
        showAuthWindow();
    }

    @Override
    public void stop() {
        scheduler.shutdownNow();
        onDestroy();
    }

    public void onDestroy() {
        presenter.disconnect();
    }

    @Override
    public void onLogOut() {
        try {
            showAuthWindow();
        } catch (Exception e) {
            displayError(e.getMessage());
        }
    }

    @Override
    public void displayError(String errorMessage) {
        lastError = errorMessage;
        if (scriptQuietDepth > 0) {
            System.err.println(errorMessage);
            return;
        }
        Platform.runLater(
                () -> {
                    ToastService.showError(primaryStage, errorMessage);
                    System.err.println(errorMessage);
                });
    }

    @Override
    public void setScriptQuietMode(boolean quiet) {
        if (quiet) {
            scriptQuietDepth++;
        } else if (scriptQuietDepth > 0) {
            scriptQuietDepth--;
        }
    }

    public String getLastError() {
        return lastError;
    }

    public void clearLastError() {
        lastError = "";
    }

    public void setLastError(String errorMessage) {
        lastError = errorMessage;
        System.err.println(errorMessage);
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public Entity onEntityAdd(String author) {
        return formBuilder.buildEntity(author);
    }

    @Override
    public User onLogin() {
        return pendingAuthUser;
    }

    @Override
    public User onRegister() {
        return pendingAuthUser;
    }

    @Override
    public void setPendingAuthUser(User user) {
        this.pendingAuthUser = user;
    }

    @Override
    public User getCurrentUser() {
        return presenter.getCurrentUser();
    }

    @Override
    public void executeCommand(String commandName, List<?> args, boolean fileMode) {
        presenter.executeCommand(commandName, args, fileMode);
    }

    @Override
    public void onCollectionReceived(List<Entity> entities) {
        Platform.runLater(
                () -> {
                    collectionStore.setAll(entities);
                    if (homeController != null) {
                        homeController.applyCollection();
                    }
                    if (visualisationController != null) {
                        visualisationController.render(collectionStore.getMaster());
                    }
                });
    }

    @Override
    public void showTextDialog(String title, String content) {
        if (scriptQuietDepth > 0) {
            System.out.println(resolveDialogTitle(title) + ":\n" + content);
            return;
        }
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
                    alert.setTitle(I18nManager.get().get("dialog.result"));
                    alert.setHeaderText(resolveDialogTitle(title));
                    alert.showAndWait();
                });
    }

    @Override
    public void refreshCollectionView() {
        if (presenter.isAuthenticated()) {
            presenter.refreshCollection();
        }
    }

    public CollectionStore getCollectionStore() {
        return collectionStore;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public GraphicsFormBuilder getFormBuilder() {
        return formBuilder;
    }

    public void showAuthWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gui/auth.fxml"));
        Parent root = loader.load();
        authController = loader.getController();
        authController.setMainView(this);
        authController.setPresenter(presenter);
        authController.setStage(primaryStage);
        authController.bindTexts();

        Scene scene = new Scene(root, 520, 420);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setTitle(I18nManager.get().get("auth.login.button"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showRegisterWindow() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gui/register.fxml"));
        Parent root = loader.load();
        RegisterController controller = loader.getController();
        controller.setMainView(this);
        controller.setPresenter(presenter);
        controller.setStage(stage);
        controller.bindTexts();

        Scene scene = new Scene(root, 420, 460);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle(I18nManager.get().get("register.title"));
        stage.setScene(scene);
        stage.show();
    }

    public void showHomeWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gui/home.fxml"));
        Parent root = loader.load();
        homeController = loader.getController();
        homeController.setMainView(this);
        homeController.setPresenter(presenter);
        homeController.setStage(primaryStage);
        homeController.initializeTable();
        homeController.bindTexts();
        presenter.refreshCollection();

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setTitle(I18nManager.get().get("main.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showVisualisationWindow() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gui/visualisation.fxml"));
        Parent root = loader.load();
        visualisationController = loader.getController();
        visualisationController.setMainView(this);
        visualisationController.setStage(stage);
        visualisationController.bindTexts();

        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle(I18nManager.get().get("vis.title"));
        stage.setScene(scene);
        stage.show();
    }

    public void cycleLanguage() {
        Locale[] locales = I18nManager.supportedLocales();
        Locale current = I18nManager.get().getLocale();
        int index = 0;
        for (int i = 0; i < locales.length; i++) {
            if (locales[i].equals(current)) {
                index = (i + 1) % locales.length;
                break;
            }
        }
        I18nManager.get().setLocale(locales[index]);
    }

    private void reloadTexts() {
        if (authController != null) {
            authController.bindTexts();
        }
        if (homeController != null) {
            homeController.bindTexts();
        }
        if (visualisationController != null) {
            visualisationController.bindTexts();
        }
    }

    private String resolveDialogTitle(String commandName) {
        return switch (commandName) {
            case "history" -> I18nManager.get().get("dialog.history");
            case "help" -> I18nManager.get().get("dialog.help");
            case "info" -> I18nManager.get().get("dialog.info");
            default -> I18nManager.get().get("dialog.result");
        };
    }
}
