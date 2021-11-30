package ge.invest.connection;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Ucha Chaduneli
 */
public class DBConnection implements Serializable {
//

    private static final String dhlDbName = "dhl?characterEncoding=utf8";
    private static final String dbUrl = "jdbc:mysql://localhost:3306/";
    private static final String dbName = "invent?characterEncoding=utf8";
    private static final String dbDriver = "com.mysql.jdbc.Driver";
    private static final String dbUserName = "root";
    private static final String dbPassword = "root";
    private static Connection dhlDbConn;
    private static Connection dbConn;

    public static Connection getDbConn() {
        try {
            if (dbConn == null || dbConn.isClosed()) {
                Class.forName(dbDriver).newInstance();
                dbConn = DriverManager.getConnection(dbUrl + dbName, dbUserName, dbPassword);
            }
        } catch (SQLException ex) {
            dbConn = null;
        } catch (Exception ex) {
            dbConn = null;
        }
        return dbConn;
    }

    public static Connection getDhlDbConn() {
        try {
            if (dhlDbConn == null || dhlDbConn.isClosed()) {
                Class.forName(dbDriver).newInstance();
                dbConn = DriverManager.getConnection(dbUrl + dhlDbName, dbUserName, dbPassword);
            }
        } catch (SQLException ex) {
            dhlDbConn = null;
        } catch (Exception ex) {
            dhlDbConn = null;
        }
        return dhlDbConn;
    }

    public static boolean IsClosedDbConn() {
        try {
            if (dbConn == null) {
                return true;
            } else {
                if (dbConn.isClosed()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public static void openDbConn() {
        if (IsClosedDbConn()) {
            try {
                Class.forName(dbDriver).newInstance();
                dbConn = DriverManager.getConnection(dbUrl + dbName, dbUserName, dbPassword);
            } catch (Exception ex) {
            }
        }
    }

    public static void openDhlDbConn() {
        if (IsClosedDbConn()) {
            try {
                Class.forName(dbDriver).newInstance();
                dhlDbConn = DriverManager.getConnection(dbUrl + dhlDbName, dbUserName, dbPassword);
            } catch (Exception ex) {
            }
        }
    }

    public static void closeDhlDbConn() {
        try {
            if (dhlDbConn != null) {
                if (!dhlDbConn.isClosed()) {
                    dhlDbConn.close();
                    dhlDbConn = null;
                }
            }
        } catch (SQLException ex) {
        }
    }

    public static void closeDbConn() {
        try {
            if (dbConn != null) {
                if (!dbConn.isClosed()) {
                    dbConn.close();
                    dbConn = null;
                }
            }
        } catch (SQLException ex) {
        }
    }

    public static void main(String[] args) throws UnknownHostException, SQLException {
        getDbConn();
    }
}
