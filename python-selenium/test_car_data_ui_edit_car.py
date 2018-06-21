import unittest

from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys

from add_car_page import AddCarPage
from car_data_utility import generate_random_trim_level, get_car_string
from car_detail_page import CarDetailPage
from edit_car_page import EditCarPage
from logging_utility import get_logger
from login_page import LoginPage
from main_page import MainPage

""" Logging setup """
LOG = get_logger(__name__)

class EditCar(unittest.TestCase):
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

        self.driver = webdriver.PhantomJS()
        self.driver.set_window_size(1440, 900)
        self.driver.get("http://localhost:8082")

        self.main_page = MainPage(self.driver)
        self.login()
        self.add_car()

    def test_edit_car(self):
        self.navigate_to_edit_car_page()

        # Edit car
        self.car['year'] = "2012"
        self.car['make'] = "Ford"
        self.car['model'] = "Focus"

        random_trim_levels = []
        for _ in range(3):
            random_trim_levels.append(generate_random_trim_level())

        self.car['trim_levels'] = random_trim_levels

        edit_car_page = EditCarPage(self.driver)
        edit_car_page.edit_year(self.car['year'])
        edit_car_page.edit_make(self.car['make'])
        edit_car_page.edit_model(self.car['model'])
        self.edit_trim_levels(edit_car_page)
        edit_car_page.click_submit_car_button()

        # Press RETURN to close alert
        ActionChains(self.driver).send_keys(Keys.RETURN).perform()
        LOG.info("Edited car to: %s", get_car_string(self.car))

        self.assertIsNotNone(self.main_page.find_car_row(self.car))

    def test_delete_trim_level(self):
        self.navigate_to_edit_car_page()

        edit_car_page = EditCarPage(self.driver)
        self.delete_trim_level(edit_car_page)
        edit_car_page.click_submit_car_button()

        # Press RETURN to close alert
        ActionChains(self.driver).send_keys(Keys.RETURN).perform()
        LOG.info("Edited car to: %s", get_car_string(self.car))

        self.assertIsNotNone(self.main_page.find_car_row(self.car))

    def test_add_trim_level(self):
        self.navigate_to_edit_car_page()

        edit_car_page = EditCarPage(self.driver)
        self.add_trim_level(edit_car_page)
        edit_car_page.click_submit_car_button()

        # Press RETURN to close alert
        ActionChains(self.driver).send_keys(Keys.RETURN).perform()
        LOG.info("Edited car to: %s", get_car_string(self.car))

        self.assertIsNotNone(self.main_page.find_car_row(self.car))

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

    def navigate_to_edit_car_page(self):
        self.main_page.navigate_to_car_detail_page(self.car)
        car_detail_page = CarDetailPage(self.driver)
        car_detail_page.click_edit_car_link()

    def add_trim_level(self, edit_car_page):
        trim_level_to_add = generate_random_trim_level()
        LOG.info("Trim level to add: %s", trim_level_to_add)
        self.car['trim_levels'].append(trim_level_to_add)

        edit_car_page.click_add_trim_level_button()
        trim_level_inputs = edit_car_page.trim_levels.find_elements(By.XPATH, ".//input")
        trim_level_inputs[-1].send_keys(trim_level_to_add)

    def edit_trim_levels(self, edit_car_page):
        trim_level_inputs = edit_car_page.trim_levels.find_elements(By.XPATH, ".//input")

        for i in range(3):
            trim_level_inputs[i].clear()
            trim_level_inputs[i].send_keys(self.car['trim_levels'][i])

    def delete_trim_level(self, edit_car_page):
        trim_level_to_delete = self.car['trim_levels'].pop()
        LOG.info("Trim level to delete: %s", trim_level_to_delete)

        trim_level_input_groups = edit_car_page.trim_levels.find_elements(By.XPATH, ".//div[contains(@class, 'input-group')]")
        for input_group in trim_level_input_groups:
            existing_trim_level = input_group.find_element(By.XPATH, ".//input")

            if trim_level_to_delete == existing_trim_level.get_attribute("value"):
                delete_button = input_group.find_element(By.XPATH, ".//button")
                delete_button.click()
                break

if __name__ == "__main__":
    unittest.main()
