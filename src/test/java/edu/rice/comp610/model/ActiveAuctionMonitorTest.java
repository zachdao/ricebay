package edu.rice.comp610.model;

import edu.rice.comp610.controller.ActiveAuctionMonitor;
import edu.rice.comp610.controller.AuctionManager;
import edu.rice.comp610.store.PostgresDatabaseManager;
import edu.rice.comp610.store.PostgresQueryManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.testing.DatabaseSetup;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.DatabaseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import static edu.rice.comp610.testing.Dates.parseDate;

public class ActiveAuctionMonitorTest {

    private static PostgresDatabaseManager databaseManager;
    private final PostgresQueryManager queryManager = new PostgresQueryManager();
    private final AuctionManager auctionManager = new StandardAuctionManager(queryManager, databaseManager);


    private static final Account NEW_ACCOUNT = new Account();
    static {
        NEW_ACCOUNT.setId(UUID.randomUUID());
        NEW_ACCOUNT.setGivenName("Name");
        NEW_ACCOUNT.setSurname("Surname");
        NEW_ACCOUNT.setEmail("mail@mail.com");
        NEW_ACCOUNT.setAlias("testalias");
        NEW_ACCOUNT.setPassword("password123");
    }

    private static final Auction NEW_AUCTION = new Auction();
    static {
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
        PostgresDatabaseManager.initialize(container.getJdbcUrl(), props);
        databaseManager = PostgresDatabaseManager.getInstance();
    }

    @AfterAll
    static void tearDown() {
        DatabaseSetup.shutdown();
    }

    @Test
    void unpublishTest() throws BadRequestException, DatabaseException {
        Query<Account> saveAccount = queryManager.makeUpdateQuery(Account.class);
        databaseManager.saveObjects(saveAccount, NEW_ACCOUNT);

        List<Auction> expiredAuctions = new ArrayList<>();
        expiredAuctions.add(NEW_AUCTION);

        ActiveAuctionMonitor activeAuctionMonitor = new ActiveAuctionMonitor(auctionManager);
        activeAuctionMonitor.unpublish(expiredAuctions);
        assertFalse(NEW_AUCTION.getPublished());


    }
}
