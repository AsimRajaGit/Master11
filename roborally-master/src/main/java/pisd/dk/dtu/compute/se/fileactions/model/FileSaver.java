package pisd.dk.dtu.compute.se.fileactions.model;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Simple extension of the Java FX library to save a file
 * and return the path of that file
 *
 * @author Gustav Utke Kauman, s195936@student.dtu.dk
 */
public class FileSaver {

    public String save() {

        FileChooser c = new FileChooser();
        c.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json Files", "*.json"));
        File selectedFile = c.showSaveDialog(null);

        return selectedFile.getAbsolutePath();

    }

}
