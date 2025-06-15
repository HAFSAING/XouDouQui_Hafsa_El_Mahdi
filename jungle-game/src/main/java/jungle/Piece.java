package jungle;

public class Piece {

    private final Animal animal;
    private final Player owner;

    public Piece(Animal animal, Player owner) {
        this.animal = animal;
        this.owner = owner;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Player getOwner() {
        return owner;
    }

    public int getRank() {
        return animal.getRank();
    }


    public boolean canCapture(Piece opponentPiece, boolean isOpponentInTrap) {
        if (isOpponentInTrap) {
            return true;
        }

        if (this.animal == Animal.RAT && opponentPiece.getAnimal() == Animal.ELEPHANT) {
            return true;
        }

        if (this.animal == Animal.ELEPHANT && opponentPiece.getAnimal() == Animal.RAT) {
            return false;
        }
        return this.getRank() >= opponentPiece.getRank();
    }
    
    @Override
    public String toString() {

        return String.format("%-4s", this.animal.getName().substring(0, 3)) + this.owner.getId();
    }
}
