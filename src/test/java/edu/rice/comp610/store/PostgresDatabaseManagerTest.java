package edu.rice.comp610.store;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.Auction;
import edu.rice.comp610.model.Category;
import edu.rice.comp610.testing.DatabaseSetup;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.*;

import static edu.rice.comp610.testing.Dates.parseDate;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class PostgresDatabaseManagerTest {
    private static PostgresDatabaseManager databaseManager;
    private final QueryManager queryManager = new QueryManager();

    private static final UUID AUCTION_ID = UUID.randomUUID();

    private static final Account NEW_ACCOUNT = new Account();
    {
        NEW_ACCOUNT.setId(UUID.randomUUID());
        NEW_ACCOUNT.setGivenName("Nebojsa");
        NEW_ACCOUNT.setSurname("DIMIC");
        NEW_ACCOUNT.setEmail("nebojsadimic@nebojsadimic.com");
        NEW_ACCOUNT.setAlias("testalias");
        NEW_ACCOUNT.setPassword("password123");
    }

    private static final Auction NEW_AUCTION = new Auction();
    {
        NEW_AUCTION.setTitle("New Auction");
        NEW_AUCTION.setDescription("New Auction Description");
        NEW_AUCTION.setBidIncrement(1.0);
        NEW_AUCTION.setMinimumBid(123.0);
        NEW_AUCTION.setStartDate(parseDate("2022-10-20"));
        NEW_AUCTION.setEndDate(parseDate("2022-10-21"));
        NEW_AUCTION.setTaxPercent(0.06f);
        NEW_AUCTION.setPublished(true);
        NEW_AUCTION.setId(UUID.randomUUID());
        NEW_AUCTION.setOwnerId(NEW_ACCOUNT.getId());
    }

    @BeforeAll
    static void setupDatabase() throws IOException, InterruptedException {
        PostgreSQLContainer container = DatabaseSetup.init();
        Properties props = new Properties();
        props.setProperty("user", container.getUsername());
        props.setProperty("password", container.getPassword());
        databaseManager = new PostgresDatabaseManager(container.getJdbcUrl(), props);
    }

    @AfterAll
    static void tearDown() {
        DatabaseSetup.shutdown();
    }

    @Test
    void loadObjectsEmptyResult() throws Exception {
        Query<Auction> query = queryManager.makeLoadQuery(Auction.class, "id");
        List<Auction> results = databaseManager.loadObjects(query, AUCTION_ID);

        assertEquals(results, List.of());
    }

    @Test
    void saveAndLoadObjectsCategory() throws Exception {
        Query<Category> query = queryManager.makeUpdateQuery(Category.class);
        Category category = new Category();
        category.setName("Toys & Games");
        category.setDescription("Things that are fun");
        databaseManager.saveObjects(query, category);

        query = queryManager.makeLoadQuery(Category.class, "name");
        List<Category> results = databaseManager.loadObjects(query, category.getName());
        assertEquals(1, results.size());
        assertEquals("Toys & Games", results.get(0).getName());
        assertEquals("Things that are fun", results.get(0).getDescription());
    }

    @Test
    void saveAndLoadObjectsAccountAndAuction() throws Exception {
        Query accountQuery = queryManager.makeUpdateQuery(Account.class);
        databaseManager.saveObjects(accountQuery, NEW_ACCOUNT);

        Query auctionQuery = queryManager.makeUpdateQuery(Auction.class);
        databaseManager.saveObjects(auctionQuery, NEW_AUCTION);

        Query<Auction> loadAuction = queryManager.makeLoadQuery(Auction.class, "id");
        List<Auction> auctionList = databaseManager.loadObjects(loadAuction, NEW_AUCTION.getId());

        assertEquals(1, auctionList.size());
        Auction auction = auctionList.get(0);
        assertEquals(NEW_AUCTION.getId(), auction.getId());
        assertEquals(NEW_ACCOUNT.getId(), auction.getOwnerId());
        assertEquals(NEW_AUCTION.getBidIncrement(), auction.getBidIncrement());
    }
}