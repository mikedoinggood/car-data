from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

import logging
import logging.handlers
import random
import re
import string
import time


# Logging setup
LOG = logging.getLogger(__name__)
LOG.setLevel(logging.INFO)
log_format = logging.Formatter('%(asctime)s - %(name)s - [%(levelname)s]: %(message)s')
file_handler = logging.handlers.TimedRotatingFileHandler("java-car-data-ui-test.log", when='midnight', backupCount=7)
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
    }, {
        'year': "2016",
        'make': "Toyota",
        'model': "Corolla",
    }, {
        'year': "2013",
        'make': "Ford",
        'model': "Explorer",
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

def add_cars():
    LOG.info("Adding cars...")

    for car in CAR_ARRAY:
        wait = WebDriverWait(driver, 5)
        wait.until(expected_conditions.presence_of_element_located((By.CSS_SELECTOR, 'a[href="/addcar"]')))

        driver.find_element_by_css_selector('a[href="/addcar"]').click()

        driver.find_element_by_css_selector("input[id=year]").send_keys(car['year'])
        driver.find_element_by_css_selector("input[id=make]").send_keys(car['make'])
        driver.find_element_by_css_selector("input[id=model]").send_keys(car['model'])
        driver.find_element_by_css_selector("textarea[id=trimlevels]").send_keys("\n".join(car['trim_levels']))

        # Submit
        driver.find_element_by_css_selector("button[id=addcarbutton]").click()

        # Press RETURN to close alert
        ActionChains(driver).send_keys(Keys.RETURN).perform()

        LOG.info("Added car: %s", get_car_string(car))

def check_year_make_model(car, td_year, td_make, td_model):
    if car['year'] == td_year.text and car['make'] == td_make.text and car['model'] == td_model.text:
        return True

def check_trim_levels(car, td_trim_levels):
    for trim in car['trim_levels']:
        regex = r"\b" + re.escape(trim) + r"\b"
        if not re.search(regex, td_trim_levels.text):
            return False

    return True

def check_cars():
    LOG.info("Checking cars...")

    wait = WebDriverWait(driver, 5)
    wait.until(expected_conditions.presence_of_element_located((By.XPATH, '//tbody[@id="tablebody"]//tr')))

    for car in CAR_ARRAY:
        # Scan table of cars, set found flag to true if car is found
        for row in driver.find_elements_by_css_selector("tr"):
            # Skip header row
            if row.find_elements_by_css_selector("th"):
                continue

            td_elements = row.find_elements_by_css_selector("td")
            if check_year_make_model(car, td_elements[0], td_elements[1], td_elements[2]) and check_trim_levels(car, td_elements[3]):
                car['found'] = True
                LOG.info("Found car: %s", get_car_string(car))
                break

def get_car_string(car):
    return car['year'] + " " + car['make'] + " " + car['model'] + " With trim levels: " + ",".join(car['trim_levels'])

def generate_trim_levels():
    random_trim_levels = []

    for x in range(0, 3):
        random_trim = ''.join([random.choice(string.ascii_letters + string.digits) for n in range(0, 5)])
        random_trim_levels.append(random_trim)

    return random_trim_levels

if __name__ == "__main__":
    # Initialize a found flag for all cars to false
    # and generate random trim levels for each vehicle
    for car in CAR_ARRAY:
        car['found'] = False
        car['trim_levels'] = generate_trim_levels()

    driver = webdriver.PhantomJS()
    driver.set_window_size(1440, 900)

    try:
        login()
        add_cars()
        check_cars()

        for car in CAR_ARRAY:
            assert car['found'] == True, "Could not find car: %s" % get_car_string(car)
    except (TimeoutException, AssertionError) as e:
        LOG.exception(e)
    finally:
        LOG.info("Cleaning up...")
        if driver:
            driver.quit()
            LOG.info("Quit driver.")

        LOG.info("Done!")
