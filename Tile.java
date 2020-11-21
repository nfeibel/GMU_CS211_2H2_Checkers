package application;
/**
 * Represents the Types of Pieces
 * @author Nick Feibel, Ember Ipek
 * @version 1.0
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
        setWidth(SIZEOFTILES);
        setHeight(SIZEOFTILES);
        this.x = x;
        this.y = y;

        relocate(x * SIZEOFTILES, y * SIZEOFTILES);
        if(backgroundColor){
            setFill(Color.BLACK);
        }
        else{
            setFill(Color.WHITE);
        }
        setTileAction();
    }

    /**
     * Sets how the new Tile should look
     */
    private void setTileAction(){
        setOnMouseClicked(e -> {
            tileClicked = (Tile) e.getSource();
            pieceAction();
        });
    }

    /**
     * Confirms whether the Tile has a piece
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
