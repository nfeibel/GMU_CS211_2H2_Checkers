package application;
/**
 * Represents the Types of Pieces
 * @author Nick Feibel, Ember Ipek
 * @version 1.0
 */
public enum PieceType {
    RED(1), WHITE(-1);

    final int MOVEDIRECTION;

    /*
     * Constructor for PieceType, and initializes MOVEDIRECTION for Type
     */
    PieceType(int MOVEDIRECTION){
        this.MOVEDIRECTION = MOVEDIRECTION;
    }

}
