import unittest

from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys

from add_car_page import AddCarPage
from car_data_utility import generate_random_trim_level, get_car_string
from car_detail_page import CarDetailPage
from logging_utility import get_logger
from login_page import LoginPage
from main_page import MainPage
from web_driver_utility import WebDriverUtility

""" Logging setup """
LOG = get_logger(__name__)

class CarDetail(unittest.TestCase):
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

        web_driver_utility = WebDriverUtility()
        self.driver = web_driver_utility.get_new_web_driver(WebDriverUtility.PHANTOMJS_DRIVER)
        self.driver.get(web_driver_utility.get_home_page())

        self.main_page = MainPage(self.driver)

    def test_car_detail(self):
        self.login()
        self.add_car()
        self.main_page.navigate_to_car_detail_page(self.car)
        LOG.info("Navigated to car detail page of %s", get_car_string(self.car))
        self.check_car_detail_page()

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

        # Press RETURN to close alert
        ActionChains(self.driver).send_keys(Keys.RETURN).perform()

        LOG.info("Added car: %s", get_car_string(self.car))

    def check_car_detail_page(self):
        LOG.info("Checking car detail page...")

        car_detail_page = CarDetailPage(self.driver)
        self.assertEqual(car_detail_page.year.text, self.car['year'])
        self.assertEqual(car_detail_page.make.text, self.car['make'])
        self.assertEqual(car_detail_page.model.text, self.car['model'])

        detail_page_trim_levels = car_detail_page.trim_levels
        detail_page_trim_levels_list = detail_page_trim_levels.text.split("\n")
        self.assertEqual(sorted(detail_page_trim_levels_list), sorted(self.car['trim_levels']))

if __name__ == "__main__":
    unittest.main()
