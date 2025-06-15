package jungle;

public class Square {

    public enum SquareType {
        NORMAL,
        RIVIERE,    
        PIEGE,      
        SANCTUAIRE  
    }

    private final SquareType type;
    private Piece piece;
    private Player trapOwner; 

    public Square(SquareType type) {
        this.type = type;
        this.piece = null;
        this.trapOwner = null;
    }
    

    public Square(SquareType type, Player trapOwner) {
        this.type = type;
        this.piece = null;
        this.trapOwner = trapOwner;
    }


    public SquareType getType() { return type; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
    public Player getTrapOwner() { return trapOwner; }

    public boolean isOccupied() {
        return this.piece != null;
    }
}
