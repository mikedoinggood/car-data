from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from .locators import EditCarPageLocators, MainPageLocators
from .page import BasePage

class EditCarPage(BasePage):
    def __init__(self, driver):
        super().__init__(driver)

        # Wait for trim levels to be present, others should then also be present
        self.wait = WebDriverWait(self.driver, 10)
        self.trim_levels = self.wait.until(expected_conditions.visibility_of_element_located(EditCarPageLocators.TRIM_LEVELS))

        self.year_input = self.driver.find_element(*EditCarPageLocators.YEAR_INPUT)
        self.make_input = self.driver.find_element(*EditCarPageLocators.MAKE_INPUT)
        self.model_input = self.driver.find_element(*EditCarPageLocators.MODEL_INPUT)

    def edit_year(self, year):
        self.year_input.clear()
        self.year_input.send_keys(year)

    def edit_make(self, make):
        self.make_input.clear()
        self.make_input.send_keys(make)

    def edit_model(self, model):
        self.model_input.clear()
        self.model_input.send_keys(model)

    def click_add_trim_level_button(self):
        add_trim_level_button = self.driver.find_element(*EditCarPageLocators.ADD_TRIM_LEVEL_BUTTON)
        add_trim_level_button.click()

    def click_submit_car_button(self):
        submit_car_button = self.driver.find_element(*EditCarPageLocators.SUBMIT_CAR_BUTTON)
        submit_car_button.click()

        # Accept alert
        self.wait.until(expected_conditions.alert_is_present())
        self.driver.switch_to.alert.accept()

        # Wait for javascript redirect
        self.wait.until(expected_conditions.presence_of_element_located(MainPageLocators.CAR_ROWS))