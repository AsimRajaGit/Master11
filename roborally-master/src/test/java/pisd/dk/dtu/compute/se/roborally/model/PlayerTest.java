package pisd.dk.dtu.compute.se.roborally.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void player_can_have_last_checkpoint_set() {

        Board board = new Board(8,8);
        Player player = new Player(board, "red");

        assertEquals(0, player.getLastCheckpoint());

        player.setLastCheckpoint(1);

        assertEquals(1, player.getLastCheckpoint());

    }

    @Test
    public void player_can_have_last_checkpoint_only_if_higher_than_last() {

        Board board = new Board(8,8);
        Player player = new Player(board, "red");

        player.setLastCheckpoint(1);

        assertEquals(1, player.getLastCheckpoint());

        player.setLastCheckpoint(0);

        assertEquals(1, player.getLastCheckpoint());
        assertNotEquals(0, player.getLastCheckpoint());

    }

    @Test
    public void player_can_only_get_next_checkpoint_in_order() {

        Board board = new Board(8,8);
        Player player = new Player(board, "red");

        player.setLastCheckpoint(1);

        assertEquals(1, player.getLastCheckpoint());

        player.setLastCheckpoint(3);

        assertEquals(1, player.getLastCheckpoint());
        assertNotEquals(3, player.getLastCheckpoint());

    }
}