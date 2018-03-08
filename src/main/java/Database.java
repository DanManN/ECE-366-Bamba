//import static spark.Spark.*;
import java.sql.*;

public class Database {

    public static boolean initDatabase() {
        //create database
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bamba.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS USERS " +
                        "(USERID INT PRIMARY KEY     NOT NULL, " +
                        " USERNAME           TEXT    NOT NULL, " +
                        " FULLNAME           TEXT    NOT NULL, " +
                        " EMAIL              TEXT    NOT NULL, " +
                        " PASSWORD           INT     NOT NULL, " +
                        " BIRTHDAY           DATE,             " +
                        " COLLECTIONS        BLOB)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS PHOTOS " +
                        "(PHOTOID INT PRIMARY KEY    NOT NULL, " +
                        " TITLE              TEXT    NOT NULL, " +
                        " USERID             INT     NOT NULL, " +
                        " PHOTO              TEXT    NOT NULL, " +
                        " UPLOADDATE         DATE    NOT NULL, " +
                        " TAGS               TEXT, " +
                        " COLLECTIONS        BLOB)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS COLLECTIONS " +
                        "(COLLECTIONID INT PRIMARY KEY    NOT NULL, " +
                        " TITLE                   TEXT    NOT NULL, " +
                        " USERID                  INT     NOT NULL, " +
                        " CREATIONDATE            DATE    NOT NULL, " +
                        " PHOTOIDS                BLOB)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        System.out.println("Opened database successfully");
        return true;
    }
    
    public static boolean createUser(String username, String fullname, String email, String password, int year, int month, int day) {
        Connection c = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bamba.db");
            System.out.println("Opened database successfully");
            String sql = "INSERT INTO USERS (USERID,USERNAME,FULLNAME,EMAIL,PASSWORD,BIRTHDAY) " +
                        "VALUES (?, ?, ?, ?, ?, ?);";
            stmt = c.prepareStatement(sql);
            stmt.setInt(1,1);
            stmt.setString(2,username);
            stmt.setString(3,fullname);
            stmt.setString(4,email);
            stmt.setInt(5,88);
            stmt.setDate(6,new Date(year,month,day));
            stmt.executeUpdate();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        System.out.println("Records created successfully");
        return true;
    }

}
