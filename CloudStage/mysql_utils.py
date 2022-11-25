import mysql.connector
from mysql.connector import errorcode
from pojos import Users, Risks
import utils


def open_connection(db_name, u, p):
    try:
        connection = mysql.connector.connect(
            user=u,
            password=p,
            host='localhost',
            database=db_name
        )
        print("Connection started")
        return connection
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)


def start_values(connection):
    table_names = [
        ["users", Users.users_table_specs],
        ["risks", Risks.risks_table_specs]
    ]
    for table_name, table_specs in table_names:
        create_table(connection, table_name, table_specs)


def create_table(connection, table_name, field_specs):
    query = "CREATE TABLE IF NOT EXISTS " + table_name + "("
    cursor = connection.cursor()
    for fields in field_specs:
        for field in fields:
            query += field + " "
        if fields != field_specs[-1]:
            query += ", "
    query += ");"
    # print(query)
    cursor.execute(query)
    if cursor.fetchone() is None:
        print("Table", table_name, "created")
    cursor.close()


def close(connection):
    connection.close()
    print("Connection terminated")

