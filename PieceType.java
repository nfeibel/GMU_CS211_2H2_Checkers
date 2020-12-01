package application;
/**
 * Represents the Types of Pieces
 *
 * @author Nick Feibel, Ember Ipek
 * @version 1.5
 */
public enum PieceType {

    //KING subtypes of the RED/BLUE PieceTypes are same positive/negative, but have a higher magnitude
    REDKING(2), RED(1), BLUE(-1), BLUEKING(-2);

    //MOVEDIRECTION allows for if statements for various move checks
    final int MOVEDIRECTION;

    /*
     * Constructor for PieceType, and initializes MOVEDIRECTION for Type
     */
    PieceType(int MOVEDIRECTION){
        this.MOVEDIRECTION = MOVEDIRECTION;
    }

}
