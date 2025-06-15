package jungle;

/**
 * Représente une pièce du jeu.
 * Chaque pièce a un type d'animal et appartient à un joueur.
 */
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

    /**
     * Détermine si cette pièce peut capturer une autre pièce ennemie.
     * @param opponentPiece La pièce ennemie.
     * @param isOpponentInTrap Si la pièce ennemie est sur une case piège du joueur courant.
     * @return true si la capture est possible.
     */
    public boolean canCapture(Piece opponentPiece, boolean isOpponentInTrap) {
        // If opponent is in one of our traps, it can be captured by any of our pieces.
        if (isOpponentInTrap) {
            return true;
        }

        // Special case: Rat captures Elephant, but not from the water.
        if (this.animal == Animal.RAT && opponentPiece.getAnimal() == Animal.ELEPHANT) {
            return true;
        }

        // Special case: Elephant cannot capture Rat
        if (this.animal == Animal.ELEPHANT && opponentPiece.getAnimal() == Animal.RAT) {
            return false;
        }

        // General case: can capture pieces of equal or lower rank
        return this.getRank() >= opponentPiece.getRank();
    }
    
    @Override
    public String toString() {
        // Renvoie une représentation courte pour l'affichage console.
        // ex: "Lio1" pour le Lion du joueur 1.
        return String.format("%-4s", this.animal.getName().substring(0, 3)) + this.owner.getId();
    }
}