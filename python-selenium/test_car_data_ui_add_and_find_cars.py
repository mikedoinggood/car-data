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

class AddAndFindCars(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.car_list = [
            {
                'year': "2021",
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

        for car in cls.car_list:
            car['trim_levels'] = [generate_random_trim_level() for _ in range(3)]

        for car in cls.car_list:
            car['car_string'] = get_car_string(car)

        web_driver_utility = WebDriverUtility()
        cls.driver = web_driver_utility.get_new_web_driver()
        cls.driver.get(web_driver_utility.get_home_page_url())

        cls.main_page = MainPage(cls.driver)
        cls.login()
        cls.add_cars()

    def test_add_and_find_cars_by_make(self):
        self.check_cars(MainPage.CarSortBy.MAKE)

    def test_add_and_find_cars_by_newest(self):
        self.check_cars(MainPage.CarSortBy.NEWEST)

    def test_add_and_find_cars_by_oldest(self):      
        self.check_cars(MainPage.CarSortBy.OLDEST)

    @classmethod
    def tearDownClass(cls):
        LOG.info("Cleaning up...")
        cls.driver.quit()
        LOG.info("Quit driver.")
        LOG.info("Done!")

    """ Utility Methods """
    @classmethod
    def login(cls):
        LOG.info("Logging in...")
        login_page = LoginPage(cls.driver)
        login_page.login("user", "password")

    @classmethod
    def add_cars(cls):
        for car in cls.car_list:
            cls.main_page.click_add_car_link()
            add_car_page = AddCarPage(cls.driver)
            add_car_page.add_car(car)

            LOG.info("Added car: %s", car['car_string'])

    def check_cars(self, car_sort_by):
        self.main_page.select_car_sort_by(car_sort_by)
        print("Using car sort by value of: " + str(car_sort_by))

        found_car_rows = self.main_page.find_multiple_car_rows(self.car_list)

        for car in self.car_list:
            self.assertIsNotNone(found_car_rows.get(car['car_string']), msg="Could not find car: %s" % car['car_string'])

if __name__ == "__main__":
    unittest.main()
