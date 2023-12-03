package pisd.dk.dtu.compute.se.roborally.model;

import pisd.dk.dtu.compute.se.roborally.controller.GameController;

/**
 *
 * @author Benjamin Andresen
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 */
public class Gear implements FieldAction {

    public Direction direction;

    public void setDirection(Direction direction) { this.direction = direction;}

    public Gear(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean doAction(GameController gameController, Space space){

        Player player = space.getPlayer();

        switch (direction) {
            case LEFT:
                gameController.turnLeft(player);
                break;

            case RIGHT:
                gameController.turnRight(player);
                break;
        }

        return true;

    }
}

