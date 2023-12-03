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
package pisd.dk.dtu.compute.se.roborally.view;

import org.jetbrains.annotations.NotNull;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import pisd.dk.dtu.compute.se.designpatterns.observer.Subject;
import pisd.dk.dtu.compute.se.roborally.model.*;

import java.net.URISyntaxException;

/**
 * The view for the individual space
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Gustav Kirkholt, s164765@student.dtu.dk
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 70;
    final public static int SPACE_WIDTH = 70;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        if (space.board.getAntenna() != null && (space.board.getAntenna().x == space.x && space.board.getAntenna().y == space.y)) {
            this.setStyle("-fx-background-color: pink;");
        }

        updateView(this.space);

        // This space view should listen to changes of the space
        space.attach(this);
    }

    private void updatePlayer() {
        Player player = space.getPlayer();
        if (player != null) {
            Polygon figure = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                figure.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                figure.setFill(Color.MEDIUMPURPLE);
            }

            figure.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(figure);
        }
    }

    private void updateBelt(){
        ConveyorBelt belt = space.getConveyorBelt();
        if (belt != null) {

            Polygon fig = new Polygon(0.0, 0.0,
                    60.0, 0.0,
                    30.0, 60.0);

            fig.setFill(Color.LIGHTGRAY);

            fig.setRotate((90*belt.getHeading().ordinal())%360);
            this.getChildren().add(fig);
        }

    }

    private void updateWalls(){
        Space space = this.space;
        if (space != null && !space.getWalls().isEmpty()) {
            for (Heading wall : space.getWalls()) {

                Polygon fig = new Polygon(.0,0.0,
                        70.0,0.0,
                        70.0,5.0,
                        0.0,5.0);

                switch (wall) {
                    case EAST:
                        fig.setTranslateX(32.5);
                        fig.setRotate((90*wall.ordinal()) % 360);
                        break;

                    case SOUTH:
                        fig.setTranslateY(32.5);
                        break;

                    case WEST:
                        fig.setTranslateX(-32.5);
                        fig.setRotate((90*wall.ordinal()) % 360);
                        break;

                    case NORTH:
                        fig.setTranslateY(-32.5);
                        break;
                }

                fig.setFill(Color.ORANGERED);
                this.getChildren().add(fig);

            }
        }
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            updateBelt();

            if (space.board.getAntenna() != null && (space.board.getAntenna().x == space.x && space.board.getAntenna().y == space.y)) {
                this.setStyle("-fx-background-color: pink;");
            } else {
                if ((space.x + space.y) % 2 == 0) {
                    this.setStyle("-fx-background-color: white;");
                } else {
                    this.setStyle("-fx-background-color: black;");
                }
            }

            if (space.getStartPlayerNo() > 0) {
                Image img = null;
                try {
                    img = new Image(SpaceView.class.getClassLoader().getResource("images/startpoint.png").toURI().toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                ImageView imgView = new ImageView(img);
                imgView.setImage(img);
                imgView.setRotate(-90);
                imgView.setFitHeight(SPACE_HEIGHT);
                imgView.setFitWidth(SPACE_WIDTH);
                imgView.setVisible(true);

                this.getChildren().add(imgView);
            }

            for (FieldAction action : space.actions) {
                if (action instanceof Checkpoint) {
                    addImage("images/checkpoint" + ((Checkpoint) action).no + ".png", -90);
                }

                if (action instanceof Pit) {
                    addImage("images/pit.png");
                }

                if (action instanceof Gear) {
                    addImage("images/gear" + (((Gear) action).direction) + ".png");
                }

            }

            updateWalls();
            updatePlayer();

        }
    }

    private ImageView addImage(String name) {
        Image img = null;
        try {
            img = new Image(SpaceView.class.getClassLoader().getResource(name).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView imgView = new ImageView(img);
        imgView.setImage(img);
        imgView.setFitHeight(SPACE_HEIGHT);
        imgView.setFitWidth(SPACE_WIDTH);
        imgView.setVisible(true);

        this.getChildren().add(imgView);

        return imgView;
    }

    private ImageView addImage(String name, double rotation) {
        ImageView imageView = addImage(name);
        imageView.setRotate(rotation);

        return imageView;
    }

}
