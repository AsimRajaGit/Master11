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
package pisd.dk.dtu.compute.se.roborally.controller;

import org.jetbrains.annotations.NotNull;

import javafx.scene.control.Alert;
import pisd.dk.dtu.compute.se.roborally.model.*;

import java.util.*;

/**
 * This is the controller that interacts when they players
 * do some action in the GUI and the game has to calculate
 * what happens from there. It's in essence the controlling
 * part of the game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 * @author Asim Raja
 */
public class GameController implements StopWatch.StopWatchListener {

    final public Board board;
    public StopWatch stopwatch;
    public boolean won = false;

    public GameController(Board board) {
        this.board = board;
//        this.stopwatch = new StopWatch(30,1000,1000);

        if (this.board.getPhase() == Phase.INITIALISATION)
            this.board.setPhase(Phase.PROGRAMMING);

//        stopwatch.addListener(this);
    }

    /**
     * This function gets the position and direction of the player and moves the player one square forward
     * @param player
     */
    public void moveForward(Player player) {
        if (player != null && player.board == board) {
            Space currentPosition = player.getSpace();

            if (player.getSpace().getNeighbourSpace(player.getHeading()).getPlayer() != null) {
                Player neighbourPlayer = player.getSpace().getNeighbourSpace(player.getHeading()).getPlayer();
                moveForwardInHeading(neighbourPlayer, player.getHeading());
            }

            if (player.getSpace().getNeighbourSpace(player.getHeading()).getWalls().contains(player.getHeading().opposite())) {
                return;
            }

            if (player.getSpace().getWalls().contains(player.getHeading())) {
                return;
            }

            int x = currentPosition.x;
            int y = currentPosition.y;

            int newX = 0, newY = 0;

            switch (player.getHeading()) {

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
            }
            /**
             * this part of the function does if there are no change in the current players position then
             * it sets the players current position to the new position
             */
            Space newPosition = board.getSpace(newX, newY);
            if (newPosition != null &&
                    newPosition.getPlayer() == null &&
                    newPosition != currentPosition) {
                newPosition.setPlayer(player);
            }
        }
    }

    public void moveForwardInHeading(Player player, Heading heading) {
        Heading prevHeading = player.getHeading();
        player.setHeading(heading);
        moveForward(player);
        player.setHeading(prevHeading);
    }

    /**
     * This function calls the moveForward function twice
     * @param player
     */
    public void fastForward(Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * This function turns the player relative to the direction they are already pointing
     * in this case it turns to the right
     * @param player
     */
    public void turnRight(Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }
    }

    /**
     * This function turns the player relative to the direction they are already pointing
     * in this case it turns to the left
     * @param player
     */
    public void turnLeft(Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * This function
     * @param source
     * @param target
     * @return
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null & targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function updates the program when the player(s) tap the finish programming button
     * the execute button then becomes active
     */
    public void finishProgrammingPhase() {
//        stopwatch.stop();
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    /**
     * This function executes the programming/moves the player(s) makes when the player taps the execute button
     * @param player
     * @param option
     */
    public void executePlayersOption(Player player, Command option) {
        if (player != null && player.board == board && board.getCurrentPlayer() == player) {
            board.setPhase(Phase.ACTIVATION);
            execute(option);
        }
    }

    /**
     * This function make the specific moves the player(s) requests
     */
    public void executePrograms() {
        board.setStepMode(false);
        while (board.getPhase() == Phase.ACTIVATION) {
            executeStep(null);
        }
    }

    private void execute(Command command) {
        executeStep(command);
        if (board.getPhase() == Phase.ACTIVATION && !board.isStepMode()) {
            executePrograms();
        }
    }

    public void initializeProgrammingPhase() {

//        stopwatch.startTimer();
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    player.getProgramField(j).setCard(null);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    player.getCardField(j).setCard(generateRandomCommandCard());
                }
            }
        }
    }

    public void executeStep() {
        board.setStepMode(true);
        executeStep(null);
    }

    private void executeStep(Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                if (option != null) {
                    executeCommand(currentPlayer, option);
                } else {
                    executeCommandCard(currentPlayer, currentPlayer.getProgramField(step).getCard());
                }
                if (board.getPhase() == Phase.ACTIVATION) {
                    if (currentPlayer.no + 1 < board.getPlayersNumber()) {
                        board.setCurrentPlayer(board.getPlayer(currentPlayer.no + 1));
                    } else {

                        for (Player player : this.board.getPlayers()) {
                            for (FieldAction action : player.getSpace().getActions()) {
                                if (won)
                                    break;

                                action.doAction(this, player.getSpace());
                            }
                        }

                        calculatePlayerOrder();

                        step++;
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    }
                }
            }
            if (board.getPhase() == Phase.ACTIVATION && (step < 0 || step >= Player.NO_REGISTERS)) {
                initializeProgrammingPhase();
            }
        }

        if (board.getPhase() == Phase.INITIALISATION) {
            initializeProgrammingPhase();
        }
    }

    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    private void executeCommandCard(@NotNull Player player, CommandCard card) {
        if (card != null) {
            executeCommand(player, card.command);
        }
    }

    private void executeCommand(@NotNull Player player, Command command) {
        if (player.board == board && command != null) {
            // XXX This is an very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a much more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).
            if (command.isInteractive()) {
                board.setPhase(Phase.PLAYER_INTERACTION);
            } else {
                switch (command) {
                    case FORWARD:
                        this.moveForward(player);
                        break;
                    case RIGHT:
                        this.turnRight(player);
                        break;
                    case LEFT:
                        this.turnLeft(player);
                        break;
                    case FAST_FORWARD:
                        this.fastForward(player);
                        break;
                    default:
                        // DO NOTHING (for now)
                }
            }
        }
    }

    /**
     * This function sets the starting position of the players
     * @param noOfPlayers
     */
    public void createPlayers(int noOfPlayers) {

        String[] colors = new String[]{"red", "green", "blue", "orange", "grey", "purple"};

        for (int i = 0; i < noOfPlayers; i++) {

            Player player = new Player(this.board, colors[i], "Player " + (i + 1));
            player.setDbNo(i);
            board.addPlayer(player);
            for(Space[] spacerow : this.board.getSpaces()){
                if (player.getSpace() != null) {
                    break;
                }

                for(Space space : spacerow){
                    if(space.getStartPlayerNo() == (i + 1)){
                        player.setSpace(space);
                        break;
                    }
                }
            }

            if (player.getSpace() == null) {
                // Player hasn't been given a space yet
                for (int j = 0; j < board.width; j++) {
                    Space space = board.getSpace(j, 0);
                    if (space != null && space.getPlayer() == null) {
                        player.setSpace(space);
                        break;
                    }
                }
            }

        }

        calculatePlayerOrder();

        board.setCurrentPlayer(board.getPlayer(0));
    }


    private void calculatePlayerOrder() {

        Antenna antenna = this.board.getAntenna();

        Map<Player, Integer> PlayersOrdered = new HashMap<Player, Integer>();

        for (int x = 0; x < this.board.width; x++) {
            for (int y = 0; y < this.board.height; y++) {
                Player player = this.board.getSpace(x, y).getPlayer();
                if (player != null) {

                    int length = Math.abs(antenna.x - x) + Math.abs(antenna.y - y);

                    PlayersOrdered.put(player, length);

                }
            }
        }

        List<Map.Entry<Player, Integer>> list = new LinkedList<>(PlayersOrdered.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Player, Integer>>() {
            @Override
            public int compare(Map.Entry<Player, Integer> o1, Map.Entry<Player, Integer> o2) {
                if (o1.getValue() == o2.getValue()) {
                    // The robots have the same distance to the antenna
                    if (o2.getKey().getSpace().y > antenna.y && o1.getKey().getSpace().y > antenna.y) {
                        // Both robots are above the antenna
                        return o1.getKey().getSpace().x - o2.getKey().getSpace().x;
                    }

                    if (o2.getKey().getSpace().y < antenna.y && o1.getKey().getSpace().y < antenna.y) {
                        // Both robots are below the antenna
                        return o2.getKey().getSpace().x - o1.getKey().getSpace().x;
                    }

                    if (o2.getKey().getSpace().y > antenna.y || o1.getKey().getSpace().y > antenna.y) {
                        // One of the robots are above the antenna
                        return o1.getKey().getSpace().x - o2.getKey().getSpace().x;
                    }


                } else {
                    return o1.getValue() - o2.getValue();
                }

                return 0;
            }
        });

        for (int i = 0; i < list.size(); i++) {
            list.get(i).getKey().no = i;
        }

        this.board.setCurrentPlayer(list.get(0).getKey());

    }

    public void initiateWin(Player player) {
        Alert winMsg = new Alert(Alert.AlertType.INFORMATION, "Spiller \"" + player.getName() + "\" har vundet spillet.");
        this.won = true;
        winMsg.showAndWait();
    }

    @Override
    public void onZero() {
        finishProgrammingPhase();
    }

}