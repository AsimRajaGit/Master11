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

import pisd.dk.dtu.compute.se.designpatterns.observer.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class determines if a space on the board has a player on it currently, defined by an x and y coordginate.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 *
 */
public class Space extends Subject {

    public final Board board;

    public final int x;
    public final int y;

    private int startPlayerNo;

    public Player player;

    public List<FieldAction> actions = new ArrayList<>();
    private List<Heading> walls = new ArrayList<>();

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     * Calculates what fields are next to the current field
     * @param heading
     * @return the field next to current field
     */
    public Space getNeighbourSpace(Heading heading) {
        int newX, newY;
        switch (heading) {

            case NORTH:
                newX = x;
                newY = (y - 1) % board.height;

                if (newY == -1)
                    newY = 7;

                break;
            case SOUTH:
                newX = x;
                newY = (y + 1) % board.height;
                break;
            case WEST:
                newX = (x - 1) % board.width;

                if (newX == -1)
                    newX = 7;

                newY = y;
                break;
            case EAST:
                newX = (x + 1) % board.width;
                newY = y;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + heading);
        }

        return this.board.getSpace(newX, newY);

    }

    public List<FieldAction> getActions() {
        return actions;
    }

    public ConveyorBelt getConveyorBelt() {

        ConveyorBelt belt = null;

        for (FieldAction action : this.actions) {
            if (action instanceof ConveyorBelt && belt == null) {
                belt = (ConveyorBelt) action;
            }
        }

        return belt;

    }

    public void addAction(FieldAction action) {
        this.actions.add(action);

        if (action instanceof Checkpoint) {
            this.board.setCheckpoint((Checkpoint) action);
        }

        notifyChange();
    }

    public List<Heading> getWalls() {
        return walls;
    }

    public void addWall(Heading heading) {
        if ( ! walls.contains(heading)) {
            walls.add(heading);
            notifyChange();
        }
    }


    public void addGear(Direction direction) {
        boolean check = false;

        for (FieldAction action : actions) {
            if (action instanceof Gear) {
                check = true;
            }
        }

        if (!check) {
            this.actions.add(new Gear(direction));
            notifyChange();
        }
    }

    public void setStartPlayerNo(int startPlayerNo) {
        this.startPlayerNo = startPlayerNo;
        notifyChange();
    }

    public int getStartPlayerNo() {
        return startPlayerNo;
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

}
