package pisd.dk.dtu.compute.se.fileactions.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import pisd.dk.dtu.compute.se.boarddesigner.model.BoardTemplate;
import pisd.dk.dtu.compute.se.fileactions.model.FileSaver;
import pisd.dk.dtu.compute.se.roborally.model.Adapter;
import pisd.dk.dtu.compute.se.roborally.model.Board;
import pisd.dk.dtu.compute.se.roborally.model.FieldAction;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple controller to handle saving of different objects.
 * In it's initial (and maybe final) version this just handles the saving of
 * instances of Board.
 *
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 */
public class SaveController {

    public static void saveBoard(Board board) {

        try {

            FileSaver fs = new FileSaver();
            String filename = fs.save();

            GsonBuilder builder = new GsonBuilder().registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                    setPrettyPrinting();
            Gson gson = builder.create();

            FileWriter fw = new FileWriter(filename);
            JsonWriter writer = gson.newJsonWriter(fw);

            BoardTemplate bt = (new BoardTemplate()).fromBoard(board);

            gson.toJson(bt, bt.getClass(), writer);

            writer.close();


        } catch (IOException e) {
            // XXX We should probably do something here...
        }


    }

}
