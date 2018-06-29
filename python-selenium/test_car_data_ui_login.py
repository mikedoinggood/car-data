import unittest

from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By

from logging_utility import get_logger
from pages.add_car_page import AddCarPage
from pages.car_detail_page import CarDetailPage
from pages.edit_car_page import EditCarPage
from pages.locators import AddCarPageLocators
from pages.login_page import LoginPage
from pages.main_page import MainPage
from web_driver_utility import WebDriverUtility

""" Logging setup """
LOG = get_logger(__name__)

class EditCar(unittest.TestCase):
    def setUp(self):
        self.web_driver_utility = WebDriverUtility()
        self.driver = self.web_driver_utility.get_new_web_driver(WebDriverUtility.PHANTOMJS_DRIVER)
        self.main_page = None

    def test_login(self):
        self.login("user", "password")
        self.driver.find_element(By.LINK_TEXT, "Logged in as user")

    def test_invalid_username_login(self):
        self.login("abc", "password")
        self.assert_login_error_message()

    def test_invalid_password_login(self):
        self.login("user", "abc")
        self.assert_login_error_message()

    def test_logout(self):
        self.login("user", "password")
        self.logout()

    def test_add_car_page_login(self):
        self.driver.get(self.web_driver_utility.get_add_car_page())

        try:
            add_car_page = AddCarPage(self.driver)
        except TimeoutException:
            LOG.info("Could not load add car page.")

        self.login("user", "password")
        self.driver.get(self.web_driver_utility.get_add_car_page())
        add_car_page = AddCarPage(self.driver)
        LOG.info("Add car page loaded.")

    def test_car_detail_page_login(self):
        self.login_and_add_car()
        car_rows = self.main_page.get_car_rows()
        first_car_detail_link = car_rows[0].find_element(By.TAG_NAME, "a").get_attribute("href")

        self.driver.get(first_car_detail_link)
        car_detail_page = CarDetailPage(self.driver)
        LOG.info("Car detail page loaded.")

        self.logout()
        self.driver.get(first_car_detail_link)

        try:
            car_detail_page = CarDetailPage(self.driver)
        except TimeoutException:
            LOG.info("Could not load car detail page.")

    def test_edit_car_page_login(self):
        self.login_and_add_car()
        car_rows = self.main_page.get_car_rows()
        first_car_detail_link = car_rows[0].find_element(By.TAG_NAME, "a").get_attribute("href")

        self.driver.get(first_car_detail_link)
        car_detail_page = CarDetailPage(self.driver)
        LOG.info("Car detail page loaded.")

        first_car_edit_link = car_detail_page.get_edit_car_link().get_attribute("href")
        self.driver.get(first_car_edit_link)
        edit_car_page = EditCarPage(self.driver)
        LOG.info("Edit car page loaded.")

        self.logout()
        self.driver.get(first_car_edit_link)

        try:
            edit_car_page = EditCarPage(self.driver)
        except TimeoutException:
            LOG.info("Could not load edit car page.")

    def tearDown(self):
        LOG.info("Cleaning up...")
        self.driver.quit()
        LOG.info("Quit driver.")
        LOG.info("Done!")

    """ Utility Methods """
    def login(self, username, password):
        self.driver.get(self.web_driver_utility.get_home_page())
        LOG.info("Logging in...")
        login_page = LoginPage(self.driver)
        login_page.login(username, password)

    def login_and_add_car(self):
        self.driver.get(self.web_driver_utility.get_home_page())
        self.login("user", "password")
        self.main_page = MainPage(self.driver)
        self.main_page.click_add_car_link()
        add_car_page = AddCarPage(self.driver)

        # Adding car just to make sure at least one car is present
        car = {
            'year': "2017",
            'make': "Honda",
            'model': "Accord",
            'trim_levels': [],
        }

        add_car_page.add_car(car)
        LOG.info("Added car.")

    def logout(self):
        self.driver.find_element(By.LINK_TEXT, "Logged in as user").click()
        self.driver.find_element(By.ID, "logoutlink").click()
        self.driver.find_element(By.ID, "loginform")
        LOG.info("Logged out.")

    def assert_login_error_message(self):
        error_message_element = self.driver.find_element(By.CLASS_NAME, "text-danger")
        self.assertEqual(error_message_element.text, "Invalid username or password.")

