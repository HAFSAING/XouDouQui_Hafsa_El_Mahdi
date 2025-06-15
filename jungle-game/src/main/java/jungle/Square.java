package jungle;

/**
 * Représente une case sur le plateau de jeu.
 * Une case a un type (Rivière, Piège, Sanctuaire) et peut contenir une pièce.
 */
public class Square {

    public enum SquareType {
        NORMAL,
        RIVIERE,    // River
        PIEGE,      // Trap
        SANCTUAIRE  // Den
    }

    private final SquareType type;
    private Piece piece;
    private Player trapOwner; // Which player's trap this is

    public Square(SquareType type) {
        this.type = type;
        this.piece = null;
        this.trapOwner = null;
    }
    
    // Constructor for traps
    public Square(SquareType type, Player trapOwner) {
        this.type = type;
        this.piece = null;
        this.trapOwner = trapOwner;
    }

    // Getters and Setters
    public SquareType getType() { return type; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
    public Player getTrapOwner() { return trapOwner; }

    public boolean isOccupied() {
        return this.piece != null;
    }
}