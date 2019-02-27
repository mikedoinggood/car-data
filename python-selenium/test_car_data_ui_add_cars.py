import unittest

from selenium.webdriver.common.keys import Keys

from car_data_utility import generate_random_trim_level, get_car_string
from logging_utility import get_logger
from pages.add_car_page import AddCarPage
from pages.login_page import LoginPage
from pages.main_page import MainPage
from web_driver_utility import WebDriverUtility

""" Logging setup """
LOG = get_logger(__name__)

class AddCars(unittest.TestCase):
    def setUp(self):
        self.car_list = [
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

        for car in self.car_list:
            car['trim_levels'] = [generate_random_trim_level() for _ in range(3)]

        for car in self.car_list:
            car['car_string'] = get_car_string(car)

        web_driver_utility = WebDriverUtility()
        self.driver = web_driver_utility.get_new_web_driver()
        self.driver.get(web_driver_utility.get_home_page_url())

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
        for car in self.car_list:
            self.main_page.click_add_car_link()
            add_car_page = AddCarPage(self.driver)
            add_car_page.add_car(car)

            LOG.info("Added car: %s", car['car_string'])

    def check_cars(self):
        found_car_rows = self.main_page.find_multiple_car_rows(self.car_list)

        for car in self.car_list:
            self.assertIsNotNone(found_car_rows.get(car['car_string']), msg="Could not find car: %s" % car['car_string'])

if __name__ == "__main__":
    unittest.main()
