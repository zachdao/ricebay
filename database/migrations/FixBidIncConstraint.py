from migration import migration


@migration(order=5)
class FixBidIncConstraint:
    
    def up(self, db_cursor):
        db_cursor.execute("""
ALTER TABLE auction DROP CONSTRAINT auction_bid_increment_check
        """)

    def down(self, db_cursor):
        db_cursor.execute("""
ALTER TABLE auction ADD CONSTRAINT auction_bid_increment_check
CHECK (bid_increment > 0::money)
        """)

        