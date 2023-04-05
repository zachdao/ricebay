# Final Project

## Student Info

1. Student name: `Dustin Homan`   Student netID: `deh5`
2. Student name: `Amy Rueve`   Student netID: `ar126`
3. Student name: `Nebojsa Dimic`   Student netID: `nsd4`
4. Student name: `Zach Dao`   Student netID: `zrd3`
5. Student name: `Geoffrey Hardy`   Student netID: `gth1`
6. Student name: `Jay Hyatt`   Student netID: `ah86`

Team name: `COMP610 RiceBay Team B`

Heroku app URL: `https://comp610-ricebay-team-b.herokuapp.com/`

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
