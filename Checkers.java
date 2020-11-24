package application;
/**
 * Represents a Checkers Game
 * @author Nick Feibel, Ember Ipek
 * @version 1.0
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static application.PlayPiece.*;

public class Checkers extends Application {

    public static final int SIZEOFTILES = 100;
    private static final int NUMCOLUMNS = 8;
    private static final int NUMROWS = 8;
    private static int redWins = 0;
    private static int blueWins = 0;
    static PieceType previousPieceType = PieceType.RED;
    static PlayPiece previousPiece = null;
    static boolean isPieceSelected = false;
    static PlayPiece pieceSelected;
    static PlayPiece previousPieceSelected;
    static Tile tileClicked;

    static Tile[][] board = new Tile[NUMCOLUMNS][NUMROWS];
    static Group tiles = new Group();
    static Group pieces = new Group();

    /**
     * Creates the Game Board Window and sets up the PlayPieces
     */
    private Parent createBoard() {
        Pane window = new Pane();
        window.setPrefSize(NUMCOLUMNS * SIZEOFTILES,NUMROWS * SIZEOFTILES);
        window.getChildren().addAll(tiles,pieces);
        Text redText = new Text(1,(NUMROWS+1) * SIZEOFTILES-5, "Red Score: "+String.valueOf(redWins));
        redText.setFill(Color.RED);
        redText.setFont(Font.font("Verdana", FontWeight.BOLD,50));
        Text blueText = new Text(NUMCOLUMNS/2 * SIZEOFTILES+1,(NUMROWS+1) * SIZEOFTILES-5, "Blue Score: "+String.valueOf(blueWins));
        blueText.setFill(Color.BLUE);
        blueText.setFont(Font.font("Verdana", FontWeight.BOLD,50));
        window.getChildren().add(redText);
        window.getChildren().add(blueText);
        for (int y = 0; y < NUMCOLUMNS; y++) {
            for (int x = 0; x < NUMROWS; x++) {
                PlayPiece.setPiecePosition(x, y);
            }
        }
        return window;
    }


    @Override
    /**
     * Sets up the game
     * @param primaryStage is the stage for the window
     */
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createBoard());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Performs a pieceAction as long as the current piece selected is not the previousPieceType
     * @return boolean whether an attack was made.
     */
    static void pieceAction() {
    	previousPieceSelected = pieceSelected;
        if (isPieceSelected && pieceSelected.getType() != previousPieceType) {
            int oldX = pieceSelected.getPieceX();
            int oldY = pieceSelected.getPieceY();
            int newX = tileClicked.getTileX();
            int newY = tileClicked.getTileY();

            if (Math.abs(newX - oldX) == 1 && (newY - oldY) == pieceSelected.getType().MOVEDIRECTION && !tileClicked.hasPiece()) {
                movePiece(oldX, oldY, newX, newY);
//                return false;

            } else if (Math.abs(newX - oldX) == 2 && (newY - oldY) == 2 * pieceSelected.getType().MOVEDIRECTION && !tileClicked.hasPiece()) {
                attackingPieceMove(oldX, oldY, newX, newY);
//                return true;
            }
            if(pieceSelected.getType() == PieceType.RED && newY == 7){
            	pieceSelected = new PlayPiece(PieceType.REDKING, newX,newY);
            }
            else if(pieceSelected.getType() == PieceType.BLUE && newY == 0){
            	pieceSelected = new PlayPiece(PieceType.BLUEKING, newX,newY);
            	previousPieceSelected = pieceSelected;
            }
        }
//        return false;
    }


    /**
     * Launches the Checkers Game
     */
    public static void main(String[] args) {
        launch(args);
    }
}
