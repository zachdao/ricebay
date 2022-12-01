from migration import migration


@migration(order=3)
class DefaultCategories:

    def __init__(self):
        self.default_categories = [
            "'Lawn'",
            "'Furniture'",
            "'Electronics'",
            "'Fashion'",
            "'Art'",
            "'Sporting Goods'",
            "'Books'",
            "'Movies'",
            "'Other'"
        ]
    
    def up(self, db_cursor):
        db_cursor.execute("""
INSERT INTO category (name) VALUES {}
        """.format(','.join([f'({value})' for value in self.default_categories])))

    def down(self, db_cursor):
        db_cursor.execute("""
DELETE FROM category WHERE name IN ({}) 
        """.format(','.join(self.default_categories)))

        