users_table_specs = [
    ["user_id", "VARCHAR(255)", "NOT NULL"],
    ["severity", "VARCHAR(10)", "NOT NULL"],
    ["risk", "VARCHAR(10)", "NOT NULL"],
    ["latitude", "VARCHAR(50)", "NOT NULL"],
    ["longitude", "VARCHAR(50)", "NOT NULL"],
    ["PRIMARY KEY (user_id)"],
    ["FOREIGN KEY (user_id) REFERENCES users(user_id)"]
]


class Users:
    def __init__(self, severity="", risk="", latitude="", longitude=""):
        self.severity = severity
        self.risk = risk
        self.latitude = latitude
        self.longitude = longitude

    @property
    def severity(self):
        return self.severity

    @property
    def risk(self):
        return self.risk

    @property
    def latitude(self):
        return self.latitude

    @property
    def longitude(self):
        return self.longitude

    @severity.setter
    def severity(self, value):
        self._severity = value

    @risk.setter
    def risk(self, value):
        self._risk = value

    @longitude.setter
    def longitude(self, value):
        self._longitude = value

    @latitude.setter
    def latitude(self, value):
        self._latitude = value
