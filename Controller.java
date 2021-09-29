package project.battleship;

import java.io.File;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Controller {

    View view;

    Model model;

    public Controller() {
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setUpTheBoard() {
        model.createBoard();
        model.createShipsWithstaticData();
    }

    public boolean setUpShipsUsingFiles(File file) {
        boolean success = model.createShipsWithFileData(file);
        return success;
    }

    //function called when the user clicks on any block on the board
    public void isHit(String coordinate) {        
        boolean hit = model.guessShipPosition(coordinate);
    }

}
