package application;
/**
 * Represents the Types of Pieces
 *
 * @author Nick Feibel, Ember Ipek
 * @version 1.5
 */
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static application.Checkers.*;

public class Tile extends Rectangle {

    private PlayPiece piece;
    private int x;
    private int y;

    /**
     * Constructor to set color and (x,y) location of a Tile
     *
     * @param backgroundColor which was a boolean representation of which color the Tile is true for black, false for white
     * @param x represents the x-axis location of the tile
     * @param y represents the y-axis location of the tile
     */
    public Tile(boolean backgroundColor, int x, int y) {

    	//Sets the Tile's width and height using the Rectangle's methods
        setWidth(SIZEOFTILES);
        setHeight(SIZEOFTILES);
        this.x = x;
        this.y = y;

        //Sets the position for the Tile
        relocate(x * SIZEOFTILES, y * SIZEOFTILES);

        //If statement sets the fill based on which backgroundColor value is true or false, allowing
        //for an alternating checkerboard pattern for the board.
        if(backgroundColor){
            setFill(Color.BLACK);
        }
        else{
            setFill(Color.WHITE);
        }

        //Prime the Tile for an action when clicked on
        setTileAction();
    }

    /**
     * Sets how the new Tile should look
     */
    private void setTileAction(){

    	//When the Tile is clicked, it confirms the source and proceeds with the pieceAction() method
    	//in PlayPiece to allow for a player action.
        setOnMouseClicked(e -> {
            tileClicked = (Tile) e.getSource();
            pieceAction();
        });
    }

    /**
     * Confirms whether the Tile has a piece
     *
     * @return true if Tile has a piece, false otherwise
     */
    boolean hasPiece() {
        return piece != null;
    }

    /**
     * Getter for piece
     *
     * @return piece
     */
    public PlayPiece getPiece() {
        return piece;
    }

    /**
     * Getter for x coordinate
     *
     * @return x location
     */
    public int getTileX() {
        return x;
    }

    /**
     * Getter for y coordinate
     *
     * @return y location
     */
    public int getTileY() {
        return y;
    }

    /**
     * Setter for piece
     *
     * @param piece the current PlayPiece on the current Tile
     */
    public void setPiece(PlayPiece piece) {
        this.piece = piece;
    }
}
