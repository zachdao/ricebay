import argparse
import importlib.util
import os
import sys

import psycopg2

from migration import migrations


def dynamic_import(base_dir):
    """
    Dynamically import all python files from a given directory
    :param base_dir: the directory that contains the python files you want imported
    :return:
    """
    files_to_import = [os.path.join(base_dir, f) for f in os.listdir(os.path.join(base_dir)) if f.endswith('.py')]
    for file in files_to_import:
        name = os.path.split(file)[-1].strip(".py")
        spec = importlib.util.spec_from_file_location(name, file)
        mod = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(mod)
        globals()[name] = mod


# Import our migration files so their decorators register
dynamic_import('migrations')

parser = argparse.ArgumentParser(
    description="migrator is a utility that generates migrations and runs migrations (up or down)")
subparsers = parser.add_subparsers(help='valid commands: generate | migrate | list', required=True,
                                   metavar='{generate, migrate, list}', dest='command')

parser_generate = subparsers.add_parser('generate',
                                        help='generate takes a single argument, the name of the migration, and generates the skeleton for that migration')
parser_generate.add_argument('migration_name', help='the name of the migration (i.e InitialSchema)')

parser_migrate = subparsers.add_parser('migrate', help='run migrations')
parser_migrate.add_argument('--db-host', required=True, help='The host IP or domain name of the target DB')
parser_migrate.add_argument('--db-port', required=True, help='The host port of the target DB')
parser_migrate.add_argument('--db-user', required=True, help='The username to authenticate with the target DB')
parser_migrate.add_argument('--db-password', required=True, help='The password to authenticate with the target DB')
parser_migrate.add_argument('--db-name', default='', help='The database to connect to on the target DB')
parser_migrate.add_argument('--migration-table-override', default='migrations',
                            help='The table name to store/find migrations in')
migrate_subparsers = parser_migrate.add_subparsers(help='valid migrate sub-commands: up | down | list', required=True,
                                                   metavar='{up, down, list}', dest='migrate_command')

parser_migrate_up = migrate_subparsers.add_parser('up',
                                                  help='run all migrations that haven\'t been ran on the target DB')

parser_migrate_down = migrate_subparsers.add_parser('down',
                                                    help='revert all migrations to a specific migration on the target DB')
parser_migrate_down.add_argument('--stop-at', default=None, help='The last migration to leave applied to the target db')

parser_migrate_list = migrate_subparsers.add_parser('list',
                                                    help='list all migrations that have been ran on the target DB')

parser_list = subparsers.add_parser('list', help='list migrations in the migration folder')

args = parser.parse_args(sys.argv[1:])


def generate(base_path):
    migration_file = f'{args.migration_name}.py'
    migration_files = [f for f in os.listdir(base_path) if
                          os.path.isfile(os.path.join(base_path, f))]
    if migration_file in migration_files:
        sys.stderr.write(f'{args.migration_name} already exists as a migration, choose a different name!')
        return

    with open(os.path.join(base_path, migration_file), 'w') as f:
        f.write(f"""from database.migration import migration


@migration(order={len(migration_files) + 1})
class {args.migration_name}:
    
    def up(self, db_cursor):
        pass
    
    def down(self, db_cursor):
        pass

        """)
    print(f'Your migration has been created at database/migrations/{args.migration_name}.py')


def main():
    migrations_path = os.path.join(os.getcwd(), 'migrations')
    if args.command == 'list':
        migrations.list_migrations()
    elif args.command == 'generate':
        generate(migrations_path)
    elif args.command == 'migrate':
        the_connection = psycopg2.connect(f'host={args.db_host} port={args.db_port} user={args.db_user} password={args.db_password} dbname={"" if args.db_name == "" else args.db_name}')
        if args.migrate_command == 'up':
            migrations.run_migrations_up(the_connection, args.migration_table_override)
        elif args.migrate_command == 'down':
            answer = ''
            while answer not in ['y', 'n', 'yes', 'no']:
                answer = input('Down migrations can be destructive to the data in the database.\nAre you sure you want to continue? [Y/N] ').lower()
            if answer in ['y', 'yes']:
                migrations.run_migrations_down(the_connection, args.migration_table_override, args.stop_at)
            else:
                print('Not running migrations down')
        elif args.migrate_command == 'list':
            migrations.list_remote_migrations(the_connection, args.migration_table_override)
        the_connection.close()


if __name__ == '__main__':
    main()
