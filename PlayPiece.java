package application;
/**
 * Represents a PlayPiece in a Checkers Game
 *
 * @author Nick Feibel, Ember Ipek
 * @version 1.5
 */
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static application.Checkers.*;

public class PlayPiece extends StackPane {

    //ELLIPSE_RADIUS_X and ELLIPSE_RADIUS_Y are the size of the ellipse's radius' in the x and y directions
    private final double ELLIPSE_RADIUS_X = 0.3;
    private final double ELLIPSE_RADIUS_Y = 0.28;

    //type is the PieceType of the PlayPiece
    private PieceType type;

    //xLocation and yLocation are the location of the PlayPiece in the 2D array
    private int xLocation;
    private int yLocation;

    /**
     * Constructor for PlayPiece
     *
     * @param type represents the type of Piece this PlayPiece is
     * @param xLocation is the location on the x-axis for this PlayPiece
     * @param yLocation is the location on the y-axis for this PlayPiece
     */
    public PlayPiece(PieceType type, int xLocation, int yLocation) {
        this.type = type;
        this.xLocation = xLocation;
        this.yLocation = yLocation;

        //Places the PlayPiece in its corresponding location
        relocate(xLocation * Checkers.SIZEOFTILES, yLocation * Checkers.SIZEOFTILES);

        //Then the ellipse is initialized to draw the PlayPiece object on the board
        //background is the shadow of the ellipse to give it some 3D likeness. Then the
        //ellipse representation of the PlayPiece is drawn to the board.
        Ellipse ellipse = createEllipse();
        Ellipse background = createBackround();
        getChildren().addAll(background, ellipse);

        //Then the PlayPiece is primed for when the mouse clicks on it.
        pieceActionOnMouseClicked();
    }

    /**
     * Moves the selected piece from old location to new location
     *
     * @param oldXLocation is the previous x-axis location of the PlayPiece
     * @param oldYLocation is the previous y-axis location of the PlayPiece
     * @param newXLocation is the new x-axis location of the PlayPiece
     * @param newYLocation is the new y-axis location of the PlayPiece
     */
    static void movePiece(int oldXLocation, int oldYLocation, int newXLocation, int newYLocation) {

    	//Moves the pieceSelected to the corresponding new x and y location and opacity set to normal
    	pieceSelected.relocate(SIZEOFTILES * newXLocation, SIZEOFTILES * newYLocation);
    	pieceSelected.setPieceY(newYLocation);
    	pieceSelected.setPieceX(newXLocation);
    	pieceSelected.setOpacity(1.0);

    	//The board position for the previous location of the pieceSelected is confirmed to be null
        board[oldYLocation][oldXLocation].setPiece(null);

        //The tile that the pieceSelected moves to has the piece set onto it
        tileClicked.setPiece(pieceSelected);

        //previousPieceType is updated to ensure the player that just moved cannot perform another action
        //until the next player performs an action
        previousPieceType = pieceSelected.getType();
    }

    /**
     * Lets selected piece attack from old location to new location
     *
     * @param oldXLocation is the previous x-axis location of the PlayPiece
     * @param oldYLocation is the previous y-axis location of the PlayPiece
     * @param newXLocation is the new x-axis location of the PlayPiece
     * @param newYLocation is the new y-axis location of the PlayPiece
     */
    static void attackingPieceMove(int oldXLocation, int oldYLocation, int newXLocation, int newYLocation) {

    	//Confirms the x and y positions of the PlayPiece attacked by this PlayPiece
    	int xPieceAttacked = oldXLocation + (newXLocation - oldXLocation) / 2;
        int yPieceAttacked = oldYLocation + (newYLocation - oldYLocation) / 2;

        //Checks if the board has a PlayPiece on its Tile
        if (board[yPieceAttacked][xPieceAttacked].hasPiece()) {

            //If there is a PlayPiece on the tile, it can be checked for removal
            PlayPiece pieceAttacked = board[yPieceAttacked][xPieceAttacked].getPiece();

            //If the current PlayPiece is not the same color as the pieceAttacked, it can be removed.
            //The check below implements the MOVEDIRECTION where RED is positive, and BLUE is negative
            //allowing for a simple check of whether they are different by whether the multiple of the two
            //provide a negative number.
            if (pieceAttacked.getType().MOVEDIRECTION*pieceSelected.getType().MOVEDIRECTION<0) {

            	//Removes the pieceAttacked from the board by setting the position to null and removing
            	//it from the total pieces display. Then the current PlayPiece is moved to the new location.
                board[yPieceAttacked][xPieceAttacked].setPiece(null);
                pieces.getChildren().remove(pieceAttacked);
                movePiece(oldXLocation, oldYLocation, newXLocation, newYLocation);

                //Score is then updated
                if(pieceSelected.getType().MOVEDIRECTION > 0){
                	redWins++;
                	Checkers.updateScore();
                }
                else{
                	blueWins++;
                	Checkers.updateScore();
                }

                //pieceWasAttacked updated to true to ensure chance for multiple attacks
                pieceWasAttacked = true;
            }
        }
    }

    /**
     * Sets the piece position to (x,y)
     *
     * @param x is the x-axis position to move to
     * @param y is the y-axis position to move to
     */
    static void setPiecePosition(int x, int y) {

    	//Sets up the Tiles on the board array then adds them to the tiles group
        board[y][x] = new Tile(((x + y) % 2 == 0), x, y);
        tiles.getChildren().add(board[y][x]);

        //Below if statements places the pieces in their respective starting spaces based on color
        PlayPiece piece = null;
        if (y <= 2 && (x + y) % 2 != 0) {
            piece = new PlayPiece(PieceType.RED, x, y);
        }
        if (y >= 5 && (x + y) % 2 != 0) {
            piece = new PlayPiece(PieceType.BLUE, x, y);
        }

        //If statement below checks for non-empty spaces and adds the pieces onto the board and
        //adds them to the pieces group to be drawn.
        if (piece != null) {
            board[y][x].setPiece(piece);
            pieces.getChildren().add(piece);
        }
    }

    /**
     * Creates the PlayPiece ellipse visual object
     *
     * @return ellipse representation of the PlayPiece
     */
    private Ellipse createEllipse(){

    	//Sets up the basic ellipse shape based on the confirmed radii
        Ellipse ellipse = new Ellipse(SIZEOFTILES * ELLIPSE_RADIUS_X, SIZEOFTILES * ELLIPSE_RADIUS_Y);

        //Simple if else checks for which PieceType the PlayPiece is and sets the corresponding fill color
        if(type == PieceType.RED){
            ellipse.setFill(Color.RED);
        }
        else if(type == PieceType.BLUE){
            ellipse.setFill(Color.BLUE);
        }
        else if(type == PieceType.REDKING){
        	ellipse.setFill(Color.DARKRED);
        }
        else{
        	ellipse.setFill(Color.DARKBLUE);
        }

        //The outline is setup for the ellipse
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(SIZEOFTILES * 0.03);

        //Ending with the location confirmed for the ellipse
        ellipse.setTranslateX((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_X * 2) / 2);
        ellipse.setTranslateY((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_Y * 2) / 2);
        return ellipse;
    }

    /**
     * Creates raised visual for the PlayPiece
     *
     * @return ellipse representation of the PlayPiece shadow
     */
    private Ellipse createBackround(){

    	//Nearly identical implementation to the createEllipse method, except BLACK is the only
    	//color needed and the positioning is dropped to make the PlayPiece appear 3D.
        Ellipse ellipse = new Ellipse(SIZEOFTILES * ELLIPSE_RADIUS_X, SIZEOFTILES * ELLIPSE_RADIUS_Y);

        ellipse.setFill(Color.BLACK);
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(SIZEOFTILES * 0.03);

        ellipse.setTranslateX((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_X * 2) / 2);
        ellipse.setTranslateY(((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_Y * 2) / 2) + SIZEOFTILES * 0.07);
        return ellipse;
    }

    /**
     * Highlights selected PlayPiece when the mouse selects it
     */
    private void pieceActionOnMouseClicked() {

    	//Creates a new MouseEvent for when the mouse is clicked on the PlayPiece
        this.setOnMouseClicked(e -> {

            //A tempPiece is set to the current PlayPiece that was clicked
            PlayPiece tempPiece = (PlayPiece) e.getSource();

            //If else if statement checks whether the piece is a legal piece to select and if so
            //changes its opacity. If it is the same piece that was used in the attack it does not update
            //another pieces opacity as it is not needed.
            if (firstTurn || (tempPiece.getType().MOVEDIRECTION*previousPieceType.MOVEDIRECTION<0)) {

            	pieceSelected = tempPiece;
                isPieceSelected = true;
                pieceSelected.setOpacity(0.5);

                if (previousPiece != null) {
                	pieceSelected.setOpacity(0.5);
                    previousPiece.setOpacity(1.0);
                }
                previousPiece = pieceSelected;
            }
            else if(pieceWasAttacked){

            	pieceSelected = tempPiece;
                isPieceSelected = true;

                if (previousPiece != null && previousPiece.samePiece(pieceSelected)) {
                	pieceSelected.setOpacity(0.5);
                }
                previousPiece = pieceSelected;
            }
        });
    }

    /**
     * Getter for x location of a PlayPiece
     *
     * @return int of the x-axis location
     */
    public int getPieceX() {
        return xLocation;
    }

    /**
     * Getter for y location of a PlayPiece
     *
     * @return int of the y-axis location
     */
    public int getPieceY() {
        return yLocation;
    }

    /**
     * Setter for x location of a PlayPiece
     *
     * @param x represents the x-axis location
     */
    public void setPieceX(int x) {
        this.xLocation = x;
    }

    /**
     * Setter for y location of a PlayPiece
     *
     * @param y represents the y-axis location
     */
    public void setPieceY(int y) {
        this.yLocation = y;
    }

    /**
     * Getter for the type of PlayPiece
     *
     * @return type
     */
    public PieceType getType() {
        return type;
    }
    /**
     * Setter for the type of PlayPiece
     *
     * @param type
     */
    public void setType(PieceType type) {
        this.type = type;
    }
    
    /**
     * Checks whether a PlayPiece is the same as another Piece
     *
     * @param otherPiece is the other PlayPiece to compare this PlayPiece with
     * @return boolean of whether it is the same PlayPiece
     */
    public boolean samePiece(PlayPiece otherPiece){
    	if(this.xLocation == otherPiece.getPieceX() && this.yLocation == otherPiece.getPieceY()){
    		return true;
    	}
    	return false;
    }
}
