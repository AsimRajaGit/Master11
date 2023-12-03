/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package pisd.dk.dtu.compute.se.roborally;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pisd.dk.dtu.compute.se.roborally.controller.AppController;
import pisd.dk.dtu.compute.se.roborally.view.BoardView;
import pisd.dk.dtu.compute.se.roborally.view.SpaceView;

import java.net.URISyntaxException;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 */
public class RoborallyApplication extends Application{

    BoardView boardView = null;
    Stage primaryStage;
    MenuBar menuBar;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        AppController app = new AppController();

        // create the primary scene
        primaryStage.setTitle("Roborally");

        BorderPane root = new BorderPane();
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);

        MenuBar menuBar = createMenu(app);

        Image img = null;
        try {
            img = new Image(SpaceView.class.getClassLoader().getResource("images/splashscreen.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(img);

        root.setCenter(imageView);
        root.setTop(menuBar);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene(); // this is to fix a likely bug with the nonresizable stage
        primaryStage.show();
    }

    /**
     * Creates the dropdown menu in the game.
     * @param app
     * @return
     */
    private MenuBar createMenu(AppController app) {

        menuBar = new MenuBar();
        Menu menu = new Menu("File");

        MenuItem item1 = new MenuItem("Save Game");
        item1.setOnAction(e -> {
            app.saveGame();
        });

        MenuItem item2 = new MenuItem("Start new game");
        item2.setOnAction(e -> {
            this.boardView = app.startNewGame();

            Scene scene = this.primaryStage.getScene();
            Parent root = scene.getRoot();
            ((BorderPane) root).setCenter(this.boardView);


            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();

            app.startGame();

        });

        MenuItem item3 = new MenuItem("Load a previous game");
        item3.setOnAction(e -> {
            this.boardView = app.loadGame();

            Scene scene = this.primaryStage.getScene();
            Parent root = scene.getRoot();
            ((BorderPane) root).setCenter(this.boardView);

            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        });

        menu.getItems().addAll(item2, item3, item1);
        menuBar.getMenus().add(menu);

        return menuBar;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}