risks_table_specs = [
    ["user_id", "VARCHAR(255)", "NOT NULL"],
    ["severity", "VARCHAR(10)", "NOT NULL"],
    ["risk", "VARCHAR(10)", "NOT NULL"],
    ["PRIMARY KEY (user_id)"],
    ["FOREIGN KEY (user_id) REFERENCES users(user_id)"]
]


class Risk:
    def __init__(self, user_id="", severity="", risk=""):
        self._user_id = user_id
        self._severity = severity
        self._risk = risk

    @property
    def user_id(self):
        return self._user_id

    @user_id.setter
    def user_id(self, value):
        self._user_id = value

    @property
    def severity(self):
        return self._severity

    @property
    def risk(self):
        return self._risk

    @severity.setter
    def severity(self, value):
        self._severity = value

    @risk.setter
    def risk(self, value):
        self._risk = value
