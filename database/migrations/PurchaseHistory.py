from migration import migration


@migration(order=4)
class PurchaseHistory:
    
    def up(self, db_cursor):
        db_cursor.execute("""
ALTER TABLE auction ADD COLUMN buyer_paid boolean DEFAULT false
        """)
        db_cursor.execute("""
ALTER TABLE auction ADD COLUMN winner_id uuid
    CONSTRAINT buyer
        REFERENCES account
        ON DELETE SET NULL
        """)
    
    def down(self, db_cursor):
        db_cursor.execute("""
ALTER TABLE auction DROP COLUMN buyer_paid
        """)
        db_cursor.execute("""
ALTER TABLE auction DROP CONSTRAINT buyer
        """)
        db_cursor.execute("""
ALTER TABLE auction DROP COLUMN winner_id
        """)

        