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
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static application.PlayPiece.*;

public class Checkers extends Application {

    public static final int SIZEOFTILES = 100;
    public static final int NUMCOLUMNS = 8;
    public static final int NUMROWS = 8;
    static int redWins = 0;
    static int blueWins = 0;
    static PieceType previousPieceType = PieceType.RED;
    static PlayPiece previousPiece = null;
    static boolean isPieceSelected = false;
    static boolean endGameSelected = false;
    static boolean pieceAttacked = false;
    static boolean firstTurn = true;
    static PlayPiece pieceSelected;
    static PlayPiece previousPieceSelected;
    static Tile tileClicked;
    private static Pane window;
    private static Button endGame;
    static Text redText;
    static Text blueText;

    static Tile[][] board = new Tile[NUMCOLUMNS][NUMROWS];
    static Group tiles = new Group();
    static Group pieces = new Group();

    /**
     * Creates the Pane window which represents a Game Board and sets up the PlayPieces
     */
    private Parent createBoard() {
        window = new Pane();
        window.setPrefSize(NUMCOLUMNS * SIZEOFTILES,(NUMROWS+2) * SIZEOFTILES);
        window.getChildren().addAll(tiles,pieces);
        updateScore();
        endGame = new Button("End Game");
        endGame.setTranslateX(NUMCOLUMNS/2.3 * SIZEOFTILES+1);
        endGame.setTranslateY((NUMROWS+1.5) * SIZEOFTILES-5);
        window.getChildren().addAll(endGame);
        for (int y = 0; y < NUMCOLUMNS; y++) {
            for (int x = 0; x < NUMROWS; x++) {
                PlayPiece.setPiecePosition(x, y);
            }
        }

        return window;
    }

    /**
     * Updates the Pane window with the updated score count
     */
    public static void updateScore(){
    	if(redWins != 0 || blueWins != 0){
    		window.getChildren().remove(redText);
    		window.getChildren().remove(blueText);
    	}
    	redText = new Text(1,(NUMROWS+1) * SIZEOFTILES-5, "Red Score: "+String.valueOf(redWins));
        redText.setFill(Color.RED);
        redText.setFont(Font.font("Verdana", FontWeight.BOLD,50));
        blueText = new Text(NUMCOLUMNS/2 * SIZEOFTILES+1,(NUMROWS+1) * SIZEOFTILES-5, "Blue Score: "+String.valueOf(blueWins));
        blueText.setFill(Color.BLUE);
        blueText.setFont(Font.font("Verdana", FontWeight.BOLD,50));
        window.getChildren().add(redText);
        window.getChildren().add(blueText);
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
    	endGame.setOnAction(e -> {
    		isItOver();
    	});
    	
        if (isPieceSelected && ((pieceSelected.getType() != previousPieceType || pieceWasAttacked) || firstTurn)) {
            int oldX = pieceSelected.getPieceX();
            int oldY = pieceSelected.getPieceY();
            int newX = tileClicked.getTileX();
            int newY = tileClicked.getTileY();

            if (firstTurn || (Math.abs(newX - oldX) == 1 && ((newY - oldY) == pieceSelected.getType().MOVEDIRECTION || pieceSelected.getType()==PieceType.REDKING || pieceSelected.getType()==PieceType.BLUEKING)  && !tileClicked.hasPiece() && !pieceSelected.samePiece(previousPieceSelected))){
                firstTurn = false;
            	pieceWasAttacked = false;
            	movePiece(oldX, oldY, newX, newY);
                endGameSelected =false;

            } else if (Math.abs(newX - oldX) == 2 && ((newY - oldY) == 2 * pieceSelected.getType().MOVEDIRECTION || pieceSelected.getType()==PieceType.REDKING || pieceSelected.getType()==PieceType.BLUEKING) && !tileClicked.hasPiece()) {
            	attackingPieceMove(oldX, oldY, newX, newY);
                endGameSelected =false;
            }
            if(pieceSelected.getType() == PieceType.RED && newY == 7){
            	pieces.getChildren().remove(pieceSelected);
            	pieces.getChildren().add(new PlayPiece(PieceType.REDKING, newX,newY));
            	endGameSelected =false;
            }
            else if(pieceSelected.getType() == PieceType.BLUE && newY == 0){
            	pieces.getChildren().remove(pieceSelected);
            	pieces.getChildren().add(new PlayPiece(PieceType.BLUEKING, newX,newY));
            	endGameSelected =false;
            }
            previousPieceSelected = pieceSelected;
        }
        
    }

    /**
     * Checks whether the game is over, which is confirmed by both players selecting the End Game button.
     * If the game is ended, the winner is confirmed, or a tie is confirmed.
     */
    private static void isItOver(){
    	if(endGameSelected){
			Rectangle done = new Rectangle(0,0, SIZEOFTILES*(NUMROWS*2),SIZEOFTILES*(NUMCOLUMNS*2));
			done.setFill(Color.BLACK);
			window.getChildren().add(done);
			if(redWins>blueWins){
				Text winText1 = new Text(SIZEOFTILES*(NUMCOLUMNS/2.7), SIZEOFTILES*4, "RED");
				Text winText2 = new Text(SIZEOFTILES*(NUMCOLUMNS/2.99), SIZEOFTILES*5, "WINS");
				winText1.setFont(Font.font("Verdana", FontWeight.BOLD,100));
				winText2.setFont(Font.font("Verdana", FontWeight.BOLD,100));
				winText1.setFill(Color.CRIMSON);
				winText2.setFill(Color.CRIMSON);
				window.getChildren().add(winText1);
				window.getChildren().add(winText2);
			}
			else if(redWins<blueWins){
				Text winText1 = new Text(SIZEOFTILES*(NUMCOLUMNS/2.85), SIZEOFTILES*4, "BLUE");
				Text winText2 = new Text(SIZEOFTILES*(NUMCOLUMNS/2.99), SIZEOFTILES*5, "WINS");
				winText1.setFont(Font.font("Verdana", FontWeight.BOLD,100));
				winText2.setFont(Font.font("Verdana", FontWeight.BOLD,100));
				winText1.setFill(Color.ROYALBLUE);
				winText2.setFill(Color.ROYALBLUE);
				window.getChildren().add(winText1);
				window.getChildren().add(winText2);
			}
			else{
				Text tieText1 = new Text(SIZEOFTILES*(NUMCOLUMNS/2.85), SIZEOFTILES*4, "IT IS");
				Text tieText2 = new Text(SIZEOFTILES*(NUMCOLUMNS/2.85), SIZEOFTILES*5, "A TIE");
				tieText1.setTextAlignment(TextAlignment.CENTER);
				tieText2.setTextAlignment(TextAlignment.CENTER);
				tieText1.setFill(Color.BURLYWOOD);
				tieText2.setFill(Color.BURLYWOOD);
				tieText1.setFont(Font.font("Verdana", FontWeight.BOLD,100));
				tieText2.setFont(Font.font("Verdana", FontWeight.BOLD,100));
				window.getChildren().add(tieText1);
				window.getChildren().add(tieText2);
			}
		}
		else{
			endGameSelected =true;
		}
    }

    /**
     * Launches the Checkers Game
     */
    public static void main(String[] args) {
        launch(args);
    }
}
