package project.battleship;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * View class
 */
public class View extends Application implements Observer {

    //controller reference variabel declared
    Controller controller;

    //grid - board for the game
    GridPane grid;

    //root pane of our application which contains grid and other nodes
    VBox vbox;

    //label node to show status when a player wins
    Label victoryLabel;

    Model model;

    private SimpleStringProperty[][] board = new SimpleStringProperty[10][10];

    int noOfTries = 0;
    int hits = 0;

    public static void main(String[] args) {
        launch();
    }

    //When setChanged is called on ship, check if the game is over or not
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Ship data has changed");
//        boolean gameOver = isGameOver();
//
//        //check if game is over or not
//        if (gameOver) {
//            System.out.println("Game Over");
//            //if true, call the controller gameOver method to update the UI
//            controller.gameWon();
//        }

        //this.board = model.getBoard();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.board[i][j] = model.getBoard()[i][j];
            }
        }
    }

    //launch() in main calls this method
    @Override
    public void start(Stage stage) throws Exception {

        model = new Model();

        model.addObserver(this);

        //initailize the controller class
        controller = new Controller();

        //add this view reference to the conroller
        controller.view = this;

        controller.setModel(model);

        controller.setUpTheBoard();

        this.board = model.getBoard();

        //call necessary methods to update the view
        createView();

        //create scene
        Scene scene = new Scene(vbox, 600, 480);

        //add the scene to the stage
        stage.setScene(scene);

        //show the stage/window
        stage.show();
    }

    //method to update the view
    public void createView() {
        //create the view by initializing vbox and adding other nodes to it
        initialize();

        //create boards
        createGameUI();

        noOfTries = 0;
        
        hits = 0;
    }

    public void initialize() {

        //initialize vbox and set its constraints
        vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);

        //add button for adding ships through a file
        Button button = new Button("Upload File for custom Ship Arrangement");

        //button handler
        button.setOnAction(e -> {

            //open a file chooser so user can select the txt file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Input File");
            fileChooser.setInitialDirectory(new File(".")); //default directory is the root directory of this project
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("TXT", "*.txt")
            );
            File file = fileChooser.showOpenDialog(button.getScene().getWindow());

            //if file is selected
            if (file != null) {
                //parse the ile data and create ship list
                boolean success = controller.setUpShipsUsingFiles(file);

                //show a dialog box to inform user if ships were created or an error if file has invalid data
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                if (success) {
                    alert.setTitle("Success!!");
                    alert.setContentText("Ship position set successully.");
                } else {
                    alert.setTitle("Failure!!");
                    alert.setContentText("File is not in correct format. Fix the errors in the file");
                }
                alert.showAndWait();
            }
        });

        //add the button to root
        vbox.getChildren().add(button);

        //create and the grid that contains baord to root
        grid = new GridPane();
        vbox.getChildren().add(grid);
        VBox.setMargin(grid, new Insets(25));

        //create nad add the label which shows the winning message to root
        victoryLabel = new Label();
        victoryLabel.setFont(new Font(18));
        vbox.getChildren().add(victoryLabel);
        VBox.setMargin(victoryLabel, new Insets(10));
    }

    private void createGameUI() {

        //add row constraints
        RowConstraints growingRow = new RowConstraints();
        growingRow.setVgrow(Priority.ALWAYS);

        //add column constraints
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(10);

        //add one column constraint for number header 
        grid.getColumnConstraints().addAll(column);

        //declare a char variable to shoe  headers
        char c = 'A';
        for (int i = 1; i <= 10; i++) {
            //create the char header button
            Button xHeader = new Button(c + "");
            //add it to the grid
            grid.add(xHeader, i, 0);
            xHeader.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            //increment the char
            c++;

            //create the number header button
            Button yHeader = new Button(i + "");
            //add it to the grid
            grid.add(yHeader, 0, i);
            yHeader.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            //add the row constraint
            grid.getColumnConstraints().add(column);
            //add the header constraint
            grid.getRowConstraints().add(growingRow);
        }

        //add the buttons/blocks to the board
        c = 'A';
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                //create a button with no text
                Button boardCell = new Button();

                //set the block coordinate location as button id
                boardCell.setId(c + "" + j);

                boardCell.textProperty().bindBidirectional(board[i - 1][j - 1]);

                //make the background transparent
                boardCell.setBackground(Background.EMPTY);

                //set the button to take up entire space of the block
                boardCell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                //set event handler for button click
                boardCell.setOnAction(e -> {
                    //call the controller isHit method whenever the button is clicked
                    controller.isHit(boardCell.getId());

                    if (boardCell.getText().equals("H")) {
                        boardCell.setTextFill(Color.GREEN);
                    } else {
                        boardCell.setTextFill(Color.RED);
                    }

                    //disable the button so it cannot be selected again
                    boardCell.setDisable(true);

                    //set opacity to 1 as disabled button reduces opacity 
                    boardCell.setOpacity(1);

                    noOfTries++;

                    if (hits == 16) {
                        gameWon();
                    }
                });

                //add the border to the button
                boardCell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

                //add the button to the boards
                grid.add(boardCell, i, j);
            }
            //once a column is done, increment the char by 1
            c++;
        }
    }

    //this method is called by View class when all ships are hit
    public void gameWon() {
        //set the victory label text to green
        victoryLabel.setTextFill(Color.GREEN);

        //show the victory text with no of tries
        victoryLabel.setText("Congratulations !! You completed the game in " + noOfTries + " moves.");
    }
}
