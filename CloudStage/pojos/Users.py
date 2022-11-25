import time

users_table_specs = [
    ["user_id", "VARCHAR(255)", "NOT NULL"],
    ["last_update", "TIME"],
    ["PRIMARY KEY (user_id)"]
]


class Users:
    def __init__(self, user_id="", last_update=time.strftime('%H:%M:%S')):
        self.user_id = user_id
        self.last_update = last_update

    @property
    def user_id(self):
        return self.user_id

    @property
    def last_update(self):
        return self.last_update

    @user_id.setter
    def user_id(self, value):
        self._user_id = value

    @last_update.setter
    def last_update(self, value):
        self._last_update = value
