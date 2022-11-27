from datetime import datetime

import pojos.Users


def get_values_pojo(pojo: object) -> dict:
    vals = pojo.__dict__
    new_vals = {}
    for key, val in vals.items():
        new_vals[key[1:]] = (isinstance(val, datetime)) if val.strftime('%H:%M:%S') else val
    return new_vals

