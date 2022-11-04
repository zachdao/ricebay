package edu.rice.comp610.testing;

import java.io.File;
import java.io.IOException;

public class Migrator {

    private String host;
    private int port;
    private String user;
    private String password;
    private String databaseName;

    public Migrator() {
    }

    public Migrator withHost(String host) {
        this.host = host;
        return this;
    }

    public Migrator withPort(int port) {
        this.port = port;
        return this;
    }

    public Migrator withUser(String user) {
        this.user = user;
        return this;
    }

    public Migrator withPassword(String password) {
        this.password = password;
        return this;
    }

    public Migrator withDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public void up() throws IOException, InterruptedException {
        Process process = new ProcessBuilder()
                .directory(new File("database"))
                .command("python", "-m", "migrator", "migrate",
                        "--db-host", host,
                        "--db-port", String.valueOf(port),
                        "--db-user", user,
                        "--db-password", password,
                        "--db-name", databaseName,
                        "up")
                .inheritIO()
                .start();

        if (process.waitFor() != 0) {
            throw new RuntimeException("migrator failed");
        }
    }
}
