package application;
/**
 * Represents a Checkers Game
 * @author Nick Feibel, Ember Ipek
 * @version 1.5
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

	//SIZEOFTILES represents the size of the tiles in pixels
	//NUMCOLUMNS and NUMROWS represents the size of the game board
    public static final int SIZEOFTILES = 100;
    public static final int NUMCOLUMNS = 8;
    public static final int NUMROWS = 8;

    //redWins and blueWins tracks the amount of captured pieces
    static int redWins = 0;
    static int blueWins = 0;

    //previousPieceType and previousPiece track the previousPiece moved and its type
    //previousPieceSelected is the previous PlayPiece that was selected
    static PieceType previousPieceType = null;
    static PlayPiece previousPiece = null;
    static PlayPiece previousPieceSelected;

    //isPieceSelected confirms whether a piece is being selected
    static boolean isPieceSelected = false;

    //endGameSelected confirms whether the previous player selected the End Game button
    static boolean endGameSelected = false;

    //pieceAttacked confirms whether a piece was attacked during the previous turn
    static boolean pieceAttacked = false;

    //firstTurn confirms whether it is the first turn of the game
    static boolean firstTurn = true;

    //pieceSelected confirms the current piece selected
    //tileClicked confirms the current tile that was selected
    static PlayPiece pieceSelected;
    static Tile tileClicked;

    //window is the Pane that the Checkers game plays on
    //endGame is the Button that the players can select to end the game
    private static Pane window;
    private static Button endGame;

    //redText and blueText are the score text provided at the bottom of the game
    static Text redText;
    static Text blueText;

    //board is the playing board represented as a 2D array
    static Tile[][] board = new Tile[NUMCOLUMNS][NUMROWS];

    //tiles and pieces are the Group of tiles and pieces to be added to the window
    static Group tiles = new Group();
    static Group pieces = new Group();

    /**
     * Creates the Pane window which represents a Game Board and sets up the PlayPieces
     */
    private Parent createBoard() {

    	//Below the window is setup with all the tiles and the endGame button
        window = new Pane();
        window.setPrefSize(NUMCOLUMNS * SIZEOFTILES,(NUMROWS+2) * SIZEOFTILES);
        window.getChildren().addAll(tiles,pieces);
        updateScore();
        endGame = new Button("End Game");
        endGame.setTranslateX(NUMCOLUMNS/2.3 * SIZEOFTILES+1);
        endGame.setTranslateY((NUMROWS+1.5) * SIZEOFTILES-5);
        window.getChildren().addAll(endGame);

        //Then the PlayPieces positions confirmed for the board
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

    	//If it is not the firstTurn, the redText and blueText needs to be removed to be updated
    	//without numbers overlaying eachother
    	if(!firstTurn){
    		window.getChildren().remove(redText);
    		window.getChildren().remove(blueText);
    	}

    	//redText and blueText setup respectively below and added back to the windo
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

    	//First it is checked whether the endGame button was selected and if so, runs isItOver() method
    	endGame.setOnAction(e -> {
    		isItOver();
    	});

    	//Below it is checked whether the piece selected is an allowed piece to be selected.
    	//firstTurn being true allows either red or blue pieces to be selected.
        if (isPieceSelected && ((pieceSelected.getType() != previousPieceType || pieceWasAttacked) || firstTurn)) {
            int oldX = pieceSelected.getPieceX();
            int oldY = pieceSelected.getPieceY();
            int newX = tileClicked.getTileX();
            int newY = tileClicked.getTileY();

            //Below it is checked whether the player moves or attacks another piece using the pieceSelected.
            //If neither, nothing occurs until a legal option is made by the player.
            if ((Math.abs(newX - oldX) == 1 && firstTurn) || (Math.abs(newX - oldX) == 1 && ((newY - oldY) == pieceSelected.getType().MOVEDIRECTION || pieceSelected.getType()==PieceType.REDKING || pieceSelected.getType()==PieceType.BLUEKING)  && !tileClicked.hasPiece() && !pieceSelected.samePiece(previousPieceSelected))){
                firstTurn = false;
            	pieceWasAttacked = false;
            	movePiece(oldX, oldY, newX, newY);
                endGameSelected =false;

            } else if (Math.abs(newX - oldX) == 2 && ((newY - oldY) == 2 * pieceSelected.getType().MOVEDIRECTION || pieceSelected.getType()==PieceType.REDKING || pieceSelected.getType()==PieceType.BLUEKING) && !tileClicked.hasPiece()) {
            	attackingPieceMove(oldX, oldY, newX, newY);
                endGameSelected =false;
            }

            //Below it is checked whether any piece reached the end of the board which allows it to
            //become a king piece. This allows the piece to move in either positive or negative MOVEDIRECTION.
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

    	//Below it is checked whether the endGame button was selected during the current turn, if not
    	//it confirms a player initiated the End Game option and allows the other player to press it.
    	if(endGameSelected){

    		//If the endGame button was clicked twice, the end game screen will be setup using the below
			Rectangle done = new Rectangle(0,0, SIZEOFTILES*(NUMROWS*2),SIZEOFTILES*(NUMCOLUMNS*2));
			done.setFill(Color.BLACK);
			window.getChildren().add(done);

			//If statement checks for who the winner is, and depending on who wins, or whether it is a tie,
			//displays the results on the screen.
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
