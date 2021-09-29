package project.battleship;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestClass {

    Model model = new Model();

    /**
     * Test the validation logic by passing valid coordinates
     */
    @Test
    public void testValidationLogicWithValidData() {
        model.createBoard();
        boolean res = model.validateCoordinates("A6");
        assertEquals("Test with valid coordinate data", true, res);
    }

    /**
     * Test the validation logic by passing invalid coordinates
     */
    @Test
    public void testValidationLogicWithInvalidData() {
        model.createBoard();
        boolean res = model.validateCoordinates("A15");
        assertEquals("Test with invalid coordinate data", false, res);
    }

    /**
     * Test the guessing logic with passing correct ship coordinates
     */
    @Test
    public void testGuessingLogicShipCordinate() {
        model.createBoard();
        model.createShipsWithstaticData();
        boolean res = model.guessShipPosition("B3");
        assertEquals("Test cordinate with correct ship posiiton", true, res);
    }

    /**
     * Test the guessing logic with passing incorrect ship coordinates
     */
    @Test
    public void testGuessingLogicWithEmptyCordinate() {
        model.createBoard();
        model.createShipsWithstaticData();
        boolean res = model.guessShipPosition("B1");
        assertEquals("Test cordinate with empty posiiton", false, res);
    }

    /**
     * Test whether a file with valid ship data is loaded or not
     */
    @Test
    public void testCustomFileLoadingWithValidFileData() {
        model.createBoard();
        File file = new File("Ship.txt");
        boolean res = model.createShipsWithFileData(file);
        assertEquals("File data loaded successfully", true, res);
    }

    /**
     * Test by hitting all ship coordinates to see that the data has been loaded
     * successfully or not
     */
    @Test
    public void testCustomFileShipData() {
        model.createBoard();
        File file = new File("Ship.txt");
        model.createShipsWithFileData(file);

        boolean res = model.guessShipPosition("A2");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("A3");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("A4");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("A5");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("A6");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("C7");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("D7");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("E7");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("F7");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("H3");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("H4");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("H5");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("B8");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("B9");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("I10");
        assertEquals("Ship hit correctly", true, res);
        res = model.guessShipPosition("J10");
        assertEquals("Ship hit correctly", true, res);
    }

    /**
     * The invalid file has only data for 4 ships so it will not be loaded and
     * model will return false
     */
    @Test
    public void testCustomFileLoadingWithInvalidFileData() {
        model.createBoard();
        File file = new File("ShipInvalidData.txt");
        boolean res = model.createShipsWithFileData(file);
        assertEquals("File data loaded successfully", false, res);
    }
}
