package jungle;

/**
 * Point d'entrée de l'application Mini-Projet Java.
 * Crée le contrôleur de jeu et lance l'application.
 */
public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.start();
    }
}