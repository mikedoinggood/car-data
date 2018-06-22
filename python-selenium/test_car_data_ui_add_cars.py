import unittest

from selenium.webdriver.common.action_chains import ActionChains
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
            car['found'] = False
            car['trim_levels'] = [generate_random_trim_level() for _ in range(3)]

        web_driver_utility = WebDriverUtility()
        self.driver = web_driver_utility.get_new_web_driver(WebDriverUtility.PHANTOMJS_DRIVER)
        self.driver.get(web_driver_utility.get_home_page())

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

            # Press RETURN to close alert
            ActionChains(self.driver).send_keys(Keys.RETURN).perform()

            LOG.info("Added car: %s", get_car_string(car))

    def check_cars(self):
        LOG.info("Checking cars...")
        car_rows = self.main_page.get_car_rows()

        for row in car_rows:
            td_elements = row.find_elements_by_css_selector("td")

            for car in self.car_list:
                if self.main_page.check_year_make_model(car, td_elements) and self.main_page.check_trim_levels(car, td_elements[3]):
                    car['found'] = True
                    LOG.info("Found car: %s", get_car_string(car))
                    break

        for car in self.car_list:
            self.assertEqual(car['found'], True, msg="Could not find car: %s" % get_car_string(car))

if __name__ == "__main__":
    unittest.main()
