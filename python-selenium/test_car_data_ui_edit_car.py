import unittest

from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys

from car_data_utility import generate_random_trim_level, get_car_string
from logging_utility import get_logger
from pages.add_car_page import AddCarPage
from pages.car_detail_page import CarDetailPage
from pages.edit_car_page import EditCarPage
from pages.login_page import LoginPage
from pages.main_page import MainPage
from web_driver_utility import WebDriverUtility

""" Logging setup """
LOG = get_logger(__name__)

class EditCar(unittest.TestCase):
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
        self.car['car_string'] = get_car_string(self.car)

        edit_car_page = EditCarPage(self.driver)
        edit_car_page.edit_year(self.car['year'])
        edit_car_page.edit_make(self.car['make'])
        edit_car_page.edit_model(self.car['model'])
        self.edit_trim_levels(edit_car_page)
        edit_car_page.click_submit_car_button()

        LOG.info("Edited car to: %s", self.car['car_string'])

        self.assertIsNotNone(self.main_page.find_car_row(self.car))

    def test_delete_trim_level(self):
        self.navigate_to_edit_car_page()

        edit_car_page = EditCarPage(self.driver)
        self.delete_trim_level(edit_car_page)
        edit_car_page.click_submit_car_button()

        LOG.info("Edited car to: %s", self.car['car_string'])

        self.assertIsNotNone(self.main_page.find_car_row(self.car))

    def test_add_trim_level(self):
        self.navigate_to_edit_car_page()

        edit_car_page = EditCarPage(self.driver)
        self.add_trim_level(edit_car_page)
        edit_car_page.click_submit_car_button()

        LOG.info("Edited car to: %s", self.car['car_string'])

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

        LOG.info("Added car: %s", self.car['car_string'])

    def navigate_to_edit_car_page(self):
        self.main_page.navigate_to_car_detail_page(self.car)
        car_detail_page = CarDetailPage(self.driver)
        car_detail_page.click_edit_car_link()

    def add_trim_level(self, edit_car_page):
        trim_level_to_add = generate_random_trim_level()
        LOG.info("Trim level to add: %s", trim_level_to_add)
        self.car['trim_levels'].append(trim_level_to_add)
        self.car['car_string'] = get_car_string(self.car)

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
        self.car['car_string'] = get_car_string(self.car)

        trim_level_input_groups = edit_car_page.trim_levels.find_elements(By.XPATH, ".//div[contains(@class, 'input-group')]")
        for input_group in trim_level_input_groups:
            existing_trim_level = input_group.find_element(By.XPATH, ".//input")

            if trim_level_to_delete == existing_trim_level.get_attribute("value"):
                delete_button = input_group.find_element(By.XPATH, ".//button")
                delete_button.click()
                break

if __name__ == "__main__":
    unittest.main()
