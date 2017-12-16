from locators import MainPageLocators
from page import BasePage

class MainPage(BasePage):
    def click_add_car_link(self):
        add_car_link = self.driver.find_element(*MainPageLocators.ADD_CARS_LINK)
        add_car_link.click()
        