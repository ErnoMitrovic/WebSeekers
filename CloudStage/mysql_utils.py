import mysql.connector
from mysql.connector import Error

'''
To install mysql-connector run this command in the terminal:  pip install mysql-connector-python
A nice tutorial is here: https://www.w3schools.com/python/python_mysql_getstarted.asp
'''
import mysql.connector
from mysql.connector import Error


def connect_mysql(host, user, password):
    dbconn = mysql.connector.connect(host, user, password)
    return dbconn


def disconnect_db(connection):
    if connection.is_connected():
        connection.close()
        print("MySQL connection is closed")


def create_db(dbname, conn):
    mycursor = conn.cursor()
    mycursor.execute("CREATE DATABASE " + dbname)


def create_table(connection, dbname, sqlSTR):
    try:
        cursor = connection.cursor()
        cursor.execute("USE " + dbname)
        result = cursor.execute(sqlSTR)
        print("Laptop Table created successfully")
    except mysql.connector.Error as error:
        print("Failed to create table in MySQL: {}".format(error))


def insert_values(connection, sqlSTR):
    try:
        cursor = connection.cursor()
        cursor.execute(sqlSTR)
        connection.commit()
        print(cursor.rowcount, "Record inserted successfully into Laptop table")
        cursor.close()
    except mysql.connector.Error as error:
        print("Failed to insert record into Laptop table {}".format(error))


def select_values(dbConn, sqlSTRQuery):
    mycursor = dbConn.cursor()
    mycursor.execute(sqlSTRQuery)
    myresult = mycursor.fetchall()
    for x in myresult:
        print(x)
    return myresult


def drop_db(dbname, connection):
    cursor = connection.cursor()
    sql = 'DROP DATABASE ' + dbname
    cursor.execute(sql)


host = 'localhost'
user = 'root'
password = 'ErnoMitrovic2.718281828459045235'
dbname = 'rescues'

sqlSTR = """CREATE TABLE IF NOT EXIST Users ( 
                            Id VARCHAR(255) NOT NULL,
                            Status DATETIME 
                            PRIMARY KEY (Id));
            CREATE TABLE IF NOT EXIST Risk (
                            Id VARCHAR(255) NOT NULL,
                            Severity VARCHAR(10),
                            Location Varchar(255), 
                            PRIMARY KEY (Id)
                            FOREIGN KEY (Id) REFERENCES Users(Id)
                            );
                             """
mySql_insert_severity = """INSERT INTO Users (Id, Status) 
                           VALUES 
                           (15, 'Lenovo ThinkPad P71', 6459, '2019-08-14') """

dbconn = connect_mysql(host, user, password)
drop_db('teste', dbconn)
create_db("teste", dbconn)
create_table(dbconn, dbname, sqlSTR)

# insert_values(connection, mySql_insert_query)
disconnect_db(dbconn)
