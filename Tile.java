package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static application.Checkers.*;

public class Tile extends Rectangle {

    private PlayPiece piece;
    private int x;
    private int y;

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

    private void setTileAction(){
        setOnMouseClicked(e -> {
            tileClicked = (Tile) e.getSource();
            pieceAction();
        });
    }

    boolean hasPiece() {
        return piece != null;
    }

    public PlayPiece getPiece() {
        return piece;
    }

    public int getTileX() {
        return x;
    }

    public int getTileY() {
        return y;
    }


    public void setPiece(PlayPiece piece) {
        this.piece = piece;
    }
}