package application;
/**
 * Represents a PlayPiece in a Checkers Game
 * @author Nick Feibel, Ember Ipek
 * @version 1.0
 */
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static application.Checkers.*;

public class PlayPiece extends StackPane {

    private final double ELLIPSE_RADIUS_X = 0.3;
    private final double ELLIPSE_RADIUS_Y = 0.28;
    private PieceType type;
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

        relocate(xLocation * Checkers.SIZEOFTILES, yLocation * Checkers.SIZEOFTILES);

        Ellipse ellipse = createEllipse();
        Ellipse background = createBackround();

        getChildren().addAll(background, ellipse);

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
    	pieceSelected.relocate(SIZEOFTILES * newXLocation, SIZEOFTILES * newYLocation);
    	pieceSelected.setPieceY(newYLocation);
    	pieceSelected.setPieceX(newXLocation);
        board[oldYLocation][oldXLocation].setPiece(null);
        tileClicked.setPiece(pieceSelected);
        previousPieceType = pieceSelected.getType();
        pieceSelected.setOpacity(1.0);
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
        int xPieceAttacked = oldXLocation + (newXLocation - oldXLocation) / 2;
        int yPieceAttacked = oldYLocation + (newYLocation - oldYLocation) / 2;
        if (board[yPieceAttacked][xPieceAttacked].hasPiece()) {
            PlayPiece pieceAttacked = board[yPieceAttacked][xPieceAttacked].getPiece();
            if (pieceAttacked.getType() != pieceSelected.getType()) {
                board[yPieceAttacked][xPieceAttacked].setPiece(null);
                pieces.getChildren().remove(pieceAttacked);
                movePiece(oldXLocation, oldYLocation, newXLocation, newYLocation);
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

        board[y][x] = new Tile(((x + y) % 2 == 0), x, y);
        tiles.getChildren().add(board[y][x]);

        PlayPiece piece = null;
        if (y <= 2 && (x + y) % 2 != 0) {
            piece = new PlayPiece(PieceType.RED, x, y);
        }
        if (y >= 5 && (x + y) % 2 != 0) {
            piece = new PlayPiece(PieceType.BLUE, x, y);
        }

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
        Ellipse ellipse = new Ellipse(SIZEOFTILES * ELLIPSE_RADIUS_X, SIZEOFTILES * ELLIPSE_RADIUS_Y);
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
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(SIZEOFTILES * 0.03);

        ellipse.setTranslateX((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_X * 2) / 2);
        ellipse.setTranslateY((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_Y * 2) / 2);
        return ellipse;
    }

    /**
     * Creates drop shadow for the PlayPiece
     *
     * @return ellipse representation of the PlayPiece shadow
     */
    private Ellipse createBackround(){
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
        this.setOnMouseClicked(e -> {
            PlayPiece tempPiece = (PlayPiece) e.getSource();
            if (tempPiece.getType() != previousPieceType && tempPiece != previousPiece) {
            	pieceSelected = tempPiece;
                isPieceSelected = true;
                pieceSelected.setOpacity(0.5);
                if (previousPiece != null) {
                	pieceSelected.setOpacity(0.5);
                    previousPiece.setOpacity(1.0);
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
     * setter for the type of PlayPiece
     *
     * @param type
     */
    public void setType(PieceType type) {
        this.type = type;
    }
}
