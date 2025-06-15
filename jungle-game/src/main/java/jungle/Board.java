package jungle;

public class Board {
    public static final int ROWS = 9;
    public static final int COLS = 7;
    private final Square[][] grid;

    public Board(Player p1, Player p2) {
        grid = new Square[ROWS][COLS];
        initializeBoard(p1, p2);
    }

    public Square getSquare(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) return grid[row][col];
        return null;
    }

    private void initializeBoard(Player player1, Player player2) {
        // Étape 1 : Tout initialiser en terrain normal
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = new Square(Square.SquareType.NORMAL);
            }
        }

        // Étape 2 : Définir les zones spéciales
        // La Rivière
        for (int r = 3; r <= 5; r++) {
            for (int c : new int[]{1, 2, 4, 5}) {
                grid[r][c] = new Square(Square.SquareType.RIVIERE);
            }
        }

        // *********************************************************
        // ***         MODIFICATION : L'ÎLE CENTRALE             ***
        // *** On transforme la case de rivière D4 en terre normale ***
        grid[4][3] = new Square(Square.SquareType.NORMAL);
        // *********************************************************

        // Les Sanctuaires
        grid[0][3] = new Square(Square.SquareType.SANCTUAIRE);
        grid[8][3] = new Square(Square.SquareType.SANCTUAIRE);

        // Les Pièges
        grid[0][2] = new Square(Square.SquareType.PIEGE, player2); grid[0][4] = new Square(Square.SquareType.PIEGE, player2); grid[1][3] = new Square(Square.SquareType.PIEGE, player2);
        grid[8][2] = new Square(Square.SquareType.PIEGE, player1); grid[8][4] = new Square(Square.SquareType.PIEGE, player1); grid[7][3] = new Square(Square.SquareType.PIEGE, player1);
        
        // Étape 3 : Placer les pièces
        grid[0][0].setPiece(new Piece(Animal.LION, player2)); grid[0][6].setPiece(new Piece(Animal.TIGRE, player2));
        grid[1][1].setPiece(new Piece(Animal.CHIEN, player2)); grid[1][5].setPiece(new Piece(Animal.CHAT, player2));
        grid[2][0].setPiece(new Piece(Animal.RAT, player2)); grid[2][2].setPiece(new Piece(Animal.PANTHERE, player2));
        grid[2][4].setPiece(new Piece(Animal.LOUP, player2)); grid[2][6].setPiece(new Piece(Animal.ELEPHANT, player2));
        grid[8][6].setPiece(new Piece(Animal.LION, player1)); grid[8][0].setPiece(new Piece(Animal.TIGRE, player1));
        grid[7][5].setPiece(new Piece(Animal.CHIEN, player1)); grid[7][1].setPiece(new Piece(Animal.CHAT, player1));
        grid[6][6].setPiece(new Piece(Animal.RAT, player1)); grid[6][4].setPiece(new Piece(Animal.PANTHERE, player1));
        grid[6][2].setPiece(new Piece(Animal.LOUP, player1)); grid[6][0].setPiece(new Piece(Animal.ELEPHANT, player1));
    }

    // Dans Board.java

public void display() {
    // Codes de couleur ANSI
    final String RESET = "\u001B[0m";
    final String BG_BLUE = "\u001B[44m";
    final String BG_RED = "\u001B[41m";
    final String BG_YELLOW = "\u001B[43m";
    final String FG_WHITE = "\u001B[97m";
    final String FG_BLACK = "\u001B[30m";

    System.out.println("\n    A       B       C       D       E       F       G    ");
    System.out.println("  +-------+-------+-------+-------+-------+-------+-------+");

    for (int r = 0; r < ROWS; r++) {
        System.out.print(r + " |");
        for (int c = 0; c < COLS; c++) {
            Square s = grid[r][c];
            String content;
            String background = "";

            if (s.getPiece() != null) {
                content = s.getPiece().getAnimal().getName().substring(0, 3) + s.getPiece().getOwner().getId();
            } else {
                content = "       "; // 7 espaces pour remplir la case
            }

            // Appliquer la couleur de fond en fonction du terrain
            switch (s.getType()) {
                case RIVIERE:    background = BG_BLUE; break;
                case PIEGE:      background = BG_RED; break;
                case SANCTUAIRE: background = BG_YELLOW; break;
                default:         background = ""; break;
            }
            
            // Pour les sanctuaires/pièges, écrire le nom par-dessus la couleur
            if (s.getPiece() == null && s.getType() == Square.SquareType.PIEGE) content = FG_BLACK + " Piège " + RESET;
            if (s.getPiece() == null && s.getType() == Square.SquareType.SANCTUAIRE) content = FG_BLACK + " Sanct." + RESET;

            System.out.print(background + FG_WHITE + content.substring(0, Math.min(content.length(), 7)) + RESET + "|");
        }
        System.out.println("\n  +-------+-------+-------+-------+-------+-------+-------+");
    }
}
}