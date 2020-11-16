
public enum PieceType {
    RED(1), WHITE(-1);

    final int MOVEDIRECTION;

    PieceType(int MOVEDIRECTION){
        this.MOVEDIRECTION = MOVEDIRECTION;
    }

}