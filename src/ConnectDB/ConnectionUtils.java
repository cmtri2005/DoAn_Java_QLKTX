package ConnectDB;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {
    public static Connection getMyConnectionOracle() throws ClassNotFoundException, SQLException {
        return ConnectionOracle.getConnectionOracle();
    }
    
    public static void main(String[] args) {
        Connection conn = null;
        try {
            System.out.println("Getting connection...");
            conn = getMyConnectionOracle();
            System.out.println("Connection established: " + conn);
            System.out.println("Successfully connected to Oracle database");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionOracle.closeConnection(conn);
        }
    }
}