severity_count_table_specs = [
    ["sev_type", "VARCHAR(10)", "NOT NULL"],
    ["sev_c", "TIME"],
    ["PRIMARY KEY (sev_type)"]
]


class SeverityCount:
    def __init__(self, sev_type="", sev_c=0):
        self._sev_type = sev_type
        self._sev_c = sev_c

    @property
    def sev_type(self):
        return self._sev_type

    @property
    def risk_c(self):
        return self._sev_c

    @sev_type.setter
    def sev_type(self, value):
        self._sev_type = value

    @risk_c.setter
    def risk_c(self, value):
        self._sev_c = value
