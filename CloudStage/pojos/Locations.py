locations_table_specs = [
    ["user_id", "VARCHAR(255)", "NOT NULL"],
    ["latitude", "VARCHAR(50)", "NOT NULL"],
    ["longitude", "VARCHAR(50)", "NOT NULL"],
    ["PRIMARY KEY (user_id)"],
    ["FOREIGN KEY (user_id) REFERENCES users(user_id)"]
]


class Locations:
    def __init__(self, latitude="", longitude=""):
        self._latitude = latitude
        self._longitude = longitude

    @property
    def latitude(self):
        return self._latitude

    @property
    def longitude(self):
        return self._longitude

    @latitude.setter
    def latitude(self, value):
        self._latitude = value

    @longitude.setter
    def longitude(self, value):
        self._longitude = value
