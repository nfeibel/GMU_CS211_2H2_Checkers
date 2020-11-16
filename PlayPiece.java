
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class PlayPiece extends StackPane {

    private final double ELLIPSE_RADIUS_X = 0.3;
    private final double ELLIPSE_RADIUS_Y = 0.28;
    private PieceType type;
    private int xLocation;
    private int yLocation;

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

    static void movePiece(int oldXLocation, int oldYLocation, int newXLocation, int newYLocation) {
    	pieceSelected.relocate(SIZEOFTILES * newXLocation, SIZEOFTILES * newYLocation);
    	pieceSelected.setPieceY(newYLocation);
    	pieceSelected.setPieceX(newXLocation);
        board[oldYLocation][oldXLocation].setPiece(null);
        tileClicked.setPiece(pieceSelected);
        previousPieceType = pieceSelected.getType();
        pieceSelected.setOpacity(1.0);
    }

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

    static void setPiecePosition(int x, int y) {

        board[y][x] = new Tile(((x + y) % 2 == 0), x, y);
        tiles.getChildren().add(board[y][x]);

        PlayPiece piece = null;
        if (y <= 2 && (x + y) % 2 != 0) {
            piece = new PlayPiece(PieceType.RED, x, y);
        }
        if (y >= 5 && (x + y) % 2 != 0) {
            piece = new PlayPiece(PieceType.WHITE, x, y);
        }

        if (piece != null) {
            board[y][x].setPiece(piece);
            pieces.getChildren().add(piece);
        }
    }

    private Ellipse createEllipse(){
        Ellipse ellipse = new Ellipse(SIZEOFTILES * ELLIPSE_RADIUS_X, SIZEOFTILES * ELLIPSE_RADIUS_Y);
        if(type == PieceType.RED){
            ellipse.setFill(Color.RED);
        }
        else{
            ellipse.setFill(Color.BLUE);
        }
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(SIZEOFTILES * 0.03);

        ellipse.setTranslateX((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_X * 2) / 2);
        ellipse.setTranslateY((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_Y * 2) / 2);
        return ellipse;
    }

    private Ellipse createBackround(){
        Ellipse ellipse = new Ellipse(SIZEOFTILES * ELLIPSE_RADIUS_X, SIZEOFTILES * ELLIPSE_RADIUS_Y);
        ellipse.setFill(Color.BLACK);
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(SIZEOFTILES * 0.03);

        ellipse.setTranslateX((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_X * 2) / 2);
        ellipse.setTranslateY(((SIZEOFTILES - SIZEOFTILES * ELLIPSE_RADIUS_Y * 2) / 2) + SIZEOFTILES * 0.07);
        return ellipse;
    }

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

    public int getPieceX() {
        return xLocation;
    }

    public int getPieceY() {
        return yLocation;
    }

    public void setPieceX(int x) {
        this.xLocation = x;
    }

    public void setPieceY(int y) {
        this.yLocation = y;
    }

    public PieceType getType() {
        return type;
    }
}