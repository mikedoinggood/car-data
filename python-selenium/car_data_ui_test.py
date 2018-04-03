from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from add_car_page import AddCarPage
from locators import MainPageLocators
from login_page import LoginPage
from main_page import MainPage

import logging
import logging.handlers
import random
import string
import unittest

""" Logging setup """
LOG = logging.getLogger(__name__)
LOG.setLevel(logging.INFO)
log_format = logging.Formatter('%(asctime)s - %(name)s - [%(levelname)s]: %(message)s')
file_handler = logging.handlers.TimedRotatingFileHandler("car-data-ui-test.log", when='midnight', backupCount=7)
file_handler.setFormatter(log_format)
console_handler = logging.StreamHandler()
console_handler.setFormatter(log_format)
LOG.addHandler(file_handler)
LOG.addHandler(console_handler)

class AddCars(unittest.TestCase):
    def setUp(self):
        self.car_array = [
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

        for car in self.car_array:
            car['found'] = False
            car['trim_levels'] = self.generate_trim_levels()

        self.driver = webdriver.PhantomJS()
        self.driver.set_window_size(1440, 900)
        self.driver.get("http://localhost:8082")

        self.main_page = MainPage(self.driver)

    def test_add_cars(self):
        self.login()
        self.add_cars()
        self.check_cars()

    def tearDown(self):
        LOG.info("Cleaning up...")
        self.driver.quit()
        LOG.info("Quit driver.")
        LOG.info("Done!")

    """ Utility Methods """
    def login(self):
        LOG.info("Logging in...")
        login_page = LoginPage(self.driver)
        login_page.login("user", "password")

    def add_cars(self):
        for car in self.car_array:
            self.main_page.click_add_car_link()
            add_car_page = AddCarPage(self.driver)
            add_car_page.add_car(car)

            # Press RETURN to close alert
            ActionChains(self.driver).send_keys(Keys.RETURN).perform()

            LOG.info("Added car: %s", self.get_car_string(car))

    def check_cars(self):
        LOG.info("Checking cars...")
        car_rows = self.main_page.get_car_rows()

        for row in car_rows:
            td_elements = row.find_elements_by_css_selector("td")

            for car in self.car_array:
                if self.main_page.check_year_make_model(car, td_elements) and self.main_page.check_trim_levels(car, td_elements[3]):
                    car['found'] = True
                    LOG.info("Found car: %s", self.get_car_string(car))
                    break

        for car in self.car_array:
            assert car['found'] == True, "Could not find car: %s" % self.get_car_string(car)

    def get_car_string(self, car):
        return car['year'] + " " + car['make'] + " " + car['model'] + " With trim levels: " + ",".join(car['trim_levels'])

    def generate_trim_levels(self):
        random_trim_levels = []

        for x in range(0, 3):
            random_trim = ''.join([random.choice(string.ascii_letters + string.digits) for n in range(0, 5)])
            random_trim_levels.append(random_trim)

        return random_trim_levels

if __name__ == "__main__":
    unittest.main()
