import argparse
import sys
import psycopg2
import random
import requests
from uuid import uuid4
import hashlib
import datetime
import time


parser = argparse.ArgumentParser(description="Add data to the RiceBay database")

parser.add_argument('--db-host', required=True, help='The host IP or domain name of the target DB')
parser.add_argument('--db-port', required=True, help='The host port of the target DB')
parser.add_argument('--db-user', required=True, help='The username to authenticate with the target DB')
parser.add_argument('--db-password', required=True, help='The password to authenticate with the target DB')
parser.add_argument('--db-name', default='', help='The database to connect to on the target DB')
parser.add_argument('--accounts', default=10, help='Number of accounts to add to the database')
parser.add_argument('--auctions', default=20, help='Number of auctions to add to the database')

# Parse the cmd line arguments
args = parser.parse_args(sys.argv[1:])

# Generate the database connection
the_connection = psycopg2.connect(
            f'host={args.db_host} port={args.db_port} user={args.db_user} password={args.db_password} dbname={"" if args.db_name == "" else args.db_name}')

def getDatabaseTables(conn):
    cur = conn.cursor()
    cur.execute(f"SELECT * FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema';")
    tables = [table[1] for table in cur.fetchall()]
    return tables

def describeTable(conn, table):
    cur = conn.cursor()
    cur.execute(f"SELECT table_name, column_name, data_type FROM information_schema.columns WHERE table_name = '{table}';")
    details = cur.fetchall()
    return details

def getUserNames(num):
    # Uses the namey REST api to generate user names randomly

    query = {'count': num, 'type' : 'surname'}
    surNames = getNames(query)

    query = {'count': num}
    givenNames = getNames(query)
    
    if not surNames or not givenNames:
        names = None
    else:
        names = [f"{fn} {sn}" for fn, sn in zip(givenNames, surNames)]
    return names
    
def getNames(query):

    try:
        names = requests.get("https://namey.muffinlabs.com/name.json?", params=query)
        onames = [name.strip('"') for name in names.text.strip("[").strip("]").split(",")]
    except:
        onames = None
    return onames

def getAccounts(conn):

    cur = conn.cursor()
    cur.execute(f"SELECT * FROM account")
    return cur.fetchall()   

def getAuctions(conn):

    cur = conn.cursor()
    cur.execute(f"SELECT * FROM auction")
    return cur.fetchall()   

def getRating(conn):

    cur = conn.cursor()
    cur.execute(f"SELECT * FROM rating")
    return cur.fetchall()   

def getAuctionViews(conn):

    cur = conn.cursor()
    cur.execute(f"SELECT * FROM auction_view")
    return cur.fetchall()   

def getCat(conn):

    cur = conn.cursor()
    cur.execute(f"SELECT * FROM category")
    return cur.fetchall()   


def getAuctionMinIncrement(conn, auction_id):
    cur = conn.cursor()
    cur.execute(f"SELECT minimum_bid, bid_increment FROM auction WHERE id='{auction_id}'")
    return cur.fetchall()   

def getAuctionDateRange(conn, auction_id):
    cur = conn.cursor()
    cur.execute(f"SELECT start_date, end_date FROM auction WHERE id='{auction_id}'")
    return cur.fetchall()   

def getBids(conn):
    cur = conn.cursor()
    cur.execute(f"SELECT * FROM bid")
    return cur.fetchall()   

def getAucCat(conn):
    cur = conn.cursor()
    cur.execute(f"SELECT * FROM auction_category")
    return cur.fetchall()   

def getAccountIdList(conn):

    accountList = getAccounts(conn)
    return [a[0] for a in accountList]

def getCatIds(conn):
    cur = conn.cursor()
    cur.execute(f"SELECT id FROM category")
    return [int(a[0]) for a in cur.fetchall()]

def getAuctionIdList(conn):

    auctionList = getAuctions(conn)
    return [a[0] for a in auctionList]

def getNYTbestSellers(date_to_search):

    nyt_key = "wrFofm6v1vwtPEwg018692Ozg7Z43s3X"
    response = requests.get(f"https://api.nytimes.com/svc/books/v3/lists/{date_to_search}/hardcover-fiction.json?api-key={nyt_key}")
    results = response.json()
    try:
        number_of_books = len(results['results']['books'])
        book = results['results']['books'][random.randint(0, number_of_books-1)]

        title = book['title']
        descrip = book['description']
        return (title, descrip)
    except:
        return None

def getRandomDate(minYear=2010, maxYear=2020):

    year = str(random.randint(minYear, maxYear))
    month = str(random.randint(1, 12))
    day = str(random.randint(1, 28))

    return f"{year}-{month.zfill(2)}-{day.zfill(2)}"

def getRandomDateRange():

    ndays = random.randint(2, 10)

    randdate = getRandomDate()
    d = [int(val) for val in randdate.split("-")]
    epoch = datetime.datetime(*d).timestamp()

    #pubepoch = epoch - 60 * 60 * 24 * random.randint(1, 4)
    #pubdate = datetime.datetime.fromtimestamp(pubepoch).strftime('%Y-%m-%d')

    endepoch = epoch + 60 * 60 * 24 * ndays
    enddate = datetime.datetime.fromtimestamp(endepoch).strftime('%Y-%m-%d')

    return (randdate, enddate)

def insertUser(conn, firstName, lastName):

    unique_id = str(uuid4())
    email = f"{firstName[0].lower()}{lastName[0].lower()}{str(random.randint(1, 99))}@rice.edu"
    alias = email[:email.index('@')]
    password = hashlib.md5(f"{email}".encode('utf-8')).hexdigest()

    cur = conn.cursor()
    cur.execute(f"INSERT INTO account(id, email, given_name, surname, password, alias) VALUES ('{unique_id}', '{email}', '{firstName}', '{lastName}', '{password}', '{alias}');")


def insertAuction(conn, owner_id, book):

    try:
        auction_dates = getRandomDateRange()
        unique_id = str(uuid4())
        published = True
        min_bid = random.uniform(10.0, 100.0)
        bid_inc = random.uniform(1.0, 10.0)
        start_date = auction_dates[0]
        end_date = auction_dates[1]
        tax_per = random.uniform(0.05, 0.1)
        title = book[0].replace("'", "''")
        descrip = book[1].replace("'", "''")
    
        cur = conn.cursor()
        cur.execute(f"INSERT INTO auction(owner_id, published, id, minimum_bid, bid_increment, start_date, end_date, tax_percent, title, description) VALUES ('{owner_id}', '{published}', '{unique_id}', '{min_bid}', '{bid_inc}', '{start_date}', '{end_date}', '{tax_per}', '{title}', '{descrip}');")
    except:
        pass

def insertRating(conn, rater, seller, rating):
    cur = conn.cursor()
    cur.execute(f"INSERT INTO rating(rater_id, seller_id, rating) VALUES ('{rater}', '{seller}', '{rating}');")

def insertBid(conn, ts, amount, bidder, auction):
    unique_id = str(uuid4())
    cur = conn.cursor()
    cur.execute(f"INSERT INTO bid(id, timestamp, amount, owner_id, auction_id) VALUES ('{unique_id}', '{ts}', '{amount}', '{bidder}', '{auction}');")

def insertCat(conn, name, descrip):
    cur = conn.cursor()
    cur.execute(f"INSERT INTO category(name, description) VALUES ('{name}', '{descrip}');")

def insertAuctionView(conn, viewer, auction, ts):

    unique_id = str(uuid4())
    cur = conn.cursor()
    cur.execute(f"INSERT INTO auction_view(id, timestamp, auction_id, viewer_id) VALUES ('{unique_id}', '{ts}', '{auction}', '{viewer}');")

def insertCatMap(conn, auction, cat_id):
    cur = conn.cursor()
    cur.execute(f"INSERT INTO auction_category(auction_id, category_id) VALUES ('{auction}', '{cat_id}');")

def deleteRatings(conn):
    cur = conn.cursor()
    cur.execute(f"DELETE FROM rating;")

def deleteCat(conn):
    cur = conn.cursor()
    cur.execute(f"DELETE FROM category;")

def deleteCatMap(conn):
    cur = conn.cursor()
    cur.execute(f"DELETE FROM auction_category;")

def terminate(conn):
    conn.commit()
    conn.close()

def getBidDate(sd, ed):

    sepoch = datetime.datetime.combine(sd, datetime.datetime.min.time())
    eepoch = datetime.datetime.combine(ed, datetime.datetime.min.time())
    newBid = random.uniform(sepoch, eepoch)
#    ts = datetime.date.fromtimestamp(newBid).strftime('%Y-%m-%d:%H:%M:%S')
    return newBid


# Add given number of random users to the database
try:
    for user in getUserNames(args.accounts):
        givenName, surName = user.split()
        insertUser(the_connection, givenName, surName)
except TypeError:
    print("Trouble creating accounts")

# Get a list of the account uuids
account_id_list = getAccountIdList(the_connection)
naccounts = len(account_id_list)

# Add given number of random auctions to the database
# These come from NYTime best seller list
for iauction in range(args.auctions):
    owner = account_id_list[random.randint(0, naccounts-1)]
    book = getNYTbestSellers(getRandomDate())
    insertAuction(the_connection, owner, book)
    time.sleep(2)


# Remove any existing ratings
deleteRatings(the_connection)

# Add Ratings to the database
for seller in account_id_list:
    raters = account_id_list.copy()
    nraters = random.randint(0, naccounts)
    for r in range(nraters):
        irater = random.randint(0, len(raters)-1)
        rater = raters.pop(irater)
        rating = random.randint(1, 5)
        insertRating(the_connection, rater, seller, rating)

# Get a list of the auction uuids
auction_id_list = getAuctionIdList(the_connection)
nauctions = len(auction_id_list)

# Add auction views to the database
for account in account_id_list:
    nviews = random.randint(0, nauctions)
    for iview in range(nviews):
        auction_id = auction_id_list[random.randint(0, nauctions-1)]
        days = random.uniform(0, 1000)
        ts = datetime.datetime.fromtimestamp(time.time()-(60*60*24*days)).strftime('%Y-%m-%d')
        insertAuctionView(the_connection, account, auction_id, ts)
        
# Add bids to the Auctions in the database
for auction in auction_id_list:
    minBid, bidInc = getAuctionMinIncrement(the_connection, auction)[0]
    # Really dont like this but out of ideas
    minBid = float(minBid.strip("$"))
    bidInc = float(bidInc.strip("$"))
    sdate, edate = getAuctionDateRange(the_connection, auction)[0]
    nbids = random.randint(0, 10)
    lastBid = sdate

    for bid in range(nbids):
        bidder = account_id_list[random.randint(0, naccounts-1)]
        amount = minBid + (bid * bidInc) 
        lastBid = getBidDate(lastBid, edate)

        insertBid(the_connection, lastBid, amount, bidder, auction)


# Add Categories to the database
deleteCat(the_connection)
categories = ['book', 'animal', 'furniture', 'IT', 'other']
for category in categories:
    insertCat(the_connection, category, category)

# Map Auctions to categories
deleteCatMap(the_connection)
cat_ids = getCatIds(the_connection)
for auction in auction_id_list:
    ncats = random.randint(1, min(3, len(categories)))
    auction_categories = cat_ids.copy()
    
    for icat in range(ncats):
        cat_index = random.randint(0, len(auction_categories)-1)
        cat_id = auction_categories.pop(cat_index)
        insertCatMap(the_connection, auction, cat_id)


# Get a list of the tables
#print(getDatabaseTables(the_connection))

# Describe a table
#print(describeTable(the_connection, "auction_category"))

# There are methods to pull the content of every table to check
#print(getAucCat(the_connection))
#print(getCat(the_connection))

terminate(the_connection)
