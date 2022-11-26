# This is a sample Python script.
import datetime

# Press Mayús+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
from pojos import Users


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    user = Users.User("e", datetime.time())
    val = user.user_id
    print(val)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
