package jungle;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages all interactions with the SQLite database.
 * WARNING: This version saves the database to the 'target/classes/jungle' directory.
 * This directory is DELETED every time you run 'mvn clean'.
 * All saved player data will be lost when 'mvn clean' is used.
 */
public class DatabaseManager {

    private static final String DB_FILENAME = "jungle_game.db";
    private static final String DB_URL;

    // This special block runs only once when the class is loaded.
    // It will set up the database path correctly.
    static {
        String tempUrl;
        try {
            // 1. Get the path to the root of the classpath (e.g., target/classes/)
            URL resourceUrl = DatabaseManager.class.getResource("/");
            if (resourceUrl == null) {
                throw new IllegalStateException("Cannot find classpath root resource '/'");
            }

            // 2. Define the path to the 'jungle' subdirectory within the classpath root.
            File jungleDir = new File(Paths.get(resourceUrl.toURI()).toFile(), "jungle");
            
            // 3. IMPORTANT: Create the 'jungle' directory if it does not exist.
            if (!jungleDir.exists()) {
                System.out.println("Creating directory: " + jungleDir.getAbsolutePath());
                boolean created = jungleDir.mkdirs(); // mkdirs() creates parent directories if needed.
                if(!created){
                     throw new IllegalStateException("Could not create directory: " + jungleDir.getAbsolutePath());
                }
            }

            // 4. Define the full path to the database file inside the 'jungle' directory.
            File dbFile = new File(jungleDir, DB_FILENAME);
            tempUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to construct database path.", e);
        }
        
        DB_URL = tempUrl;
        System.out.println("Database path is set to: " + DB_URL); // For debugging
    }

    public DatabaseManager() {
        // This code ensures the 'players' table is created inside our new DB file.
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sqlPlayers = "CREATE TABLE IF NOT EXISTS players (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "username TEXT NOT NULL UNIQUE," +
                                "password TEXT NOT NULL," +
                                "wins INTEGER DEFAULT 0 NOT NULL," +
                                "losses INTEGER DEFAULT 0 NOT NULL," +
                                "draws INTEGER DEFAULT 0 NOT NULL);";
            
            stmt.execute(sqlPlayers);

        } catch (SQLException e) {
            System.err.println("Error creating or connecting to database: " + e.getMessage());
            e.printStackTrace(); // Print the full error to see what's wrong
        }
    }

    // All other methods (createPlayer, loginPlayer, etc.) remain exactly the same.
    // They will now use the new DB_URL.

    public boolean createPlayer(String username, String password) {
        String sql = "INSERT INTO players(username, password) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating player (username might already exist): " + e.getMessage());
            return false;
        }
    }

    public Player loginPlayer(String username, String password) {
        String sql = "SELECT id, username, password, wins, losses, draws FROM players WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Player player = new Player(rs.getString("username"), rs.getString("password"));
                player.setId(rs.getInt("id"));
                player.setWins(rs.getInt("wins"));
                player.setLosses(rs.getInt("losses"));
                player.setDraws(rs.getInt("draws"));
                return player;
            }
        } catch (SQLException e) {
            System.err.println("Error during player login: " + e.getMessage());
        }
        return null;
    }

    public void updatePlayerStats(Player player) {
        String sql = "UPDATE players SET wins = ?, losses = ?, draws = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, player.getWins());
            pstmt.setInt(2, player.getLosses());
            pstmt.setInt(3, player.getDraws());
            pstmt.setInt(4, player.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating player stats: " + e.getMessage());
        }
    }
}