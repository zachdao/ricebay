import sys


class Migration:
    def __init__(self, instance, order):
        self.instance = instance
        self.order = order


class Migrations:

    def __init__(self):
        self.registry = dict()

    def list_migrations(self):
        [print(k) for k in self.registry.keys()]

    def list_remote_migrations(self, conn, migration_table):
        cur = conn.cursor()
        cur.execute(f'CREATE TABLE IF NOT EXISTS {migration_table} (name varchar PRIMARY KEY, timestamp date DEFAULT now());')
        conn.commit()
        cur.execute(f'SELECT * FROM {migration_table} ORDER BY timestamp;')
        migration_tuples = list(cur.fetchall())
        [print(f'Migrations that have run {row[0]} @ {row[1]}') for row in migration_tuples]

    def run_migrations_up(self, conn, migration_table):
        cur = conn.cursor()
        cur.execute(f'CREATE TABLE IF NOT EXISTS {migration_table} (name varchar PRIMARY KEY, timestamp date DEFAULT now());')
        conn.commit()
        cur.execute(f'SELECT * FROM {migration_table};')
        migration_tuples = list(cur.fetchall())

        runnable_migrations = list(self.registry.items())
        print(f'Found {len(runnable_migrations)} runnable migrations')
        runnable_migrations.sort(key=lambda t: t[1].order)
        ran_migrations = set([row[0] for row in migration_tuples])
        for name, runnable_migration in runnable_migrations:
            if name in ran_migrations:
                print(f'Already ran migration {name}, skipping...')
            else:
                runnable_migration.instance.up(cur)
                cur.execute(f'INSERT INTO {migration_table} (name) VALUES (\'{name}\');')

        conn.commit()
        print(f'Finished running migrations!')
        cur.close()

    def run_migrations_down(self, conn, migration_table, stop_at):
        cur = conn.cursor()
        cur.execute(f'SELECT * FROM {migration_table};')
        migration_tuples = list(cur.fetchall())

        runnable_migrations = list(self.registry.items())
        print(f'Found {len(runnable_migrations)} runnable migrations')
        runnable_migrations.sort(reverse=True, key=lambda t: t[1].order)
        ran_migrations = set([row[0] for row in migration_tuples])
        for name, runnable_migration in runnable_migrations:
            if name == stop_at:
                break
            elif name in ran_migrations:
                runnable_migration.instance.down(cur)
                cur.execute(f'DELETE FROM {migration_table} WHERE name=\'{name}\';')
            else:
                print(f'Haven\'t ran migration {name}, skipping...')
        conn.commit()
        print(f'Finished running migrations down!')
        cur.close()


migrations = Migrations()


def migration_decorator_factory(order):
    def _migration(migrator_class):
        """
        Decorator to mark a class as a migrator class. These classes will be registered by their class name and their
        corresponding "up" and "down" functions will be called during migration.

        :param migrator_class: a class with a function named "up" and a function named "down"
        :return: the decorated class
        """
        instance = migrator_class()
        if 'up' not in dir(instance):
            sys.stderr.write(f'{migrator_class.__name__} is missing an "up" function\n')
            sys.exit(1)
        if 'down' not in dir(instance):
            sys.stderr.write(f'{migrator_class.__name__} is missing a "down" function\n')
            sys.exit(1)
        migrations.registry[migrator_class.__name__] = Migration(instance, order)
        return migrator_class

    return _migration


migration = migration_decorator_factory
