from migration import migration


@migration(order=2)
class MaxBid:
    
    def up(self, db_cursor):
        db_cursor.execute("""
ALTER TABLE bid ADD COLUMN max_bid money
        """)

    def down(self, db_cursor):
        db_cursor.execute("""
ALTER TABLE bid DROP COLUMN max_bid
        """)
        