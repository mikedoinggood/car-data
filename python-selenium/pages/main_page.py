from enum import Enum
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait

from car_data_utility import get_car_string
from logging_utility import get_logger
from .locators import MainPageLocators
from .page import BasePage

""" Logging setup """
LOG = get_logger(__name__)

class MainPage(BasePage):
    class CarSortBy(Enum):
        MAKE = 1
        NEWEST = 2
        OLDEST = 3

    def click_add_car_link(self):
        add_car_link = self.driver.find_element(*MainPageLocators.ADD_CARS_LINK)
        add_car_link.click()

    def get_car_rows(self):
        wait = WebDriverWait(self.driver, 5)
        car_rows = wait.until(expected_conditions.presence_of_all_elements_located(MainPageLocators.CAR_ROWS))

        return car_rows

    def navigate_to_car_detail_page(self, car):
        car_row = self.find_car_row(car)
        row_columns = car_row.find_elements(By.XPATH, ".//td")
        row_columns[0].find_element(By.TAG_NAME, "a").click()

    def find_car_row(self, car):
        LOG.info("Finding car row...")

        while True:
            car_rows = self.get_car_rows()

            for row in car_rows:
                row_car = self.car_row_to_car_dict(row)
                row_car_string = get_car_string(row_car)

                if row_car_string == car['car_string']:
                    LOG.info("Found car: {}".format(row_car_string))
                    return row

            clicked_next_page_link = self.click_next_page_link()

            if not clicked_next_page_link:
                break

        return None

    def find_multiple_car_rows(self, car_list):
        LOG.info("Finding multiple car rows...")
        found_count = 0
        found_car_rows = {}

        for car in car_list:
            found_car_rows[car['car_string']] = None

        while True:
            car_rows = self.get_car_rows()

            for row in car_rows:
                row_car = self.car_row_to_car_dict(row)
                row_car_string = get_car_string(row_car)

                if row_car_string in found_car_rows:
                    LOG.info("Found car: {}".format(row_car_string))
                    found_car_rows[row_car_string] = row
                    found_count += 1

                    if found_count == len(car_list):
                        return found_car_rows


            clicked_next_page_link = self.click_next_page_link()

            if not clicked_next_page_link:
                break

        return found_car_rows

    def click_next_page_link(self):
        try:
            next_page_link = self.driver.find_element(By.LINK_TEXT, "[Next]")
            next_page_link.click()
            LOG.info("Clicked next page link")
        except NoSuchElementException as e:
            return False

        return True

    def select_car_sort_by(self, car_sort_by):
        select = Select(self.driver.find_element(*MainPageLocators.SORT_BY_DROP_DOWN))

        if car_sort_by == self.CarSortBy.MAKE:
            select.select_by_value("make")
        elif car_sort_by == self.CarSortBy.NEWEST:
            select.select_by_value("newest")
        elif car_sort_by == self.CarSortBy.OLDEST:
            select.select_by_value("oldest")

    def car_row_to_car_dict(self, row):
        td_elements = row.find_elements(By.XPATH, ".//td")

        return {
            'year': td_elements[0].text,
            'make': td_elements[1].text,
            'model': td_elements[2].text,
            'trim_levels': td_elements[3].text.split(", ")
        }
