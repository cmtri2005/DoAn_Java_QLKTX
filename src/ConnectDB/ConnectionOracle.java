package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionOracle {
    private static final String HOSTNAME = "localhost";
    private static final String SID = "orcl";
    private static final String USERNAME = "QUANLYKTX";
    private static final String PASSWORD = "Admin123";
    
    public static Connection getConnectionOracle() throws ClassNotFoundException, SQLException {
        String jdbcUrl = String.format("jdbc:oracle:thin:@%s:1521:%s", HOSTNAME, SID);
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}