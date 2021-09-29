package project.battleship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class Model extends Observable {

    public List<Ship> shipList;

//    private char[][] board = new char[9][9];
    private SimpleStringProperty[][] board = new SimpleStringProperty[10][10];

    public SimpleStringProperty[][] getBoard() {
        return board;
    }

    public void createBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new SimpleStringProperty("Em");
            }
        }
    }

    public void createShipsWithstaticData() {
        //initilaize the ship variables
        shipList = new ArrayList<>();
        int length;
        List<String> coordinates;

        //create the ship object, set appropriate properties and add it to the ships list
        //First Ship length 2
        length = 2;
        coordinates = new ArrayList<>();
        coordinates.add("B3");
        coordinates.add("B4");
        Ship ship = new Ship(length, length, true, coordinates);

        //add the view as an observer to this ship
        //ship.addObserver(this);
        shipList.add(ship);

        //First Ship length 2
        length = 2;
        coordinates = new ArrayList<>();
        coordinates.add("H4");
        coordinates.add("I4");
        ship = new Ship(length, length, false, coordinates);

        //add the view as an observer to this ship
        //ship.addObserver(this);
        shipList.add(ship);

        //First Ship length 3
        length = 3;
        coordinates = new ArrayList<>();
        coordinates.add("D5");
        coordinates.add("E5");
        coordinates.add("F5");
        ship = new Ship(length, length, false, coordinates);

        //add the view as an observer to this ship
        //ship.addObserver(this);
        shipList.add(ship);

        //First Ship length 4
        length = 4;
        coordinates = new ArrayList<>();
        coordinates.add("I6");
        coordinates.add("I7");
        coordinates.add("I8");
        coordinates.add("I9");
        ship = new Ship(length, length, true, coordinates);

        //add the view as an observer to this ship
        //ship.addObserver(this);
        shipList.add(ship);

        //First Ship length 5
        length = 5;
        coordinates = new ArrayList<>();
        coordinates.add("B9");
        coordinates.add("C9");
        coordinates.add("D9");
        coordinates.add("E9");
        coordinates.add("F9");
        ship = new Ship(length, length, false, coordinates);

        //add the view as an observer to this ship
        //ship.addObserver(this);
        shipList.add(ship);
    }

    //if user uploads a file, parse the file to get ship data
    public boolean createShipsWithFileData(File file) {
        //create a temporary list. if no exception is thrown anywhere copy this temp list content to global ship list
        List<Ship> tempShipList = new ArrayList<>();

        //make a list which has the number of ships and their length as values. We will remove the ship length from this list after adding a ship to the temp list
        //if aafter adding all ships this list is empty, it means that th file was valid.
        //if it is not empty, it means file didnt have correct data. so we return false and dont save the ship data
        List<Integer> shipInfo = new ArrayList<>();
        shipInfo.add(2);
        shipInfo.add(2);
        shipInfo.add(3);
        shipInfo.add(4);
        shipInfo.add(5);

        try {
            //open the file
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";

            //read the file line by line. each line represents a ship
            while ((line = br.readLine()) != null) {
                //create a new ship object
                Ship ship1 = new Ship();
                int blockCount = 0;

                //spplit the line by commas
                String[] arr = line.split(",");

                //the first value in the array shows the ship orientation. vertical or horizontal
                int orientation = Integer.parseInt(arr[0]);

                //set the isVertical variable
                boolean isVertical = orientation != 0;
                ship1.setIsVertical(isVertical);

                //validate the coordinates given in the file and if it is corect add it to the coordinates list
                //if any invalid coordinate is found, return false and dont save the ship data
                List<String> coordinates = new ArrayList<>();

                //iterate the array from the second position
                for (int i = 1; i < arr.length; i++) {
                    //for each coordinate increase the bock count
                    blockCount++;

                    //add the coordinate to the list
                    coordinates.add(arr[i]);

                    //no validation for the first coordinate
                    if (i == 1) {
                        continue;
                    }

                    //for all other coordinates do validations depending on whether the orientaion is vertical or horizontal
                    String previous = arr[i - 1]; //previous coordinate
                    int currNo = Integer.parseInt(arr[i].substring(1)); //previous coordinate number
                    int prevNo = Integer.parseInt(previous.substring(1)); //current coordinate number
                    char curr = previous.charAt(0); //previous coordinate character
                    char prev = arr[i].charAt(0); //current coordinate character

                    //if orientation is horizontal
                    if (orientation == 0) {//horizontal   
                        //check that char is changing and number is same in coordinate
                        if (curr + 1 != prev || currNo != prevNo) {
                            return false;
                        }
                    } //if orientation is vertical
                    else {//vertical
                        //check that number is changing and char is same in coordinate
                        if (prevNo + 1 != currNo || curr != prev) {
                            return false;
                        }
                    }

                }
                //if all coordinates are correct, add the ship to the temp list
                ship1.setHealth(blockCount);
                ship1.setLength(blockCount);
                ship1.setCoordinates(coordinates);

                //add the view as an observer to this ship
                //ship1.addObserver(this);
                tempShipList.add(ship1);

                //remove the current ship length from the shipInfo list
                shipInfo.remove(shipInfo.indexOf(blockCount));
            }

        } catch (Exception ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        //if ship info list is not emo=pty, than it means that all required length ships were not found in the list. return false in that case
        if (!shipInfo.isEmpty()) {
            return false;
        }

        //if no error was found, save the temp list ship data to global ship list and retrun true;
        shipList = tempShipList;
        return true;
    }

    public boolean validateCoordinates(String coordinate) {
        try {
            int x = (int) coordinate.charAt(0) - 'A';
            int y = Integer.parseInt(coordinate.substring(1)) - 1;

            if (y >= 0 && x >= 0 && x < 10 && y < 10 && board[x][y].getValue().equals("Em")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean guessShipPosition(String coordinate) {

        //iterate through the ships to see if any ship is positioned at the current block
        for (Ship ship : shipList) {
            //if a ship is present at the current block
            if (ship.getCoordinates().contains(coordinate)) {
                //decrease the health of ship by 1
                ship.setHealth(ship.getHealth() - 1);

                updateBoard(coordinate, true);

                //call the ship observer setChanged method
                setChanged();

                //break the loop
                return true;
            }
        }
        updateBoard(coordinate, false);
        setChanged();
        return false;
    }

    public void updateBoard(String coordinate, boolean hit) {
        int x = (int) coordinate.charAt(0) - 'A';
        int y = Integer.parseInt(coordinate.substring(1)) - 1;
        if (hit) {
            this.board[x][y].setValue("H");
        } else {
            this.board[x][y].setValue("M");
        }
    }

    //override the addObserver method
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o); //To change body of generated methods, choose Tools | Templates.
    }

    //override the setChanged method
    @Override
    protected synchronized void setChanged() {
        super.setChanged();

        //notify the observers when the object setChanged is called
        notifyObservers();
    }
}
