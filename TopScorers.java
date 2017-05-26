
package pong2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TopScorers {
    
    static String driver = "jdbc:sqlserver:";
    static String url = "//lcmdb.cbjmpwcdjfmq.us-east-1.rds.amazonaws.com:";
    static String port = "1433";
    static String username = "DS3";
    static String password = "Touro123";
    static String database = "DS3";
    static PreparedStatement state;
    static String connection = driver + url + port
            + ";databaseName=" + database + ";user=" + username + ";password=" + password + ";";

    public static void addNewScore(String name, int score) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver Successfully Loaded!");
            try (Connection connect = DriverManager.getConnection(connection)) {
                System.out.println("Connected to Database!");
                state = connect.prepareStatement("INSERT INTO Scores VALUES(\'" + name
                        + "', " + score + ")");
                state.execute();
                System.out.println("Query Executed Successfully!");

                if (arrayScores().size() > 9) {
                    state = connect.prepareStatement("DELETE FROM Scores WHERE ID IN (SELECT TOP 1 ID FROM Scores ORDER BY Score)"
                            );
                    System.out.println("Query Executed Successfully!");
                    state.execute();
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: Driver Class not found.");
            ex.printStackTrace();
        } catch (SQLException sqlex) {
            System.out.println("Error: SQL Error");
            sqlex.printStackTrace();
        }
    }

    public static ArrayList<String> arrayScores() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver Successfully Loaded!");
            try (Connection connect = DriverManager.getConnection(connection)) {
                System.out.println("Connected to Database!");
                state = connect.prepareStatement("SELECT TOP 10 * FROM SCORES"
                        + " ORDER BY Score DESC");
                System.out.println("Query Executed Successfully!");

                ArrayList<String> scores = new ArrayList<>(10);
                int i = 0;
                ResultSet rs = state.executeQuery();
                while (rs.next()) {
                    scores.add(i, String.format("%-10s%-20s%s", (i + 1) + ".", rs.getString("PlayerName"), rs.getString("Score")));
                    ++i;
                }
                return scores;
            }
            //System.out.println("Database Closed!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: Driver Class not found.");
            ex.printStackTrace();
        } catch (SQLException sqlex) {
            System.out.println("Error: SQL Error");
            sqlex.printStackTrace();
        }
        return null;
    }
}
