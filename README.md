# Final Project

## Student Info

1. Student name: `Dustin Homan`   Student netID: `deh5`
2. Student name: `Amy Rueve`   Student netID: `ar126`
3. Student name: `Nebojsa Dimic`   Student netID: `nsd4`
4. Student name: `Zach Dao`   Student netID: `zrd3`
5. Student name: `Geoffrey Hardy`   Student netID: `gth1`
6. Student name: `Jay Hyatt`   Student netID: `ah86`

Team name: `COMP610 RiceBay Team B`

Heroku app URL: [`https://comp610-ricebay-team-b.herokuapp.com/`](https://comp610-ricebay-team-b.herokuapp.com/)

## Dependencies
### Java SDK 17+

Please install the Java SDK manually or via Intellij.

### NodeJS LTS

Please follow the instructions [here](https://nodejs.org/en/download/) to install NodeJS.

## How to run

### Run in IDEA

To run the code in IDEA, navigate
to your [Controller](src/main/java/edu/rice/comp610/controller/Controller.java)
and click the "run" icon next to the `main()` function.

### Run in command line

Execute following:

```shell
mvn clean install
mvn exec:java
```

## How to open
To open your app in a local browser, navigate to http://localhost:4567 or your deployed Heroku url. 
## How to deploy on Heroku
```shell
 mvn heroku:deploy
 # Will not succeed if your code contains any error.
```

## How to test

### Test in IDEA

To run all the unit tests in IDEA, go to the IDEA tool bar - Run - Edit Configuration - click the "+" on the upper left -
select JUNIT - select "All in package" and apply - Select the run configuration you just created in top right - click
Run or Run with coverage.

### Test in command line

Execute following:

```shell
mvn clean test
```

## Development Guides

### Adding a new table? Altering a table? You need the "migrator"!

To control our database schema changes, we have a small python utility called the migrator. A migration consists of an
"up" action, that is the steps needed to migrate the database from one state to the desired next state. And a "down" action,
that is the steps needed to undo the "up" action. The script tracks which migrations have been run against the target DB and
only runs the ones it needs to.

#### Generate a migration

To generate a new migration, pick a useful migration name (i.e. InitialSchema or RemoveDefaultPayment) that is unique to
the migration, and then run:

```shell
cd database
python migrator.py generate <migration name>
```

The new migration class will be in `database/migrations/<migration name>.py` with empty "up" and "down" functions. As an
argument to the "up" and "down" functions, the migrator will pass you a psycopg2 cursor so that you can execute your
desired SQL.

As displayed in the output, please add an import in `database/migrator.py` for your new migration. We need to have the
import so that the decorators that register the migrations are executed.

#### Running Migrations

To apply the migrations in the `migrations` directory, get your database connection information and run:

```shell
cd database
python migrator.py migrate --db-host <hostname> --db-port <port> --db-user <username> --db-password <password> --db-name <database name> up
```

This will inspect the `migrations` table in the target database (or create one if it doesn't exist) and run all migrations
that haven't been run against the target database.

#### Need to undo a migration?

To apply the "down" function to a database, find out which migrations the database has run by running:

```shell
cd database
python migrator.py migrate --db-host <hostname> --db-port <port> --db-user <username> --db-password <password> --db-name <database name> list
```

Determine the state that you wish to revert to. Also read as, what is the last migration you want applied? Now run the following:

```shell
python migrator.py migrate --db-host <hostname> --db-port <port> --db-user <username> --db-password <password> --db-name <database name> list --stop-at <last migration name>
```

Is your desired state a clean DB? If so, just omit the `--stop-at` argument.