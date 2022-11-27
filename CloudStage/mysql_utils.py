import mysql.connector
from mysql.connector import errorcode
from pojos import Users, Risks, Locations
import utils


def open_connection(db_name, u, p):
    try:
        connection = mysql.connector.connect(
            user=u,
            password=p,
            host="localhost",
            database=db_name
        )
        print("Connection with database started")
        return connection
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)


def start_values(connection: mysql.connector.connection):
    table_names = [
        ["users", Users.users_table_specs],
        ["risks", Risks.risks_table_specs],
        ["locations", Locations.locations_table_specs]
    ]
    for table_name, table_specs in table_names:
        create_table(connection, table_name, table_specs)


def create_table(connection: mysql.connector.connection, table_name, field_specs):
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


def add_values(connection, table_name, val):
    cursor = connection.cursor()
    keys = str(utils.get_values_pojo(val).keys())[11:-2].replace("'", "")
    vals = str(utils.get_values_pojo(val).values())[13:-2]
    query = "INSERT INTO {0} ({1}) VALUES ({2})".format(table_name, keys, vals)
    print(query)
    cursor.execute(query)
    connection.commit()
    cursor.close()


def update_values(connection: mysql.connector.connection, table_name, val, delimiters):
    cursor = connection.cursor()
    keys = list(utils.get_values_pojo(val).keys())
    vals = list(utils.get_values_pojo(val).values())
    vals_to_update = ""
    for i in range(len(keys)):
        vals_to_update += keys[i] + "=" + vals[i] + ", "
    vals_to_update = vals_to_update[:-2]
    query = "UPDATE {0} SET {1} WHERE {2}".format(table_name, vals_to_update, delimiters)
    print(query)
    cursor.execute(query)
    connection.commit()
    cursor.close()


def close(connection):
    connection.close()
    print("Connection terminated")


if __name__ == "__main__":
    pass
