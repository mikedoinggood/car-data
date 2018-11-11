from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from .locators import CarDetailPageLocators
from .page import BasePage

class CarDetailPage(BasePage):
    def __init__(self, web_driver):
        super().__init__(web_driver)

        wait = WebDriverWait(self.driver, 5)
        wait.until(expected_conditions.presence_of_element_located(CarDetailPageLocators.CAR_DETAILS))

        self.trim_levels = self.driver.find_element(*CarDetailPageLocators.TRIM_LEVELS)
        self.year = self.driver.find_element(*CarDetailPageLocators.YEAR)
        self.make = self.driver.find_element(*CarDetailPageLocators.MAKE)
        self.model = self.driver.find_element(*CarDetailPageLocators.MODEL)
        self.edit_car_link = self.driver.find_element(*CarDetailPageLocators.EDIT_CAR_LINK)
        self.delete_car_button = self.driver.find_element(*CarDetailPageLocators.DELETE_CAR_BUTTON)

    def get_edit_car_link(self):
        return self.edit_car_link

    def click_edit_car_link(self):
        self.edit_car_link.click()

    def click_delete_car_button(self):
        self.delete_car_button.click()
