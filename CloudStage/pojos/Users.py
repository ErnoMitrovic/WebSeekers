import datetime
import time

users_table_specs = [
    ["user_id", "VARCHAR(255)", "NOT NULL"],
    ["last_update", "TIME"],
    ["PRIMARY KEY (user_id)"]
]


class User:
    def __init__(self, user_id="", last_update=datetime.datetime.now()):
        self._user_id = user_id
        self._last_update = last_update

    @property
    def user_id(self):
        return self._user_id

    @property
    def last_update(self):
        return self._last_update

    @user_id.setter
    def user_id(self, value):
        self._user_id = value

    @last_update.setter
    def last_update(self, value):
        self._last_update = value
