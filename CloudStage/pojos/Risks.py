risks_table_specs = [
    ["user_id", "VARCHAR(255)", "NOT NULL"],
    ["severity", "VARCHAR(10)", "NOT NULL"],
    ["risk", "VARCHAR(10)", "NOT NULL"],
    ["PRIMARY KEY (user_id)"],
    ["FOREIGN KEY (user_id) REFERENCES users(user_id)"]
]


class Users:
    def __init__(self, severity="", risk=""):
        self.severity = severity
        self.risk = risk

    @property
    def severity(self):
        return self.severity

    @property
    def risk(self):
        return self.risk

    @severity.setter
    def severity(self, value):
        self._severity = value

    @risk.setter
    def risk(self, value):
        self._risk = value
