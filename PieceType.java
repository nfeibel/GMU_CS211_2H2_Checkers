package application;
/**
 * Represents the Types of Pieces
 * @author Nick Feibel, Ember Ipek
 * @version 1.0
 */
public enum PieceType {
    REDKING(2), RED(1), BLUE(-1), BLUEKING(-2);

    final int MOVEDIRECTION;

    /*
     * Constructor for PieceType, and initializes MOVEDIRECTION for Type
     */
    PieceType(int MOVEDIRECTION){
        this.MOVEDIRECTION = MOVEDIRECTION;
    }

}
