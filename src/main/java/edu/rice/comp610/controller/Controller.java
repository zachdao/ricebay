package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.SearchSortField;
import edu.rice.comp610.util.IUtil;
import edu.rice.comp610.util.JsonStatusResponse;
import edu.rice.comp610.util.Util;

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

//        JsonParser jsonParser = util.getJsonParser();

        // TODO Instantiate the app's model
        final AccountController accountController = new AccountController();
        final AuctionController auctionController = new AuctionController();

        // TODO Set up SparkJava endpoints

        // Dummy endpoint because SparkJava wants at least one endpoint defined.   Delete this line when other endpoint(s) have been added.
        post("/accounts/create", (request, response) -> gson.toJson(
                accountController.createAccount(gson.fromJson(request.body(), Account.class)
        )));

        get("/auctions/search", ((request, response) -> gson.toJson(auctionController.search(
                request.params("query"),
                SearchSortField.valueOf(request.params("sortField")),
                Boolean.parseBoolean(request.params("sortAscending"))
        ))));


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
