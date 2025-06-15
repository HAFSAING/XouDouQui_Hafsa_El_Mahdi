package jungle;

import java.util.Scanner;

public class GameController {
    private final DatabaseManager dbManager;
    private final Scanner scanner;
    private Player player1;
    private Player player2;

    public GameController() {
        this.dbManager = new DatabaseManager();
        this.scanner = new Scanner(System.in);
    }

    // ========== HELPER POUR UN MEILLEUR AFFICHAGE ==========
    private void printHeader(String title) {
        System.out.println("\n==============================================");
        System.out.println("  " + title);
        System.out.println("==============================================");
    }

    public void start() {
        printHeader("Bienvenue dans le jeu Xou Dou Qi (Jungle) !");
        
        while (true) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Commencer une nouvelle partie");
            System.out.println("2. Créer un nouveau compte joueur");
            System.out.println("3. Quitter le jeu");
            System.out.print(">>> Votre choix : ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    if (setupPlayers()) {
                        runGame();
                    }
                    break;
                case "2":
                    createAccount();
                    break;
                case "3":
                    System.out.println("\nMerci d'avoir joué. Au revoir !");
                    return;
                default:
                    System.out.println("!! Option invalide. Veuillez entrer 1, 2, ou 3.");
            }
        }
    }
    
    private void createAccount() {
        printHeader("Création d'un nouveau compte");
        System.out.print("Entrez un nom d'utilisateur : ");
        String username = scanner.nextLine();
        System.out.print("Entrez un mot de passe : ");
        String password = scanner.nextLine();

        if (dbManager.createPlayer(username, password)) {
            System.out.println("\n>> Compte pour '" + username + "' créé avec succès ! <<");
        } else {
            System.out.println("\n!! ERREUR : Ce nom d'utilisateur existe déjà. Veuillez en choisir un autre.");
        }
    }

    private boolean setupPlayers() {
        printHeader("Connexion des Joueurs");
        
        System.out.println("\n--- Connexion du Joueur 1 ---");
        player1 = login("Joueur 1");
        if (player1 == null) {
            System.out.println("Connexion annulée.");
            return false;
        }
        
        System.out.println("\n--- Connexion du Joueur 2 ---");
        while (true) {
            player2 = login("Joueur 2");
            if (player2 == null) {
                System.out.println("Connexion annulée.");
                return false;
            }
            if (player2.getId() != player1.getId()) {
                break;
            } else {
                System.out.println("!! Le joueur 2 ne peut pas être le même que le joueur 1. Veuillez connecter un autre joueur.");
            }
        }
        return true;
    }

    private Player login(String playerLabel) {
        while (true) {
            System.out.print("Nom d'utilisateur (ou tapez 'annuler' pour revenir au menu) : ");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("annuler")) return null;
            
            System.out.print("Mot de passe : ");
            String password = scanner.nextLine();

            Player player = dbManager.loginPlayer(username, password);
            if (player != null) {
                System.out.println(">> Bienvenue, " + username + " ! Vous êtes le " + playerLabel + ".");
                return player;
            } else {
                System.out.println("!! Nom d'utilisateur ou mot de passe incorrect. Réessayez.");
            }
        }
    }

    private void runGame() {
        printHeader("La partie commence !");
        System.out.println(player1.getUsername() + " (Joueur 1) vs " + player2.getUsername() + " (Joueur 2)");
        
        Game game = new Game(player1, player2);
        
        while (!game.isGameOver()) {
            game.getBoard().display();
            Player currentPlayer = game.getCurrentPlayer();
            
            String playerLabel = (currentPlayer == player1) ? "Joueur 1" : "Joueur 2";
            System.out.println("\n>>> C'est le tour de " + currentPlayer.getUsername() + " (" + playerLabel + ")");
            System.out.print("Entrez votre mouvement (ex: A6A5) ou 'abandonner' : ");
            String input = scanner.nextLine().toUpperCase();
            
            if (input.equalsIgnoreCase("ABANDONNER")) {
                Player winner = (currentPlayer == player1) ? player2 : player1;
                System.out.println("\n" + currentPlayer.getUsername() + " a abandonné la partie.");
                game.setWinner(winner); // On définit le gagnant dans l'objet Game
                break; // On sort de la boucle de jeu
            }

            if (input.length() != 4) {
                System.out.println("!! Format d'entrée invalide. Veuillez utiliser 4 caractères (ex: A6A5).");
                continue;
            }

            try {
                int fromCol = input.charAt(0) - 'A';
                int fromRow = Integer.parseInt(input.substring(1, 2));
                int toCol = input.charAt(2) - 'A';
                int toRow = Integer.parseInt(input.substring(3, 4));

                if (!game.makeMove(fromRow, fromCol, toRow, toCol)) {
                    System.out.println("!! Mouvement invalide. Veuillez réessayer.");
                }

            } catch (Exception e) {
                System.out.println("!! Coordonnées invalides. Assurez-vous d'utiliser le format LETTRECHIFFRELETTRECHIFFRE.");
            }
        }
        
        // Affichage du résultat final
        game.getBoard().display();
        printHeader("Partie Terminée !");
        System.out.println("    Le gagnant est : " + game.getWinner().getUsername() + " ! Félicitations !");
        System.out.println("==============================================");
        
        Player loser = (game.getWinner() == player1) ? player2 : player1;
        updateGameResults(game.getWinner(), loser);
    }
    
    private void updateGameResults(Player winner, Player loser) {
        System.out.println("\n--- Mise à jour des statistiques ---");
        winner.incrementWins();
        loser.incrementLosses();
        
        dbManager.updatePlayerStats(winner);
        dbManager.updatePlayerStats(loser);
        
        System.out.println(winner);
        System.out.println(loser);
    }
}