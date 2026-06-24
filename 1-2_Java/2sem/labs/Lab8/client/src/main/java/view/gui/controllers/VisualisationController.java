package view.gui.controllers;

import common.models.Entity;
import common.models.LocationFrom;
import common.models.LocationTo;
import common.models.Route;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.i18n.I18nManager;
import view.gui.GraphicsView;

import java.util.*;

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

    private static final double HANDLE_RADIUS = 10;

    @FXML private StackPane canvasPane;
    @FXML private Canvas canvas;
    @FXML private Button backButton;
    @FXML private Label titleLabel;

    private GraphicsView mainView;
    private Stage stage;

    private final Map<Integer, RouteVector> vectors = new HashMap<>();
    private final List<RouteVector> layoutVectors = new ArrayList<>();

    private final Map<Integer, Double> appearProgress = new HashMap<>();
    private final Map<String, Color> authorColors = new LinkedHashMap<>();

    private final Map<Integer, Route> locallyModifiedRoutes = new HashMap<>();

    private List<Entity> currentEntities = List.of();

    private Timeline appearTimeline;

    private Integer hoveredId;
    private Integer selectedId;

    private DragTarget dragTarget = DragTarget.NONE;
    private RouteVector draggedVector;

    private enum DragTarget {
        NONE,
        START,
        END
    }

    public void setMainView(GraphicsView mainView) {
        this.mainView = mainView;
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnShown(e -> Platform.runLater(this::renderFromStore));

        stage.setOnHidden(e -> {
            stopTimeline();
        });
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

        canvasPane.widthProperty().addListener((obs, o, n) -> redraw());
        canvasPane.heightProperty().addListener((obs, o, n) -> redraw());

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleHover);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);
    }

    @FXML
    private void handleBack() {
        stopTimeline();
        stage.close();
    }

    public void render(List<Entity> entities) {

    List<Entity> merged = new ArrayList<>();

    for (Entity entity : entities) {

        if (entity instanceof Route route) {

            Route local =
                    locallyModifiedRoutes.get(route.getId());

            if (local != null) {
                merged.add(local);
            } else {
                merged.add(route);
            }

        } else {
            merged.add(entity);
        }
    }

    currentEntities = merged;

    rebuildLayout();

    startAnimation();

    drawFrame();
}

    private void renderFromStore() {
        if (mainView != null) {
            render(mainView.getCollectionStore().getMaster());
        }
    }

    private void rebuildLayout() {

        vectors.clear();
        layoutVectors.clear();

        double w = canvas.getWidth();
        double h = canvas.getHeight();

        if (w <= 1 || h <= 1) {
            return;
        }

        int[] paletteIndex = {0};

        for (Entity entity : currentEntities) {

            if (!(entity instanceof Route route)) {
                continue;
            }

            if (route.getLocationFrom() == null || route.getLocationTo() == null) {
                continue;
            }

            Color color =
                    authorColors.computeIfAbsent(
                            route.getAuthor(),
                            a -> PALETTE[paletteIndex[0]++ % PALETTE.length]);

            double x1 = worldToScreenX(route.getLocationFrom().getX());
            double y1 = worldToScreenY(route.getLocationFrom().getY());

            double x2 = worldToScreenX(route.getLocationTo().getX());
            double y2 = worldToScreenY(route.getLocationTo().getY());

            RouteVector vector =
                    new RouteVector(
                            route,
                            x1,
                            y1,
                            x2,
                            y2,
                            color
                    );

            layoutVectors.add(vector);
            vectors.put(route.getId(), vector);

            appearProgress.putIfAbsent(route.getId(), 1.0);
        }
    }

    private void redraw() {
        rebuildLayout();
        drawFrame();
    }

    private void startAnimation() {

        stopTimeline();

        for (RouteVector vector : layoutVectors) {
            appearProgress.put(vector.route().getId(), 0.0);
        }

        appearTimeline =
                new Timeline(
                        new KeyFrame(
                                Duration.millis(30),
                                e -> {

                                    boolean done = true;

                                    for (RouteVector vector : layoutVectors) {

                                        int id = vector.route().getId();

                                        double progress =
                                                appearProgress.getOrDefault(id, 1.0);

                                        if (progress < 1.0) {

                                            progress += 0.08;

                                            if (progress > 1.0) {
                                                progress = 1.0;
                                            }

                                            appearProgress.put(id, progress);

                                            done = false;
                                        }
                                    }

                                    drawFrame();

                                    if (done) {
                                        stopTimeline();
                                    }
                                }));

        appearTimeline.setCycleCount(Animation.INDEFINITE);
        appearTimeline.play();
    }

    private void stopTimeline() {

        if (appearTimeline != null) {
            appearTimeline.stop();
            appearTimeline = null;
        }
    }

    private void drawFrame() {

        double w = canvas.getWidth();
        double h = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, w, h);

        gc.setFill(Color.web("#0d1016"));
        gc.fillRect(0, 0, w, h);

        drawGrid(gc, w, h);
				drawLegend(gc, w);

        for (RouteVector vector : layoutVectors) {

            double progress =
                    appearProgress.getOrDefault(
                            vector.route().getId(),
                            1.0
                    );

            drawVector(
                    gc,
                    vector,
                    progress,
                    Objects.equals(hoveredId, vector.route().getId()),
                    Objects.equals(selectedId, vector.route().getId())
            );
        }
    }

    private void drawGrid(GraphicsContext gc, double w, double h) {

        gc.setStroke(Color.web("#252b36"));

        for (int i = 0; i < 10; i++) {

            double x = w * i / 10.0;
            double y = h * i / 10.0;

            gc.strokeLine(x, 0, x, h);
            gc.strokeLine(0, y, w, y);
        }
    }

    private void drawVector(
        GraphicsContext gc,
        RouteVector vector,
        double progress,
        boolean hovered,
        boolean selected
) {

    double x1 = vector.startX();
    double y1 = vector.startY();

    double x2 = vector.endX();
    double y2 = vector.endY();

    double px = x1 + (x2 - x1) * progress;
    double py = y1 + (y2 - y1) * progress;

    Color base = vector.color();

    gc.setStroke(base.deriveColor(0, 1, 1, 0.18));
    gc.setLineWidth(selected ? 12 : 8);
    gc.strokeLine(x1, y1, px, py);

    gc.setStroke(base);

    gc.setLineWidth(selected ? 4 : 2.5);

    gc.strokeLine(x1, y1, px, py);


    drawArrow(gc, x1, y1, px, py);

    drawHandle(
            gc,
            x1,
            y1,
            hovered || dragTarget == DragTarget.START,
            base
    );
    drawHandle(
            gc,
            x2,
            y2,
            hovered || dragTarget == DragTarget.END,
            base
    );

    gc.setFill(Color.WHITE);
    gc.fillText(
            vector.route().getName(),
            (x1 + x2) / 2 + 8,
            (y1 + y2) / 2 - 8
    );
}

private void drawArrow(
        GraphicsContext gc,
        double x1,
        double y1,
        double x2,
        double y2
) {

    double angle = Math.atan2(y2 - y1, x2 - x1);

    double len = 18;

    double ax1 = x2 - len * Math.cos(angle - Math.PI / 7);
    double ay1 = y2 - len * Math.sin(angle - Math.PI / 7);

    double ax2 = x2 - len * Math.cos(angle + Math.PI / 7);
    double ay2 = y2 - len * Math.sin(angle + Math.PI / 7);

    gc.setLineWidth(3);

    gc.strokeLine(x2, y2, ax1, ay1);
    gc.strokeLine(x2, y2, ax2, ay2);
}

private void drawHandle(
        GraphicsContext gc,
        double x,
        double y,
        boolean active,
        Color color
) {

    double r = active ? 14 : 10;

    /*
     * Outer glow
     */

    gc.setFill(color.deriveColor(0, 1, 1, 0.25));

    gc.fillOval(
            x - r,
            y - r,
            r * 2,
            r * 2
    );

    /*
     * Main circle
     */

    gc.setFill(color);

    gc.fillOval(
            x - r / 2,
            y - r / 2,
            r,
            r
    );

    /*
     * Inner highlight
     */

    gc.setFill(Color.WHITE);

    gc.fillOval(
            x - r / 6,
            y - r / 6,
            r / 3,
            r / 3
    );
}

    private void handleHover(MouseEvent e) {

        hoveredId = null;

        for (RouteVector vector : layoutVectors) {

            if (isNear(e.getX(), e.getY(), vector.startX(), vector.startY())
                    || isNear(e.getX(), e.getY(), vector.endX(), vector.endY())) {

                hoveredId = vector.route().getId();

                canvas.setCursor(Cursor.HAND);

                drawFrame();

                return;
            }
        }

        canvas.setCursor(Cursor.DEFAULT);

        drawFrame();
    }

    private void handleMousePressed(MouseEvent e) {

        for (RouteVector vector : layoutVectors) {

            if (isNear(e.getX(), e.getY(), vector.startX(), vector.startY())) {

                draggedVector = vector;
                dragTarget = DragTarget.START;

                return;
            }

            if (isNear(e.getX(), e.getY(), vector.endX(), vector.endY())) {

                draggedVector = vector;
                dragTarget = DragTarget.END;

                return;
            }
        }
    }

    private void handleMouseDragged(MouseEvent e) {

        if (draggedVector == null) {
            return;
        }

        Route route = draggedVector.route();

        double worldX = screenToWorldX(e.getX());
        double worldY = screenToWorldY(e.getY());

        Route updated;

        if (dragTarget == DragTarget.START) {

            LocationFrom newFrom =
                    new LocationFrom(
                            (int) worldX,
                            (double) worldY,
                            route.getLocationFrom().getName()
                    );

            updated =
                    new Route(
                            route.getId(),
                            route.getName(),
                            route.getCoordinates(),
                            route.getCreationDate(),
                            newFrom,
                            route.getLocationTo(),
                            route.getDistance(),
                            route.getAuthor()
                    );

        } else {

            LocationTo newTo =
                    new LocationTo(
                            worldX,
                            worldY,
                            route.getLocationTo().getZ(),
                            route.getLocationTo().getName()
                    );

            updated =
                    new Route(
                            route.getId(),
                            route.getName(),
                            route.getCoordinates(),
                            route.getCreationDate(),
                            route.getLocationFrom(),
                            newTo,
                            route.getDistance(),
                            route.getAuthor()
                    );
        }

        locallyModifiedRoutes.put(updated.getId(), updated);

        replaceRoute(updated);

        drawFrame();
    }

    private void replaceRoute(Route route) {

        for (int i = 0; i < currentEntities.size(); i++) {

            Entity entity = currentEntities.get(i);

            if (entity instanceof Route r && r.getId() == route.getId()) {

                List<Entity> copy = new ArrayList<>(currentEntities);

                copy.set(i, route);

                currentEntities = copy;

                rebuildLayout();

                return;
            }
        }
    }

    private void handleMouseReleased(MouseEvent e) {

        draggedVector = null;
        dragTarget = DragTarget.NONE;
    }

	@FXML
	private void handleSyncObjects() {

			for (Route route : locallyModifiedRoutes.values()) {

					mainView
									.getPresenter()
									.executeCommand(
													"update",
													List.of(route, route.getId()),
													false
									);
			}

			mainView.refreshCollectionView();

			Timeline clearTimer =
							new Timeline(
											new KeyFrame(
															Duration.seconds(1),
															e -> locallyModifiedRoutes.clear()
											)
							);

			clearTimer.play();
	}

    private boolean isNear(
            double mx,
            double my,
            double x,
            double y
    ) {

        double dx = mx - x;
        double dy = my - y;

        return dx * dx + dy * dy <= HANDLE_RADIUS * HANDLE_RADIUS;
    }

    private double worldToScreenX(double x) {

        double w = canvas.getWidth();

        return ((x + 1000.0) / 2000.0) * w;
    }

    private double worldToScreenY(double y) {

        double h = canvas.getHeight();

        return h - ((y + 1000.0) / 2000.0) * h;
    }

    private double screenToWorldX(double x) {

        double w = canvas.getWidth();

        return (x / w) * 2000.0 - 1000.0;
    }

    private double screenToWorldY(double y) {

        double h = canvas.getHeight();

        return -((y / h) * 2000.0 - 1000.0);
    }

    private record RouteVector(
            Route route,
            double startX,
            double startY,
            double endX,
            double endY,
            Color color
    ) {}

		private void drawLegend(GraphicsContext gc, double canvasWidth) {

    double x = canvasWidth - 190;
    double y = 30;

    gc.setFill(Color.rgb(20, 20, 25, 0.82));

    gc.fillRoundRect(
            x - 20,
            y - 20,
            180,
            authorColors.size() * 28 + 30,
            14,
            14
    );

    gc.setStroke(Color.web("#495057"));

    gc.strokeRoundRect(
            x - 20,
            y - 20,
            180,
            authorColors.size() * 28 + 30,
            14,
            14
    );

    gc.setFill(Color.WHITE);

    gc.fillText("Владелец:", x, y);

    y += 24;

    for (Map.Entry<String, Color> entry : authorColors.entrySet()) {

        gc.setFill(entry.getValue());

        gc.fillOval(x, y - 8, 12, 12);

        gc.setFill(Color.WHITE);

        gc.fillText(entry.getKey(), x + 22, y + 2);

        y += 24;
    }
}
}