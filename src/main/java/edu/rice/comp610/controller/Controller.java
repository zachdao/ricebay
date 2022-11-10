package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.store.*;
import edu.rice.comp610.util.*;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

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

        Properties properties = PropertiesLoader.loadProperties();

        // TODO Instantiate the app's model
        final QueryManager queryManager = new QueryManager();
        final DatabaseManager databaseManager = new PostgresDatabaseManager(properties);
        final UserManager userManager = new UserManager(queryManager, databaseManager);
        final AuctionManager auctionManager = new AuctionManager(queryManager, databaseManager);

        // TODO Set up SparkJava endpoints

        // USER/ACCOUNT ENDPOINTS ------------------------------------------------------------
        path("/accounts", () -> {
            post("", (request, response) -> gson.toJson(
                    userManager.saveAccount(gson.fromJson(request.body(), Account.class)))
            );

            post("/login", ((request, response) -> {
                var credentials = util.getJsonParser().parse(request.body());
                var email = credentials.getAsJsonObject().get("email").getAsString();
                var password = credentials.getAsJsonObject().get("password").getAsString();

                return gson.toJson(userManager.validateLogin(email, password));
            }));

            get("/me", ((request, response) -> {
                // TODO: Get alias from session
                return gson.toJson(userManager.retrieveAccount("TODO"));
            }));
        });

        // AUCTION ENDPOINTS -----------------------------------------------------------------
        path("/auctions", () -> {
            post("", ((request, response) ->
                    gson.toJson(auctionManager.createAuction(gson.fromJson(request.body(), Auction.class))))
            );
            get("/search", ((request, response) -> gson.toJson(
                    auctionManager.search(
                            new AuctionQuery(
                                    request.params("query"),
                                    AuctionSortField.valueOf(request.params("sortField")),
                                    Boolean.parseBoolean(request.params("sortAscending"))))))
            );
            get("/:id", ((request, response) ->
                    gson.toJson(auctionManager.loadAuction(UUID.fromString(request.params("id")))))
            );
            put("/:id", ((request, response) ->
                    gson.toJson(auctionManager.updateAuction(gson.fromJson(request.body(), Auction.class))))
            );
        });

        // A redirect to our SPA if it isn't a route we recognize
        get("*", (req, res) -> {
            res.type("application/json");
            try (InputStream stream = getClass().getResourceAsStream("/public/index.html")) {
                assert stream != null;
                res.status(200);
                res.type("text/html");
                return IOUtils.toString(stream);
            } catch (IOException e) {
                // If the resource doesn't exist, return a 404
                // We should never get here!
                res.status(404);
                res.type("application/json");
                return "not supported";
            }
        });

        exception(UnauthorizedException.class, (exception, request, response) -> {
            response.status(401);
            response.type("application/json");
            response.body(exception.getMessage());
        });

        exception(BadRequestException.class, (exception, request, response) -> {
            response.status(400);
            response.type("application/json");
            response.body(gson.toJson(exception.getRequestErrors()));
        });

        exception(ObjectNotFoundException.class, (e, request, response) -> {
            response.status(404);
            response.type("application/json");
            response.body("not found");
        });

        exception(Exception.class, (exception, request, response) -> {
            System.out.println("Unhandled exception: " + exception.getMessage());
            response.status(500);
            response.type("application/json");
            response.body("Internal Server Error");
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
