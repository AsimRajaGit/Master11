package pisd.dk.dtu.compute.se.fileactions.model;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Simple extension of the Java FX library to load a file
 * and return the path of that file
 *
 * @author Gustav Utke Kauman, s195396@student.dtu.dk
 */
public class FileLoader {

    public String open() {

        FileChooser c = new FileChooser();
        c.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json Files", "*.json"));
        File selectedFile = c.showOpenDialog(null);

        if (selectedFile == null) {
            return "";
        }

        return selectedFile.getAbsolutePath();

    }

}
