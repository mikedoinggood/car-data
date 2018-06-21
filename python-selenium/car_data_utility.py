import random
import string

def generate_random_trim_level():
    random_trim_level = ''.join([random.choice(string.ascii_letters + string.digits) for n in range(0, 5)])

    return random_trim_level

def get_car_string(car):
    return car['year'] + " " + car['make'] + " " + car['model'] + " With trim levels: " + ",".join(car['trim_levels'])

