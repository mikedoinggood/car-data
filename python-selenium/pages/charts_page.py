from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from .locators import ChartsPageLocators
from .page import BasePage

class ChartsPage(BasePage):
    def __init__(self, web_driver):
        super().__init__(web_driver)

        self.wait = WebDriverWait(self.driver, 5)
        self.charts_div = self.wait.until(expected_conditions.visibility_of_element_located(ChartsPageLocators.CHARTS_DIV))
