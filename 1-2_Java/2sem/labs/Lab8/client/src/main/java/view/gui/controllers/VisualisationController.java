package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import view.gui.GraphicsView;

public class VisualisationController {
    private GraphicsView mainView;
    private Stage stage;

    public void setMainView(GraphicsView mainView) {
        this.mainView = mainView;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleBack() {
        stage.close();
        try {
            mainView.showHomeWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}