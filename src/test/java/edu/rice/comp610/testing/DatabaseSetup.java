package edu.rice.comp610.testing;

import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.net.URI;

public class DatabaseSetup {
    public static final String DATABASE_NAME = "ricebay";
    public static final String USERNAME = "ricebay";
    public static final String PASSWORD = "secret";

    private final static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15.0")
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    public static PostgreSQLContainer init() throws IOException, InterruptedException {
        postgresqlContainer.start();

        String url = postgresqlContainer.getJdbcUrl();
        URI uri = URI.create(url.substring(5));

        System.out.println("JDBC URL: " + url);
        new Migrator()
                .withHost(postgresqlContainer.getHost())
                .withPort(uri.getPort())
                .withDatabaseName(uri.getPath().substring(1))
                .withUser(postgresqlContainer.getUsername())
                .withPassword(postgresqlContainer.getPassword())
                .up();
        return postgresqlContainer;
    }

    public static void shutdown() {
        postgresqlContainer.stop();
    }

}
