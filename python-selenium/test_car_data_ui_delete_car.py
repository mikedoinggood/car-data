import unittest
import time

from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException

from add_car_page import AddCarPage
from car_data_utility import generate_random_trim_level, get_car_string
from car_detail_page import CarDetailPage
from logging_utility import get_logger
from login_page import LoginPage
from main_page import MainPage
from web_driver_utility import WebDriverUtility

""" Logging setup """
LOG = get_logger(__name__)

class DeleteCar(unittest.TestCase):
    def setUp(self):
        self.car = {
            'year': "2017",
            'make': "Honda",
            'model': "Civic",
        }
        self.car['found'] = False

        random_trim_levels = []
        for _ in range(3):
            random_trim_levels.append(generate_random_trim_level())

        self.car['trim_levels'] = random_trim_levels

        # Using firefox instead of phantomjs to handle confirmation pop ups
        web_driver_utility = WebDriverUtility()
        self.driver = web_driver_utility.get_new_web_driver(WebDriverUtility.FIREFOX_DRIVER)
        self.driver.get(web_driver_utility.get_home_page())

        self.main_page = MainPage(self.driver)

    def test_delete_car(self):
        self.login()
        self.add_car()
        self.main_page.navigate_to_car_detail_page(self.car)
        LOG.info("Navigated to car detail page of %s", get_car_string(self.car))
        self.delete_car()

        no_such_element_assertion_error = False
        try:
            self.main_page.find_car_row(self.car)
        except NoSuchElementException:
            no_such_element_assertion_error = True

        self.assertEqual(no_such_element_assertion_error, True)

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

    def add_car(self):
        self.main_page.click_add_car_link()
        add_car_page = AddCarPage(self.driver)
        add_car_page.add_car(self.car)

        # Accept alert
        self.driver.switch_to.alert.accept()

        LOG.info("Added car: %s", get_car_string(self.car))

    def delete_car(self):
        car_detail_page = CarDetailPage(self.driver)
        car_detail_page.click_delete_car_button()

        # Accept alerts
        self.driver.switch_to.alert.accept()
        time.sleep(1)
        self.driver.switch_to.alert.accept()

        LOG.info("Deleted car: %s", get_car_string(self.car))

if __name__ == "__main__":
    unittest.main()
