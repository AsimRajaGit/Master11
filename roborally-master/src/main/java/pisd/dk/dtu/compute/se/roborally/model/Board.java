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
package pisd.dk.dtu.compute.se.roborally.model;

import org.jetbrains.annotations.NotNull;
import javafx.scene.control.TextInputDialog;
import pisd.dk.dtu.compute.se.designpatterns.observer.Subject;

import java.util.ArrayList;
import java.util.List;

import static pisd.dk.dtu.compute.se.roborally.model.Phase.INITIALISATION;

/**
 * This class creates the Board, adds spaces and adds players to the board when its method is
 * instantiated in the initial application class. extends subject
 * as the observer that notifies the view (GUI) when it is changed
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Gustav Kirkholt, s164765@student.dtu.dk
 *
 */
public  class Board extends Subject {

    public final int width;
    public final int height;

    private Space[][] spaces;
    private Antenna antenna;

    private List<Checkpoint> checkpoints = new ArrayList<Checkpoint>();

    private List<Player> players = new ArrayList<Player>();
    private Player current;

    private Phase phase = INITIALISATION;
    private int step = 0;

    public int change;

    private boolean stepMode;

    private int gameID;
    private String name;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];

        // We set the antenna in case it isn't provided later on
        this.antenna = new Antenna(this, ((int) (Math.random() * width)), ((int) (Math.random() * height)));

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    public Board(int width, int height, Space[][] spaces) {
        this.width = width;
        this.height = height;
        this.spaces = spaces;

    }

    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    public void setChange(int change) {
        this.change = change;
    }

    public int getChange() {
        return change;
    }

    /**
     * This function contains the updated time from the stopwatch
     * @param change is current time
     */
    public void timerChange(int change) {
        this.change=change;
        notifyChange();
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            for (Player player: players) {
                if (player.no == i)
                    return player;
            }
        }

        return null;

    }

    public Player getPlayerByDB(int i) {
        if (i >= 0 && i < players.size()) {
            for (Player player: players) {
                if (player.getDbNo() == i)
                    return player;
            }
        }

        return null;
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    public Antenna getAntenna() {
        return this.antenna;
    }

    public void setAntenna(Antenna antenna) {
        this.antenna = antenna;
        for (Space[] spaces : this.spaces) {
            for (Space space : spaces) {
                // Very hack, we just need to trigger an update on all spaces.
                space.playerChanged();
            }
        }
    }


    public List<Checkpoint> getCheckpoints() {
        return this.checkpoints;
    }

    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoints.add(checkpoint);
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getName() {

        if (this.name != null) {

            return name;

        } else {

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Navn");
            dialog.setContentText("Indtast dit navn for spillet");
            dialog.showAndWait();

            if (dialog.getResult() != null) {
                this.name = dialog.getResult();
                return this.name;
            }

        }

        return null;

    }
}
