package com.codecool.quest;

import com.codecool.quest.logic.Cell;
import com.codecool.quest.logic.CellType;
import com.codecool.quest.logic.GameMap;
import com.codecool.quest.logic.MapLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    private final int screenWidth = 25;
    private final int screenHeight = 20;
    List<String> mapList = new ArrayList<>(){{
//        add("/map3.txt");
        add("/map.txt");
        add("/map2.txt");
    }};
    GameMap map = MapLoader.loadMap(mapList.remove(0));
    Canvas canvas = new Canvas(
            screenWidth * Tiles.TILE_WIDTH,
            screenHeight * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnMouseClicked(this::mouseEvent);

        primaryStage.setTitle("Codecool Quest");
        primaryStage.show();
    }

    private void mouseEvent(MouseEvent mouseEvent) {
        System.out.println("Hello");
//        System.out.println(cell.getTileName());
//        if (cell.getType() == )
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        Cell cell = new Cell(map, map.getPlayer().getX(), map.getPlayer().getY());
        int moveX = 0;
        int moveY = 0;
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(moveX, --moveY);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(moveX, ++moveY);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(--moveX, moveY);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(++moveX,moveY);
                refresh();
                break;
            case F:
                map.kill();
                refresh();
                break;
            case SPACE:
                map.shot();
                refresh();
                break;
        }
        if (cell.getNeighbor(moveX, moveY).getType().equals(CellType.STAIRSDOWN)){
            map = MapLoader.loadMap(mapList.remove(0));
            refresh();
        }
    }

    private void refresh() {
        int mapShiftX = map.getPlayer().getX();
        int mapShiftY = map.getPlayer().getY();
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        map.moveMonsters();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), 10+x-mapShiftX, 10+y-mapShiftY);
                }
                else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), 10+x-mapShiftX, 10+y-mapShiftY);
                }
                else {
                    Tiles.drawTile(context, cell, 10+x-mapShiftX, 10+y-mapShiftY);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
    }
}
