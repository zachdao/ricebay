package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.*;
import edu.rice.comp610.store.*;
import edu.rice.comp610.util.*;
import spark.Filter;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        PostgresDatabaseManager.initialize(properties);

        final QueryManager queryManager = new PostgresQueryManager();
        final DatabaseManager databaseManager = PostgresDatabaseManager.getInstance();

        final AccountManager accountManager = new LocalAccountManager(queryManager, databaseManager);

        final AuctionManager auctionManager = new StandardAuctionManager(queryManager, databaseManager);

        final AccountAdapter accountAdapter = new AccountAdapter(accountManager);
        final AuctionAdapter auctionAdapter = new AuctionAdapter(accountManager,
                auctionManager,
                new SellerRatingManager(queryManager, databaseManager),
                new StandardBidManager(queryManager, databaseManager));

        final ActiveAuctionMonitor activeAuctionMonitor = new ActiveAuctionMonitor(auctionManager);

        Filter authenticatedMiddleware = (request, response) -> {
            boolean isUnauthenticatedRoute = Objects.equals(request.requestMethod(), "POST") &&
                    (Objects.equals(request.pathInfo(), "/accounts") ||
                            Objects.equals(request.pathInfo(), "/accounts/login"));
            if (!isUnauthenticatedRoute && request.session().attribute("user") == null) {
                halt(401, "Unauthenticated");
            }
        };

        before("/accounts", authenticatedMiddleware);
        before("/accounts/*", authenticatedMiddleware);
        // USER/ACCOUNT ENDPOINTS ------------------------------------------------------------
        path("/accounts", () -> {
            post("", (request, response) -> {
                var accountDetails = gson.fromJson(request.body(), ViewAccount.class);
                var credentials = gson.fromJson(request.body(), Credentials.class);
                var appResponse = accountAdapter.register(accountDetails, credentials);
                response.status(appResponse.getStatus());
                if (appResponse.isSuccess()) {
                    request.session(true);
                    request.session().attribute("user", accountAdapter.me(credentials.getEmail()).getData());
                }
                return gson.toJson(appResponse);
            });

            post("/login", (request, response) -> {
                var credentials = gson.fromJson(request.body(), Credentials.class);
                var appResponse = accountAdapter.login(credentials);
                response.status(appResponse.getStatus());
                if (appResponse.isSuccess()) {
                    request.session(true);
                    request.session().attribute("user", accountAdapter.me(credentials.getEmail()).getData());
                }
                return gson.toJson(appResponse);
            });

            post("/logout", (request, response) -> {
                request.session().invalidate();
                return gson.toJson(new AppResponse<>(200, true, null, "logout"));
            });

            get("/me", (request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                var appResponse = accountAdapter.me(loggedInAccount.getEmail());
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            });

            post("/:id", (request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                ViewAccount account = gson.fromJson(request.body(), ViewAccount.class);
                if (!Objects.equals(loggedInAccount.getId().toString(), request.params("id")) || !Objects.equals(account.getId().toString(), request.params("id"))) {
                    throw new UnauthorizedException();
                }
                var appResponse = accountAdapter.update(account);
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            });

            post("/:id/password", (request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                if (!Objects.equals(loggedInAccount.getId().toString(), request.params("id"))) {
                    throw new UnauthorizedException();
                }
                var credentials = gson.fromJson(request.body(), Credentials.class);
                var jsonBody = util.getJsonParser().parse(request.body());
                var newPassword = jsonBody.getAsJsonObject().get("newPassword").getAsString();
                var appResponse = accountAdapter.updatePassword(credentials, newPassword);
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            });
        });

        before("/auctions", authenticatedMiddleware);
        before("/auctions/*", authenticatedMiddleware);
        // AUCTION ENDPOINTS -----------------------------------------------------------------
        path("/auctions", () -> {
            post("", ((request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                ViewAuction viewAuction = gson.fromJson(request.body(), ViewAuction.class);
                var appResponse = auctionAdapter.create(viewAuction, loggedInAccount.getId());
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            }));
            get("/search", ((request, response) -> {
                var query = new AuctionQuery(queryManager.filters(), request.queryMap().toMap(),
                        AuctionSortField.valueOf(request.params().getOrDefault("sortField", String.valueOf(AuctionSortField.END_DATE))),
                        Boolean.parseBoolean(request.params().getOrDefault("sortAscending", "true")));
                var appResponse = auctionAdapter.search(query);
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            }));
            get("/recentlyViewed", (((request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                var appResponse = auctionAdapter.recentlyViewed(loggedInAccount.getId());
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            })));
            get("/:id", ((request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                var appResponse = auctionAdapter.get(UUID.fromString(request.params("id")), loggedInAccount);
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            }));
            post("/:id", ((request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                ViewAuction viewAuction = gson.fromJson(request.body(), ViewAuction.class);
                var appResponse = auctionAdapter.update(viewAuction, loggedInAccount.getId());
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            }));

            // BID ENDPOINTS -----------------------------------------------------------------
            post("/:id/placeBid", (((request, response) -> {
                ViewAccount loggedInAccount = request.session().attribute("user");
                ViewBid bid = gson.fromJson(request.body(), ViewBid.class);
                UUID auctionId = UUID.fromString(request.params("id"));
                var appResponse = auctionAdapter.placeBid(loggedInAccount.getId(), auctionId, bid.getBid(), bid.getMaxBid());
                response.status(appResponse.getStatus());
                return gson.toJson(appResponse);
            })));

            // TODO: Update bid endpoint

        });

        // CATEGORY ENDPOINT -----------------------------------------------------------------
        get("/categories", (((request, response) -> {
            var appResponse = auctionAdapter.allCategories();
            response.status(appResponse.getStatus());
            return gson.toJson(appResponse);
        })));

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

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(activeAuctionMonitor,
                0,
                Integer.parseInt(properties.getProperty("ricebay.activeauctionmonitor.delay", "1")),
                TimeUnit.MINUTES);

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
