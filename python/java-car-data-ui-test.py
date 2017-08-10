from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

import logging
import logging.handlers
import re
import time


# Logging setup
LOG = logging.getLogger(__name__)
LOG.setLevel(logging.INFO)
log_format = logging.Formatter('%(asctime)s - %(name)s - [%(levelname)s]: %(message)s')
file_handler = logging.handlers.TimedRotatingFileHandler("log.out", when='midnight', backupCount=7)
file_handler.setFormatter(log_format)
console_handler = logging.StreamHandler()
console_handler.setFormatter(log_format)
LOG.addHandler(file_handler)
LOG.addHandler(console_handler)


LOGIN_URL = "http://localhost:8082/login"
LOGIN_USERNAME = "user"
LOGIN_PASSWORD = "password"
CAR_ARRAY = [
    {   
        'year': "2017",
        'make': "Honda",
        'model': "Civic",
        'trim_levels': [
            "DX",
            "EX",
            "LX",
        ]
    }, {
        'year': "2016",
        'make': "Toyota",
        'model': "Corolla",
        'trim_levels': [
            "L",
            "LE",
            "SE",
        ]
    }, {
        'year': "2013",
        'make': "Ford",
        'model': "Explorer",
        'trim_levels': [
            "A",
            "B",
        ]
    },
]

def login():
    LOG.info("Logging in...")
    driver.get(LOGIN_URL)
    username_input = driver.find_element_by_css_selector("input[id=username]")
    password_input = driver.find_element_by_css_selector("input[id=password]")

    username_input.send_keys(LOGIN_USERNAME)
    password_input.send_keys(LOGIN_PASSWORD)
    password_input.submit()
    time.sleep(3)

def add_cars():
    LOG.info("Adding cars...")
    driver.find_element_by_css_selector('a[href="/addcar"]').click()

    for car in CAR_ARRAY:
        driver.find_element_by_css_selector('a[href="/addcar"]').click()

        driver.find_element_by_css_selector("input[id=year]").send_keys(car['year'])
        driver.find_element_by_css_selector("input[id=make]").send_keys(car['make'])
        driver.find_element_by_css_selector("input[id=model]").send_keys(car['model'])
        driver.find_element_by_css_selector("textarea[id=trimlevels]").send_keys("\n".join(car['trim_levels']))

        # Submit
        driver.find_element_by_css_selector("button[id=addcarbutton]").click()

        # Press RETURN to close alert then wait a little while it redirects
        ActionChains(driver).send_keys(Keys.RETURN).perform()
        time.sleep(3)

        LOG.info("Car added.")

def check_year_make_model(car, td_year, td_make, td_model):
    if car['year'] == td_year.text and car['make'] == td_make.text and car['model'] == td_model.text:
        return True

    return False

def check_trim_levels(car, td_trim_levels):
    for trim in car['trim_levels']:
        regex = r"\b" + re.escape(trim) + r"\b"
        if not re.search(regex, td_trim_levels.text):
            return False

    return True

def check_cars():
    LOG.info("Checking cars...")
    for car in CAR_ARRAY:
        found_car = False

        for row in driver.find_elements_by_css_selector("tr"):
            # Skip header row
            if row.find_elements_by_css_selector("th"):
                continue

            td_elements = row.find_elements_by_css_selector("td")
            if check_year_make_model(car, td_elements[0], td_elements[1], td_elements[2]) and check_trim_levels(car, td_elements[3]):
                found_car = True
                break

        car_string = car['year'] + " " + car['make'] + " " + car['model'] + " With trim levels: " + ",".join(car['trim_levels'])

        if found_car:
            LOG.info("Found car! " + "(" + car_string + ")")
        else:
            LOG.info("CAR NOT FOUND!!! " + "(" + car_string + ")")

if __name__ == "__main__":
    driver = webdriver.PhantomJS()
    driver.set_window_size(1440, 900)

    login()
    add_cars()
    check_cars()

    driver.quit()
    LOG.info("Done!")

