from locators import MainPageLocators
from page import BasePage
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

import re

class MainPage(BasePage):
    def click_add_car_link(self):
        add_car_link = self.driver.find_element(*MainPageLocators.ADD_CARS_LINK)
        add_car_link.click()

    def get_car_rows(self):
        wait = WebDriverWait(self.driver, 10)
        car_rows = wait.until(expected_conditions.presence_of_all_elements_located(MainPageLocators.CAR_ROWS))

        return car_rows

    def check_year_make_model(self, car, td_elements):
        year = td_elements[0].text
        make = td_elements[1].text
        model = td_elements[2].text

        if car['year'] == year and car['make'] == make and car['model'] == model:
            return True

    def check_trim_levels(self, car, td_trim_levels):
        for trim in car['trim_levels']:
            regex = r"\b" + re.escape(trim) + r"\b"
            if not re.search(regex, td_trim_levels.text):
                return False

        return True
