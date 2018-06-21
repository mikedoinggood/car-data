import re

from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from locators import MainPageLocators
from page import BasePage

class MainPage(BasePage):
    def click_add_car_link(self):
        add_car_link = self.driver.find_element(*MainPageLocators.ADD_CARS_LINK)
        add_car_link.click()

    def get_car_rows(self):
        wait = WebDriverWait(self.driver, 10)
        car_rows = wait.until(expected_conditions.presence_of_all_elements_located(MainPageLocators.CAR_ROWS))

        return car_rows

    def navigate_to_car_detail_page(self, car):
        car_row = self.find_car_row(car)
        row_columns = car_row.find_elements(By.XPATH, ".//td")
        row_columns[0].find_element(By.TAG_NAME, "a").click()

    def find_car_row(self, car):
        car_rows = self.get_car_rows()

        for row in car_rows:
            row_columns = row.find_elements(By.XPATH, ".//td")

            if self.check_year_make_model(car, row_columns) and self.check_trim_levels(car, row_columns[3]):
                return row

        raise NoSuchElementException

    def check_year_make_model(self, car, td_elements):
        year = td_elements[0].text
        make = td_elements[1].text
        model = td_elements[2].text

        if car['year'] == year and car['make'] == make and car['model'] == model:
            return True

    def check_trim_levels(self, car, td_trim_levels):
        td_trim_levels_list = td_trim_levels.text.split(", ")

        if sorted(td_trim_levels_list) == sorted(car['trim_levels']):
            return True
