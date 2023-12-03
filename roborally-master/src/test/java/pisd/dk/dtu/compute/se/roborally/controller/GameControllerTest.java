package pisd.dk.dtu.compute.se.roborally.controller;

import org.junit.Test;
import pisd.dk.dtu.compute.se.roborally.model.Heading;
import pisd.dk.dtu.compute.se.roborally.model.Player;
import pisd.dk.dtu.compute.se.roborally.model.Space;

import static org.junit.Assert.*;

public class GameControllerTest {

    AppController app;
    GameController gameController;

    @org.junit.Before
    public void setUp() throws Exception {

        this.app = new AppController();
        app.gameController = new GameController(app.createBoardFromLayout(null));
        this.gameController = app.gameController;

    }

    @Test
    public void player_can_turn_left() {

        gameController.createPlayers(2);

        Player player = this.gameController.board.getPlayer(0);
        player.setHeading(Heading.NORTH);

        gameController.turnLeft(player);

        assertEquals(Heading.WEST, player.getHeading());

    }

    @Test
    public void player_can_turn_right() {

        gameController.createPlayers(2);
        Player player = gameController.board.getPlayer(0);

        player.setHeading(Heading.NORTH);

        gameController.turnRight(player);

        assertEquals(Heading.EAST, player.getHeading());

    }

    @Test
    public void player_can_move_forward() {

        gameController.createPlayers(2);
        Player player = gameController.board.getPlayer(0);
        player.setSpace(gameController.board.getSpace(0,0));
        player.setHeading(Heading.SOUTH);

        gameController.moveForward(player);

        Space playerSpace = player.getSpace();
        assertEquals(0, playerSpace.x);
        assertEquals(1, playerSpace.y);

    }

    @Test
    public void player_will_push_player_in_front() {

        gameController.createPlayers(2);

        Player player1 = gameController.board.getPlayer(0);
        player1.setSpace(gameController.board.getSpace(0,0));
        player1.setHeading(Heading.SOUTH);

        Player player2 = gameController.board.getPlayer(1);
        player2.setSpace(gameController.board.getSpace(0,1));

        gameController.moveForward(player1);

        assertEquals(0, player2.getSpace().x);
        assertEquals(2, player2.getSpace().y);

        assertEquals(0, player1.getSpace().x);
        assertEquals(1, player1.getSpace().y);

    }

    @Test
    public void player_gets_stopped_by_wall() {

        gameController.createPlayers(2);
        Player player = gameController.board.getPlayer(0);
        Space space = player.getSpace();
        player.setHeading(Heading.SOUTH);

        space.getNeighbourSpace(Heading.SOUTH).addWall(Heading.NORTH);

        gameController.moveForward(player);

        assertNotNull(player.getSpace());
        assertEquals(space, player.getSpace());

    }

    @Test
    public void player_does_not_move_if_player_to_be_pushed_cannot_move() {

        // Set up
        gameController.createPlayers(2);

        Player player1 = gameController.board.getPlayer(0);
        player1.setSpace(gameController.board.getSpace(0,0));
        player1.setHeading(Heading.SOUTH);

        Player player2 = gameController.board.getPlayer(1);
        player2.setSpace(gameController.board.getSpace(0,1));

        gameController.board.getSpace(0,2).addWall(Heading.NORTH);

        // Execution
        gameController.moveForward(player1);

        // Validation
        assertEquals(gameController.board.getSpace(0,0), player1.getSpace());
        assertEquals(gameController.board.getSpace(0,1), player2.getSpace());

    }
}