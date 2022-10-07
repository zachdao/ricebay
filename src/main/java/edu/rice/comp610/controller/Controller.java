package edu.rice.comp610.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.rice.comp610.util.IUtil;
import edu.rice.comp610.util.JsonStatusResponse;
import edu.rice.comp610.util.Util;

import java.beans.PropertyChangeListener;

import static spark.Spark.*;


/**
 * The paint world controller creates the adapter(s) that communicate with the view.
 * The controller responds to requests from the view after contacting the adapter(s).
 */
public class Controller {

    /**
     * The main entry point into the program.
     *
     * @param args The program arguments normally specified on the cmd line
     */
    public static void main(String[] args) {
        (new Controller()).start(args);
    }

    /**
     * Start the main spark server
     * @param args args from command line. (Currently not used)
     */
    public void start(String[] args) {
        staticFiles.location("/public");
        port(getHerokuAssignedPort());
        Gson gson = new Gson();
        IUtil util = Util.getInstance();

        JsonParser jsonParser = util.getJsonParser();

        // TODO Instantiate the app's model

        // TODO Set up SparkJava endpoints

        // Dummy endpoint because SparkJava wants at least one endpoint defined.   Delete this line when other endpoint(s) have been added.
        get("/anEndpoint", (request, response) -> new JsonStatusResponse(true,null, "ok"));

        // A redirect happen when user go to wrong location.
        notFound((req, res) -> {
            res.redirect("/");
            return gson.toJson(new JsonStatusResponse(false, null, "redirecting to main page"));
        });
        awaitInitialization();

        System.out.println("Server started and running on http://localhost:" + getHerokuAssignedPort());
    }

    /**
     * Get the heroku assigned port number.
     *
     * @return The port number
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
