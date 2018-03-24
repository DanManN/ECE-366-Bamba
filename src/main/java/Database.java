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
                        " UPLOADDATE         DATE, " +
                        " TAGS               TEXT, " +
                        " COLLECTIONS        BLOB)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS COLLECTIONS " +
                        "(COLLECTIONID            INT PRIMARY KEY    NOT NULL, " +
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
        System.out.println("Initialized database successfully.");
        return true;
    }
    
    public static boolean createUser(String username, String fullname, String email, String password, int year, int month, int day) {
        Connection c = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bamba.db");
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
        System.out.println(username+"'s records created successfully.");
        return true;
    }

    public static boolean uploadPhoto(String pathName, String Title, String Tags, String username) {
        int uid = getUserID(username);
        Connection c = null;
        PreparedStatement stmt = null;
        if(uid == -1)
            return false;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bamba.db");
            String sql = "INSERT INTO PHOTOS (PHOTOID,USERID,TITLE,TAGS,PHOTO) " +
                    "VALUES (?, ?, ?, ?, ?);";
            stmt = c.prepareStatement(sql);

            stmt.setInt(1,1);
            stmt.setInt(2,uid);
            stmt.setString(3,Title);
            stmt.setString(4,Tags);
            stmt.setString(5,pathName);
            stmt.executeUpdate();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        System.out.println(username+"'s upload records created successfully.");
        return true;
    }
   private static int getUserID(String username){
        Connection c = null;
        PreparedStatement stmt = null;
        String sql = "Select USERID From USERS WHERE USERNAME=?";
        int userid=-1;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bamba.db");
            stmt = c.prepareStatement(sql);
            stmt.setString(1,username);
            ResultSet uid = stmt.executeQuery();
            while(uid.next()) {
                userid = uid.getInt("USERID");
            }
            c.close();
            System.out.println(username+"'s id is: "+userid);
            return userid;
        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return -1;
        }
    }

    public static boolean userLogin(String username,String pass){
        Connection c = null;
        PreparedStatement stmt = null;
        String sql = "Select USERNAME,PASSWORD From USERS WHERE USERNAME=?";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bamba.db");
            stmt = c.prepareStatement(sql);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                if(rs.getString("PASSWORD").equals(pass)){
                    System.out.println(username+" logged in with password: " + pass);
                    return true;
                }
            }
            c.close();
        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return false;
    }

}
