package pisd.dk.dtu.compute.se.roborally.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import org.apache.commons.io.FilenameUtils;
import pisd.dk.dtu.compute.se.boarddesigner.model.BoardTemplate;
import pisd.dk.dtu.compute.se.fileactions.model.FileLoader;
import pisd.dk.dtu.compute.se.roborally.model.Adapter;
import pisd.dk.dtu.compute.se.roborally.model.Board;
import pisd.dk.dtu.compute.se.roborally.model.FieldAction;
import pisd.dk.dtu.compute.se.roborally.model.Phase;
import pisd.dk.dtu.compute.se.roborally.model.database.DatabaseConnection;
import pisd.dk.dtu.compute.se.roborally.model.database.GameInDB;
import pisd.dk.dtu.compute.se.roborally.model.database.Repository;
import pisd.dk.dtu.compute.se.roborally.view.BoardView;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple controller to create all the necessary things to
 * actually run the Roborally game.
 *
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 */
public class AppController {

    private Board board = null;
    private BoardView boardView;
    public GameController gameController;
    private int selectNoOfPlayerCounter = 0;

    public int selectNoOfPlayers() {
        List antalSpillerItems = new ArrayList<Integer>();

        for (int i = 2; i <= 6; i++) {
            antalSpillerItems.add(i);
        }

        ChoiceDialog antalSpillerValg = new ChoiceDialog();
        antalSpillerValg.getItems().addAll(antalSpillerItems);

        antalSpillerValg.setTitle("Antal spillere");
        antalSpillerValg.setContentText("Vælg antal spillere: ");
        antalSpillerValg.setSelectedItem(antalSpillerValg.getItems().get(0));
        antalSpillerValg.showAndWait();

        if (antalSpillerValg.getSelectedItem() == null) {

            String msg = "Vælg venligst antal spillere. Du har forsøgt " + ++this.selectNoOfPlayerCounter + " gange indtil nu. Efter 3 forsøg lukker programmet.";
            Alert alert = new Alert(Alert.AlertType.ERROR, msg);
            alert.showAndWait();

            if (this.selectNoOfPlayerCounter >= 3) {
                System.exit(-1);
            }

            selectNoOfPlayers();

        }

        int antalSpillere = Integer.parseInt(antalSpillerValg.getSelectedItem().toString());

        return antalSpillere;
    }

    public void startGame() {
        this.gameController.initializeProgrammingPhase();
    }

    public BoardView loadGame() {

        Repository repo = new Repository(new DatabaseConnection());

        List<GameInDB> games = repo.getGames();

        ChoiceDialog dialog = new ChoiceDialog();
        dialog.setContentText("Vælg hvilket spil, du vil indlæse");
        dialog.getItems().addAll(games);
        dialog.showAndWait();

        if (dialog.getSelectedItem() != null) {
            this.board = repo.loadGameFromDB(((GameInDB) dialog.getSelectedItem()).id);
            this.gameController = new GameController(this.board);

            if (this.board.getPhase() == Phase.INITIALISATION) {
                this.gameController.initializeProgrammingPhase();
            }

            return boardView = new BoardView(gameController);
        }

        return null;
    }

    public BoardView createGame() {
        String layout = this.selectBoardLayout();

        // The user haven't provided a proper file
        if (layout.equals("")) {
            layout = "default";
        }

        this.board = createBoardFromLayout(layout);
        this.gameController = new GameController(this.board);

        int noOfPlayers = this.selectNoOfPlayers();
        this.gameController.createPlayers(noOfPlayers);

        return boardView = new BoardView(gameController);
    }

    public Board createBoardFromLayout(String layout) {

        GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        InputStream is = null;

        if (layout == null) {
            return new Board(8,8);
        }

        if (layout.equals("default")) {
            is = this.getClass().getClassLoader().getResourceAsStream("boards/default.json");
        } else {
            try {
                is = new FileInputStream(new File(layout));
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "En fejl opstod under indlæsningen af filen.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }

        try {
            InputStreamReader isr = new InputStreamReader(is);

            JsonReader reader = gson.newJsonReader(isr);

            BoardTemplate boardTemplate = gson.fromJson(reader, BoardTemplate.class);
            Board board = boardTemplate.toBoard();

            reader.close();

            return board;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private String selectBoardLayout() {

        FileLoader fl = new FileLoader();
        String filename = fl.open();

        return filename;

    }

    private List getAvailableBoardLayouts() {

        List boardNames = new ArrayList<String>();

        try {
            File folder = new File(this.getClass().getClassLoader().getResource("boards/").toURI());
            File[] files = folder.listFiles();


            for (File file : files) {
                if (file.isFile()) {
                    boardNames.add(FilenameUtils.removeExtension(file.getName()));
                }
            }

        } catch (URISyntaxException e) {
            // XXX Do nothing for now.
            //     We just don't want to throw this exception any further
            //     as we really should handle this here.
        }

        return boardNames;

    }

    public void saveGame() {
        Repository repo = new Repository(new DatabaseConnection());
        if (this.board.getGameID() != 0) {
            repo.updateGameInDB(this.board);
        } else {
            repo.createGameInDB(this.board);
        }
    }

    public BoardView startNewGame() {
        this.board = null;
        this.boardView = null;
        this.gameController = null;
        this.selectNoOfPlayerCounter = 0;

        return createGame();
    }

}
