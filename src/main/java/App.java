import static spark.Spark.*;
//import java.sql.*;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        //test spark
        get("/hello", (req, res) -> "Hello World");

        //test database
        Database.initDatabase();
        Database.createUser("LeroyJenkins","Bob marley","bobsemail@place.com","password",2010,10,10);
    }
}
