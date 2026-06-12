package view.gui;

import java.util.List;
import common.models.Entity;
import common.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presenter.Presenter;
import presenter.PresenterFactory;
import util.forms.buidler.GraphicsFormBuilder;
import view.View;
import view.gui.controllers.AuthController;
import view.gui.controllers.HomeController;
import view.gui.controllers.RegisterController;
import view.gui.controllers.VisualisationController;

public class GraphicsView extends Application implements View {
    private Presenter presenter;
    private GraphicsFormBuilder formBuidler;
    private Stage primaryStage;
    private HomeController homeController;

    public void onCreate() {
        launch();
    }

    public void onDestroy() {
        displayMessage("Bye!");
        this.presenter.disconnect();
    }

    public void onLogOut() {
        displayMessage("Logged out");
        this.presenter.logOut();
    }

    public void displayError(String errorMessage) {
        System.err.println(errorMessage);
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public Entity onEntityAdd(String author) {
        return formBuidler.buildEntity(author);
    }

    public User onRegister() {
        return formBuidler.buildUser(true);
    }

    public User onLogin() {
        return formBuidler.buildUser(false);
    }

    public User getCurrentUser() {
        return presenter.getCurrentUser();
    }

    public void executeCommand(String commandName, List<?> args, boolean fileMode) {
        this.presenter.executeCommand(commandName, args, fileMode);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.presenter = PresenterFactory.providePresenter(this);
        this.formBuidler = new GraphicsFormBuilder();
        showAuthWindow();
    }

    public void showAuthWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth.fxml"));
        Parent root = loader.load();
        
        AuthController controller = loader.getController();
        controller.setMainView(this);
        controller.setPresenter(presenter);
        controller.setStage(primaryStage);
        
        Scene scene = new Scene(root, 350, 400);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showRegisterWindow() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
        Parent root = loader.load();
        
        RegisterController controller = loader.getController();
        controller.setMainView(this);
        controller.setPresenter(presenter);
        controller.setStage(stage);
        
        Scene scene = new Scene(root, 350, 450);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }

    public void showHomeWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
        Parent root = loader.load();
        
        homeController = loader.getController();
        homeController.setMainView(this);
        homeController.setPresenter(presenter);
        homeController.setStage(primaryStage);
        homeController.refreshTable();
        
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showVisualisationWindow() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visualisation.fxml"));
        Parent root = loader.load();
        
        VisualisationController controller = loader.getController();
        controller.setMainView(this);
        controller.setStage(stage);
        
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Visualisation");
        stage.setScene(scene);
        stage.show();
    }

    public void refreshHomeTable() {
        if (homeController != null) {
            homeController.refreshTable();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}