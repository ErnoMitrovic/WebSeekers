# This is a sample Python script.
import datetime
import mysql_utils as db

# Press Mayús+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
from pojos import Users


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    conn = db.open_connection("rescues", "root", "ErnoMitrovic2.718281828459045235")
    curs = conn.cursor()
    curs.execute("SELECT last_update FROM users WHERE user_id=\"ID01\"")
    last_update = curs.fetchone()
    curs.close()
    delta = datetime.datetime.now() - last_update[0]
    print(delta.minute)


# See PyCharm help at https://www.jetbrains.com/help/pycharm/
