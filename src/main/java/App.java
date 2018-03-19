import static spark.Spark.*;
//import java.sql.*;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        //test spark
        staticFiles.location("/");
        get("/hello", (req, res) -> "Hello World");
        post("/usersignup",(req,res) -> {
            Database.createUser(req.queryParams("uname"),
                                req.queryParams("fname")+req.queryParams("lname"),
                                req.queryParams("email"),
                                req.queryParams("psw"),
                                2010,
                                10,
                                10);
            return 0;
        });
        post("/uploadphoto",(req,res) -> {
            Database.uploadPhoto(req.queryParams("fileToUpload"),
                    req.queryParams("title"),
                    req.queryParams("tags"),
                    "ialb97"
                                        );
            return 0;
        });
        post("/login",(req,res) -> {
            Boolean loginsuccess = Database.userLogin(req.queryParams("uname"),
                    req.queryParams("psw"));
            return loginsuccess;
        });



        //test database
        Database.initDatabase();
        //Database.createUser("LeroyJenkins","Bob marley","bobsemail@place.com","password",2010,10,10);
        //Database.uploadPhoto("ApplePiePhoto.jpeg","Delicious Apple Pie","#goodeats #food #apple","LeroyJenkins");
    }
}
