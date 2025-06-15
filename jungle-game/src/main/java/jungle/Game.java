package jungle;

/**
 * VERSION DE DÉBOGAGE - NE PAS GARDER EN VERSION FINALE
 */
public class Game {

    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private boolean isGameOver;
    private Player winner;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board(player1, player2);
        this.currentPlayer = player1;
    }

    public void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Affiche le début de la tentative de mouvement
        System.out.println("\n--- Tentative de mouvement : " + (char)('A' + fromCol) + fromRow + " -> " + (char)('A' + toCol) + toRow + " par " + currentPlayer.getUsername() + " ---");
        
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            System.out.println(">>> RÉSULTAT FINAL: Mouvement REFUSÉ par isValidMove.");
            return false;
        }
        
        System.out.println(">>> RÉSULTAT FINAL: Mouvement ACCEPTÉ.");
        Square fromSquare = board.getSquare(fromRow, fromCol);
        Square toSquare = board.getSquare(toRow, toCol);
        toSquare.setPiece(fromSquare.getPiece());
        fromSquare.setPiece(null);
        if (isWinConditionMet(toSquare)) {
            isGameOver = true;
            winner = currentPlayer;
        } else {
            switchTurn();
        }
        return true;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        System.out.println("  [Validation] Début de la vérification...");

        // Étape 1 : Vérifications de base
        if (fromRow < 0 || fromRow >= Board.ROWS || fromCol < 0 || fromCol >= Board.COLS ||
            toRow < 0 || toRow >= Board.ROWS || toCol < 0 || toCol >= Board.COLS) {
            System.out.println("  [FAIL] Coordonnées hors du plateau.");
            return false;
        }
        System.out.println("  [OK] Coordonnées dans le plateau.");
        
        Square fromSquare = board.getSquare(fromRow, fromCol);
        Square toSquare = board.getSquare(toRow, toCol);
        Piece pieceToMove = fromSquare.getPiece();

        if (pieceToMove == null) {
            System.out.println("  [FAIL] Aucune pièce à déplacer.");
            return false;
        }
        System.out.println("  [OK] Pièce à déplacer : " + pieceToMove.getAnimal().getName());
        
        if (pieceToMove.getOwner() != currentPlayer) {
            System.out.println("  [FAIL] La pièce n'appartient pas au joueur actuel (Appartient à " + pieceToMove.getOwner().getUsername() + ").");
            return false;
        }
        System.out.println("  [OK] La pièce appartient au joueur actuel.");

        if (toSquare.isOccupied() && toSquare.getPiece().getOwner() == currentPlayer) {
            System.out.println("  [FAIL] La case d'arrivée est occupée par une pièce alliée.");
            return false;
        }
        System.out.println("  [OK] La case d'arrivée n'est pas bloquée par un allié.");

        int ownDenRow = (currentPlayer == player1) ? 8 : 0;
        if (toSquare.getType() == Square.SquareType.SANCTUAIRE && toRow == ownDenRow) {
            System.out.println("  [FAIL] Tentative d'entrer dans son propre sanctuaire.");
            return false;
        }
        System.out.println("  [OK] Ne tente pas d'entrer dans son propre sanctuaire.");

        // Étape 2 : Vérification du type de mouvement
        Animal animal = pieceToMove.getAnimal();
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        if (animal == Animal.LION || animal == Animal.TIGRE) {
            // Logique de saut...
        }

        boolean isSimpleMove = (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
        if (isSimpleMove) {
            System.out.println("  [OK] Le mouvement est un 'mouvement simple' valide.");
            // Logique de terrain pour mouvement simple
            if (animal == Animal.RAT) {
                // ...
            } else {
                if (toSquare.getType() == Square.SquareType.RIVIERE) {
                    System.out.println("  [FAIL] Un non-rat ne peut pas entrer dans la rivière.");
                    return false;
                }
            }
            System.out.println("  [OK] Les règles de terrain sont respectées.");
            return canCaptureAtDestination(pieceToMove, toSquare);
        }
        
        System.out.println("  [FAIL] Le mouvement n'est ni un saut valide, ni un mouvement simple.");
        return false;
    }
    // Dans Game.java
public void setWinner(Player winner) {
    this.winner = winner;
    this.isGameOver = true;
}
    private boolean canCaptureAtDestination(Piece piece, Square to) {
        System.out.println("    [Vérif. Capture] Vérification de la destination...");
        if (!to.isOccupied()) {
            System.out.println("    [Vérif. Capture] Case vide. Mouvement autorisé.");
            return true;
        }
        // ... (le reste de la logique de capture)
        return true; // Simplifié pour le test
    }
    
    // Assurez-vous d'avoir le reste de vos méthodes ici
    private boolean isWinConditionMet(Square destination) {
        if (destination.getType() == Square.SquareType.SANCTUAIRE) {
            Player opponent = (currentPlayer == player1) ? player2 : player1;
            int opponentDenRow = (opponent == player1) ? 8 : 0;
            return destination == board.getSquare(opponentDenRow, 3);
        }
        return false;
    }
    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return isGameOver; }
    public Player getWinner() { return winner; }
}