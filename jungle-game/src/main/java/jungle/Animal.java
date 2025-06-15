package jungle;

/**
 * Énumération représentant les types de pièces (animaux) et leur hiérarchie.
 * Le rang 8 est le plus fort (Éléphant) et le rang 1 le plus faible (Rat).
 */
public enum Animal {
    ELEPHANT("Elephant", 8),
    LION("Lion", 7),
    TIGRE("Tigre", 6),
    PANTHERE("Panthere", 5),
    CHIEN("Chien", 4),
    LOUP("Loup", 3),
    CHAT("Chat", 2),
    RAT("Rat", 1);

    private final String name;
    private final int rank;

    Animal(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }
}