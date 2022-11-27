risk_count_table_specs = [
    ["risk_type", "VARCHAR(10)", "NOT NULL"],
    ["risk_c", "INT"],
    ["PRIMARY KEY (risk_type)"]
]


class RiskCount:
    def __init__(self, risk_type="", risk_c=0):
        self._risk_type = risk_type
        self._risk_c = risk_c

    @property
    def risk_type(self):
        return self._risk_type

    @property
    def risk_c(self):
        return self._risk_c

    @risk_type.setter
    def risk_type(self, value):
        self._risk_type = value

    @risk_c.setter
    def risk_c(self, value):
        self._risk_c = value
