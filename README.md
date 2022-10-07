# Final Project

## Student Info

1. Student name: `NAME #1 HERE`   Student netID: `NetID #1 HERE`
2. Student name: `NAME #2 HERE`   Student netID: `NetID #2 HERE`
3. Student name: `NAME #3 HERE`   Student netID: `NetID #3 HERE`
4. Student name: `NAME #4 HERE`   Student netID: `NetID #4 HERE`
5. Student name: `NAME #5 HERE`   Student netID: `NetID #5 HERE`
6. Student name: `NAME #6 HERE`   Student netID: `NetID #6 HERE`

Team name: `Your team name here`

Heroku app URL: `YOUR HOSTED HEROKU URL HERE`

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