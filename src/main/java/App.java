import static spark.Spark.*;
import java.io.*;
import java.nio.file.*;
import javax.servlet.*;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        String staticDir = "src/main/resources/";
        File uploadDir = new File(staticDir+"pictures"); //change image directory for production
        uploadDir.mkdir();
        staticFiles.externalLocation(staticDir); //only for developement

        //staticFiles.location("/"); //only for production

        get("/hello", (req, res) -> "Hello World");
        post("/usersignup",(req,res) -> {
            Database.createUser(req.queryParams("uname"),
                                req.queryParams("fname")+req.queryParams("lname"),
                                req.queryParams("email"),
                                Integer.parseInt(req.queryParams("psw")),
                                2010,
                                10,
                                10);
            res.redirect("/signupsuccess.html");
            return 0;
        });

        post("/login",(req,res) -> {
            Boolean loginsuccess = Database.userLogin(req.queryParams("uname"),
                    req.queryParams("psw"));
            if(loginsuccess)
                res.redirect("/home.html");
            else
                res.redirect("/login.html");
            return loginsuccess;
        });

        post("/uploadphoto",(req,res) -> {
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream is = req.raw().getPart("fileToUpload").getInputStream()) {
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            Database.uploadPhoto(tempFile.getFileName().toString(),
                                 req.queryParams("title"),
                                 req.queryParams("tags"),
                                 "ialb97");

            return "<h1>You uploaded this image:<h1><img src='pictures/" + tempFile.getFileName() + "'>"+"<p>"+tempFile.getFileName()+"</p>";
        });


        notFound("<html><body><table width=\"400\" cellpadding=\"3\" cellspacing=\"5\"><tr><td id=\"tableProps\" valign=\"top\" align=\"left\"><img id=\"pagerrorImg\" src=\"/web/information.gif\" width=\"25\" height=\"33\"></td> <td id=\"tableProps2\" align=\"left\" valign=\"middle\" width=\"360\"><h1 id=\"textSection1\" style=\"COLOR: black; FONT: 13pt/15pt verdana\"><span id=\"errorText\">These Weapons of Mass Destruction cannot be displayed</span></h1></td></tr><tr><td id=\"tablePropsWidth\" width=\"400\" colspan=\"2\"><font style=\"COLOR: black; FONT: 8pt/11pt verdana\">The weapons you are looking for are currently unavailable. The country might be experiencing technical difficulties, or you may need to adjust your weapons inspectors mandate.</font></td></tr><tr><td id=\"tablePropsWidth\" width=\"400\" colspan=\"2\"><font id=\"LID1\" style=\"COLOR: black; FONT: 8pt/11pt verdana\"><hr color=\"#C0C0C0\" noshade><p id=\"LID2\">Please try the following:</p><ul><li id=\"instructionsText1\">Click the <a href=\"http://www.remember-chile.org.uk/news/01-03-06nyt.htm\" border=\"0\"><img border=\"0\" src=\"/web/refresh.gif\" width=\"13\" height=\"16\" alt=\"refresh.gif (82 bytes)\" align=\"middle\"></a> Regime change button, or try again later.<br> </li> <li id=\"instructionsText2\">If you are George Bush and typed the country's name in the address bar, make sure that it is spelled correctly. (IRAQ).<br></li><li id=\"instructionsText3\">To check your weapons inspector settings, click the <b>UN</b> menu, and then click <b>Weapons Inspector Options</b>. On the <b>Security Council</b> tab, click <b>Consensus</b>. The settings should match those provided by your government or NATO. </li><li id=\"list4\">If the Security Council has enabled it, The United States of America can examine your country and automatically discover Weapons of Mass Destruction.<br>If you would like use the CIA to try and discover them,<br>click <a href=\"http://www.cia.gov/\"><img border=\"0\" src=\"/web/detect.gif\" width=\"16\" height=\"16\" alt=\"Detect Settings\" align=\"center\"> Detect weapons</a></li><li id=\"instructionsText5\">Some countries require 128 thousand troops to liberate them. Click the <b>Panic</b> menu and then click <b> About US foreign policy </b> to determine what regime they will install.</li><li id=\"instructionsText4\">If you are an Old European Country trying to protect your interests, make sure your options are left wide open as long as possible. Click the <b>Tools</b> menu, and then click on <b>League of Nations</b>. On the Advanced tab, scroll to the Head in the Sand section and check settings for your exports to Iraq.</li><li id=\"list3\">Click the <a href=\"http://news.bbc.co.uk/hi/english/static/in_depth/world/2001/cruise_missile/\"><img valign=\"bottom\" border=\"0\" src=\"/web/bomb.gif\">Bomb</a> button if you are Donald Rumsfeld.</li></ul><p><br></p><h2 id=\"IEText\" style=\"font:8pt/11pt verdana; color:black\">Cannot find weapons or CIA Error<br>Iraqi Explorer</h2></font></td></tr></table><Paste></body></html>");

        //test database
        Database.initDatabase();
        //Database.createUser("LeroyJenkins","Bob marley","bobsemail@place.com","password",2010,10,10);
        //Database.uploadPhoto("ApplePiePhoto.jpeg","Delicious Apple Pie","#goodeats #food #apple","LeroyJenkins");
    }
}
