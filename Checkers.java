package application;

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
    static PieceType previousPieceType = PieceType.RED;
    static PlayPiece previousPiece = null;
    static boolean isPieceSelected = false;
    static PlayPiece pieceSelected;
    static PlayPiece previousPieceSelected;
    static Tile tileClicked;

    static Tile[][] board = new Tile[NUMCOLUMNS][NUMROWS];
    static Group tiles = new Group();
    static Group pieces = new Group();

    /*
     * Creates the Game Board Window and sets up the PlayPieces
     */
    private Parent createBoard() {
        Pane window = new Pane();
        window.setPrefSize(NUMCOLUMNS * SIZEOFTILES,NUMROWS * SIZEOFTILES);
        window.getChildren().addAll(tiles,pieces);

        for (int y = 0; y < NUMCOLUMNS; y++) {
            for (int x = 0; x < NUMROWS; x++) {
                PlayPiece.setPiecePosition(x, y);
            }
        }
        return window;
    }
    
 
    @Override
    /*
     * Sets up the game
     * @param primaryStage is the stage for the window
     */
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createBoard());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /*
     * Performs a pieceAction as long as the current piece selected is not the previousPieceType
     * @return boolean whether an attack was made.
     */
    static boolean pieceAction() {
    	previousPieceSelected = pieceSelected;
        if (isPieceSelected && pieceSelected.getType() != previousPieceType) {
            int oldX = pieceSelected.getPieceX();
            int oldY = pieceSelected.getPieceY();
            int newX = tileClicked.getTileX();
            int newY = tileClicked.getTileY();

            if (Math.abs(newX - oldX) == 1 && (newY - oldY) == pieceSelected.getType().MOVEDIRECTION && !tileClicked.hasPiece()) {
                movePiece(oldX, oldY, newX, newY);
                return false;

            } else if (Math.abs(newX - oldX) == 2 && (newY - oldY) == 2 * pieceSelected.getType().MOVEDIRECTION && !tileClicked.hasPiece()) {
                attackingPieceMove(oldX, oldY, newX, newY);
                return true;
            }
        }
        return false;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
