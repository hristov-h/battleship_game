/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.battleship;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Work
 */
public class Console implements Observer {

    Model model;

    Scanner sc;

    int hits = 0;
    int noOfTries = 0;

    private SimpleStringProperty[][] board = new SimpleStringProperty[10][10];

    public static void main(String[] args) {
        Console console = new Console();
        console.initialize();
    }

    public void initialize() {
        model = new Model();

        model.addObserver(this);

        sc = new Scanner(System.in);

        boolean loop = true;

        while (loop) {
            System.out.println("-------------------------------------------------");
            System.out.println("Choose an option to start the game"
                    + "\n1. Start new game with general ship data "
                    + "\n2. Start new game using a file to set up the ship data"
                    + "\n3. Exit");

            int choice = sc.nextInt();

            if (choice == 1) {
                model.createBoard();
                model.createShipsWithstaticData();
                sc.nextLine();
            } else if (choice == 2) {
                File file = getFileFromUser();
                model.createBoard();
                boolean res = model.createShipsWithFileData(file);
                if (res) {
                    System.out.println("Ship data loaded");
                } else {
                    System.out.println("Error in parsing file data");
                }
            } else if (choice == 3) {
                loop = false;
                continue;
            } else {
                System.out.println("Invalid Choice");
                continue;
            }

            this.board = model.getBoard();

            printBoard();

            startGame();
        }

    }

    public void printBoard() {
        System.out.println("-------------------------------------------------");
        char c = 'A';
        System.out.print("\t");
        for (int i = 0; i < 10; i++) {
            System.out.print(c + "\t");
            c++;
        }

        System.out.println("");

        for (int i = 0; i < 10; i++) {
            System.out.print((i + 1) + "\t");
            for (int j = 0; j < 10; j++) {
                System.out.print(board[j][i].getValue() + "\t");
            }
            System.out.println("");
        }
        System.out.println("-------------------------------------------------");
    }

    public void startGame() {

        hits = 0;

        noOfTries = 0;

        boolean gameOver = false;

        while (!gameOver) {
            System.out.println("Enter coordinates");
            String coordinates = sc.nextLine();

            boolean checkCordinates = model.validateCoordinates(coordinates);

            if (!checkCordinates) {
                System.out.println("Invalid coordinates");
                System.out.println("-------------------------------------------------");
                continue;
            }

            boolean isHit = model.guessShipPosition(coordinates);

            if (isHit) {
                System.out.println("Awesome!! You hit the enemy ship");
                hits++;
            } else {
                System.out.println("Oops!! You missed.");
            }
            System.out.println("-------------------------------------------------");
            printBoard();

            noOfTries++;

            if (hits == 16) {
                System.out.println("Congratulations !! You completed the game in " + noOfTries + " moves.");
                gameOver = true;
            }
        }
    }

    public File getFileFromUser() {
        sc.nextLine();
        System.out.println("Enter the path to file");
        String path = sc.nextLine();
        File file = new File(path);
        return file;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Ship data has changed");

        //this.board = model.getBoard();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.board[i][j] = model.getBoard()[i][j];
            }
        }
    }

}
