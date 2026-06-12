package view.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import common.models.Entity;
import common.models.Route;
import util.access.ClientAccess;
import util.i18n.I18nManager;
import view.gui.GraphicsView;
import view.gui.dialogs.EntityInfoDialog;
import view.gui.dialogs.RouteFormDialog;

public class VisualisationController {
    private static final Color[] PALETTE = {
        Color.web("#ff6b6b"),
        Color.web("#4dabf7"),
        Color.web("#51cf66"),
        Color.web("#fcc419"),
        Color.web("#cc5de8"),
        Color.web("#20c997"),
        Color.web("#ff922b")
    };

    @FXML private StackPane canvasPane;
    @FXML private Canvas canvas;
    @FXML private Button backButton;
    @FXML private Label titleLabel;

    private GraphicsView mainView;
    private Stage stage;
    private final Map<Integer, RouteShape> shapes = new HashMap<>();
    private List<Entity> currentEntities = List.of();
    private Integer hoveredId;
    private Integer selectedId;

    public void setMainView(GraphicsView mainView) {
        this.mainView = mainView;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnShown(
                e -> Platform.runLater(() -> renderFromStore()));
    }

    public void bindTexts() {
        I18nManager i18n = I18nManager.get();
        titleLabel.setText(i18n.get("vis.title"));
        backButton.setText(i18n.get("vis.back"));
    }

    @FXML
    private void initialize() {
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
        canvasPane.widthProperty().addListener((obs, o, n) -> render(currentEntities));
        canvasPane.heightProperty().addListener((obs, o, n) -> render(currentEntities));
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleCanvasClick);
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleCanvasHover);
    }

    @FXML
    private void handleBack() {
        stage.close();
    }

    public void render(List<Entity> entities) {
        currentEntities = entities != null ? entities : List.of();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (w <= 1 || h <= 1) {
            return;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.web("#0d1016"));
        gc.fillRect(0, 0, w, h);
        drawGrid(gc, w, h);
        shapes.clear();

        Map<String, Color> authorColors = new LinkedHashMap<>();
        int[] paletteIndex = {0};

        double padding = 36;
        double plotW = w - padding * 2;
        double plotH = h - padding * 2 - 36;

        for (Entity entity : currentEntities) {
            if (!(entity instanceof Route route) || route.getCoordinates() == null) {
                continue;
            }

            Color color =
                    authorColors.computeIfAbsent(
                            route.getAuthor(),
                            author -> PALETTE[paletteIndex[0]++ % PALETTE.length]);

            double x = padding + normalize(route.getCoordinates().getX(), -1000, 1000) * plotW;
            double y = padding + normalize(route.getCoordinates().getY(), 0, 200) * plotH;
            double size = 14 + normalize(route.getDistance(), 1, 500) * 36;

            RouteShape shape = new RouteShape(route, x, y, size, color);
            shapes.put(route.getId(), shape);

            boolean hovered = Objects.equals(hoveredId, route.getId());
            boolean selected = Objects.equals(selectedId, route.getId());
            drawShape(gc, shape, hovered, selected);
        }

        if (authorColors.isEmpty()) {
            gc.setFill(Color.web("#9aa5b5"));
            gc.fillText(I18nManager.get().get("table.empty"), w / 2 - 80, h / 2);
        } else {
            drawLegend(gc, authorColors, w, h);
        }
    }

    private void renderFromStore() {
        if (mainView != null) {
            render(mainView.getCollectionStore().getMaster());
        }
    }

    private void drawGrid(GraphicsContext gc, double w, double h) {
        gc.setStroke(Color.web("#252b36"));
        gc.setLineWidth(1);
        int steps = 8;
        for (int i = 1; i < steps; i++) {
            double x = w * i / steps;
            double y = h * i / steps;
            gc.strokeLine(x, 0, x, h);
            gc.strokeLine(0, y, w, y);
        }
    }

    private void drawShape(GraphicsContext gc, RouteShape shape, boolean hovered, boolean selected) {
        double half = shape.size() / 2;
        double cx = shape.x();
        double cy = shape.y();
        double size = shape.size();

        gc.setFill(shape.color());
        gc.fillOval(cx - half, cy - half, size, size);

        if (selected) {
            gc.setStroke(Color.web("#ffd43b"));
            gc.setLineWidth(3);
            gc.strokeOval(cx - half - 5, cy - half - 5, size + 10, size + 10);
        } else if (hovered) {
            gc.setStroke(Color.web("#74c0fc"));
            gc.setLineWidth(2);
            gc.strokeOval(cx - half - 3, cy - half - 3, size + 6, size + 6);
        } else {
            gc.setStroke(Color.web("#ffffff", 0.35));
            gc.setLineWidth(1);
            gc.strokeOval(cx - half, cy - half, size, size);
        }
    }

    private void drawLegend(GraphicsContext gc, Map<String, Color> authorColors, double w, double h) {
        gc.setFill(Color.web("#b8c4d4"));
        gc.fillText(I18nManager.get().get("vis.legend"), 12, h - 18);

        double x = 80;
        for (Map.Entry<String, Color> entry : authorColors.entrySet()) {
            gc.setFill(entry.getValue());
            gc.fillOval(x, h - 24, 10, 10);
            gc.setFill(Color.web("#b8c4d4"));
            gc.fillText(entry.getKey(), x + 14, h - 15);
            x += entry.getKey().length() * 7 + 36;
            if (x > w - 60) {
                break;
            }
        }
    }

    private void handleCanvasHover(MouseEvent event) {
        Point2D point = new Point2D(event.getX(), event.getY());
        Integer newHover =
                shapes.entrySet().stream()
                        .filter(e -> e.getValue().contains(point))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);

        if (!Objects.equals(hoveredId, newHover)) {
            hoveredId = newHover;
            canvas.setCursor(
                    newHover != null ? javafx.scene.Cursor.HAND : javafx.scene.Cursor.DEFAULT);
            render(currentEntities);
        }
    }

    private void handleCanvasClick(MouseEvent event) {
        Point2D click = new Point2D(event.getX(), event.getY());
        Optional<RouteShape> hit =
                shapes.values().stream().filter(s -> s.contains(click)).findFirst();

        if (hit.isPresent()) {
            selectedId = hit.get().route().getId();
            render(currentEntities);
            showEntityInfo(hit.get());
        } else {
            selectedId = null;
            render(currentEntities);
        }
    }

    private void showEntityInfo(RouteShape shape) {
        Route route = shape.route();
        boolean canEdit = ClientAccess.canModify(route, mainView.getCurrentUser());

        EntityInfoDialog.show(
                stage,
                route,
                canEdit,
                editable ->
                        RouteFormDialog.show(stage, editable.getAuthor(), editable)
                                .ifPresent(
                                        updated -> {
                                            mainView
                                                    .getPresenter()
                                                    .executeCommand(
                                                            "update",
                                                            List.of(updated, editable.getId()),
                                                            false);
                                            mainView.refreshCollectionView();
                                        }));
    }

    private double normalize(double value, double min, double max) {
        if (max <= min) {
            return 0.5;
        }
        double clamped = Math.max(min, Math.min(max, value));
        return (clamped - min) / (max - min);
    }

    private record RouteShape(Route route, double x, double y, double size, Color color) {
        boolean contains(Point2D point) {
            double dx = point.getX() - x;
            double dy = point.getY() - y;
            return dx * dx + dy * dy <= (size / 2 + 4) * (size / 2 + 4);
        }
    }
}
