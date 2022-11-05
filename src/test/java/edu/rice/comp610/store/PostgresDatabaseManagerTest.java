package edu.rice.comp610.store;

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
    private static final Auction NEW_AUCTION = new Auction();
    {
        NEW_AUCTION.setTitle("New Auction");
        NEW_AUCTION.setDescription("New Auction Description");
        NEW_AUCTION.setBidIncrement(1);
        NEW_AUCTION.setMinimumBid(123);
        NEW_AUCTION.setStartDate(parseDate("2022-10-20"));
        NEW_AUCTION.setEndDate(parseDate("2022-10-20"));
        NEW_AUCTION.setTaxPercent(0.06f);
        NEW_AUCTION.setPublished(true);
        NEW_AUCTION.setId(UUID.randomUUID());

    }

    @BeforeAll
    static void setupDatabase() throws IOException, InterruptedException {
        PostgreSQLContainer container = DatabaseSetup.init();
        Properties props = new Properties();
        props.setProperty("user", container.getUsername());
        props.setProperty("password", container.getPassword());
//        databaseManager = new PostgresDatabaseManager(container.getJdbcUrl(), props);
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
    void saveAndLoadObjects() throws Exception {
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
}