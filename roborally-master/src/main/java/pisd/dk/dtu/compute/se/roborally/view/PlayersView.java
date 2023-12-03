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

import pisd.dk.dtu.compute.se.designpatterns.observer.Subject;
import pisd.dk.dtu.compute.se.roborally.controller.GameController;
import pisd.dk.dtu.compute.se.roborally.model.Board;
import pisd.dk.dtu.compute.se.roborally.model.Player;

import javafx.scene.control.TabPane;

import java.util.*;

/**
 * The view of the tabs and cards dependant on the selected player
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Gustav Utke Kauman, s195936@student.dtu.dk
 *
 */
public class PlayersView extends TabPane implements ViewObserver {

    private Board board;

    private PlayerView[] playerViews;

    private GameController gameController;

    public PlayersView(GameController gameController) {
        this.board = gameController.board;
        this.gameController = gameController;
        this.playerViews = null;

        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        this.playerViews = new PlayerView[board.getPlayersNumber()];
        for (int i = 0; i < board.getPlayersNumber();  i++) {
            this.playerViews[i] = new PlayerView(gameController, board.getPlayer(i));
            this.getTabs().add(playerViews[i]);
        }
        board.attach(this);
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            updateTabs();
            Player current = board.getCurrentPlayer();
            this.getSelectionModel().select(current.no);
        }
    }



    private void updateTabs() {

        List viewList = Arrays.asList(this.playerViews);
        Collections.sort(viewList, new Comparator<PlayerView>() {
            @Override
            public int compare(PlayerView o1, PlayerView o2) {
                return o1.getPlayer().no - o2.getPlayer().no;
            }
        });

        this.getTabs().removeAll(this.getTabs());

        for (PlayerView pv : this.playerViews) {
            this.getTabs().add(pv);
        }

    }

}
