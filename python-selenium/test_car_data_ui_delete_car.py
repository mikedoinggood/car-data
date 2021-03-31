import unittest
import time

from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from car_data_utility import generate_random_trim_level, get_car_string
from logging_utility import get_logger
from pages.add_car_page import AddCarPage
from pages.car_detail_page import CarDetailPage
from pages.login_page import LoginPage
from pages.main_page import MainPage
from web_driver_utility import WebDriverUtility

""" Logging setup """
LOG = get_logger(__name__)

class DeleteCar(unittest.TestCase):
    def setUp(self):
        self.car = {
            'year': "2021",
            'make': "Honda",
            'model': "Civic",
            'trim_levels': [generate_random_trim_level() for _ in range(3)],
        }

        self.car['car_string'] = get_car_string(self.car)

        web_driver_utility = WebDriverUtility()
        self.driver = web_driver_utility.get_new_web_driver()
        self.driver.get(web_driver_utility.get_home_page_url())

        self.main_page = MainPage(self.driver)

    def test_delete_car(self):
        self.login()
        self.add_car()
        self.main_page.navigate_to_car_detail_page(self.car)
        LOG.info("Navigated to car detail page of %s", self.car['car_string'])
        self.delete_car()

        self.assertIsNone(self.main_page.find_car_row(self.car))

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

        LOG.info("Added car: %s", self.car['car_string'])

    def delete_car(self):
        car_detail_page = CarDetailPage(self.driver)
        car_detail_page.click_delete_car_button()

        # Accept alerts
        wait = WebDriverWait(self.driver, 5)
        wait.until(expected_conditions.alert_is_present())
        self.driver.switch_to.alert.accept()
        wait.until(expected_conditions.alert_is_present())
        self.driver.switch_to.alert.accept()

        LOG.info("Deleted car: %s", self.car['car_string'])

if __name__ == "__main__":
    unittest.main()
