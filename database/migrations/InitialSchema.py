from migration import migration


@migration(order=1)
class InitialSchema:

    def up(self, db_cursor):
        print(f'Creating initial schema')
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS account (
    id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    alias varchar NOT NULL UNIQUE,
    email varchar NOT NULL UNIQUE,
    given_name varchar DEFAULT '' NOT NULL,
    surname varchar DEFAULT '' NOT NULL,
    password varchar NOT NULL,
    last_password varchar,
    image bytea,
    zelle_id varchar DEFAULT ''
);
        """)
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS rating (
    rater_id uuid NOT NULL
        CONSTRAINT rater
            REFERENCES account
            ON DELETE CASCADE,
    seller_id uuid NOT NULL
        CONSTRAINT seller
            REFERENCES account
            ON DELETE CASCADE,
    rating smallint NOT NULL
        CONSTRAINT rating_rating_check
            CHECK (rating >= 1 AND rating <= 5),
    PRIMARY KEY (rater_id, seller_id)
);
        """)
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS auction (
    id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    title varchar NOT NULL,
    description varchar NOT NULL,
    minimum_bid money NOT NULL,
    bid_increment money
        CONSTRAINT auction_bid_increment_check
            CHECK (bid_increment > 0::money),
    start_date date NOT NULL,
    end_date date NOT NULL,
    tax_percent double precision,
    published boolean DEFAULT false,
    owner_id uuid NOT NULL
        CONSTRAINT owner
            REFERENCES account
            ON DELETE CASCADE,
    CONSTRAINT end_after_start
        CHECK (start_date > end_date)
);
        """)
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS auction_view (
    id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    timestamp date DEFAULT now(),
    auction_id uuid NOT NULL
        CONSTRAINT auction
            REFERENCES auction
            ON DELETE CASCADE,
    viewer_id uuid NOT NULL
        CONSTRAINT viewer
            REFERENCES account
            ON DELETE CASCADE
);
        """)
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS category (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL UNIQUE,
    description varchar
);
        """)
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS picture (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    picture_name varchar NOT NULL,
    picture_sequence smallint DEFAULT 0,
    picture_data bytea NOT NULL,
    auction_id uuid NOT NULL
        CONSTRAINT auction
            REFERENCES auction
            ON DELETE CASCADE
);
        """)
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS bid (
    id uuid DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    timestamp date DEFAULT now(),
    amount money NOT NULL
        CONSTRAINT bid_amount_check
            CHECK (amount > 0::money),
    owner_id uuid NOT NULL
        CONSTRAINT bidder
            REFERENCES account
            ON DELETE CASCADE,
    auction_id uuid NOT NULL
        CONSTRAINT auction
            REFERENCES auction
            ON DELETE CASCADE
);
        """)
        db_cursor.execute("""
CREATE TABLE IF NOT EXISTS auction_category (
    auction_id uuid NOT NULL
        CONSTRAINT auction
            REFERENCES auction
            ON DELETE CASCADE,
    category_id integer NOT NULL
        CONSTRAINT category
            REFERENCES category
            ON DELETE CASCADE,
    PRIMARY KEY (auction_id, category_id)
);
        """)
        print(f'Finished creating needed tables')

    def down(self, db_cursor):
        db_cursor.execute("DROP TABLE IF EXISTS auction_category;")
        db_cursor.execute("DROP TABLE IF EXISTS picture;")
        db_cursor.execute("DROP TABLE IF EXISTS auction_view;")
        db_cursor.execute("DROP TABLE IF EXISTS bid;")
        db_cursor.execute("DROP TABLE IF EXISTS rating;")
        db_cursor.execute("DROP TABLE IF EXISTS category;")
        db_cursor.execute("DROP TABLE IF EXISTS auction;")
        db_cursor.execute("DROP TABLE IF EXISTS account;")
