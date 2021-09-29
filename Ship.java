package project.battleship;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * model class for ship
 */
public class Ship {

    private int length; //ship length. no of blocks it occupies
    private int health; //health of ship
    private boolean isVertical; //is the ship vertical(true) or horizontal(false)
    private List<String> coordinates; //stores the coordinates of where ship is placed

    public Ship() {
    }

    public Ship(int length, int health, boolean isVertical, List<String> coordinates) {
        this.length = length;
        this.health = health;
        this.isVertical = isVertical;
        this.coordinates = coordinates;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isIsVertical() {
        return isVertical;
    }

    public void setIsVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

}
