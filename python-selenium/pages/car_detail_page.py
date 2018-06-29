from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from .locators import CarDetailPageLocators
from .page import BasePage

class CarDetailPage(BasePage):
    def __init__(self, driver):
        super().__init__(driver)

        # Wait for trim levels to be present, others should then also be present
        wait = WebDriverWait(self.driver, 10)
        self.trim_levels = wait.until(expected_conditions.presence_of_element_located(CarDetailPageLocators.TRIM_LEVELS))

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
