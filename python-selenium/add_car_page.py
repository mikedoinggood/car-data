from locators import AddCarPageLocators
from page import BasePage

class AddCarPage(BasePage):
    def add_car(self, car):
        year_input = self.driver.find_element(*AddCarPageLocators.YEAR_INPUT)
        make_input = self.driver.find_element(*AddCarPageLocators.MAKE_INPUT)
        model_input = self.driver.find_element(*AddCarPageLocators.MODEL_INPUT)
        trim_levels_text_area = self.driver.find_element(*AddCarPageLocators.TRIM_LEVELS_TEXT_AREA)
        add_car_button = self.driver.find_element(*AddCarPageLocators.ADD_CAR_BUTTON)

        year_input.send_keys(car['year'])
        make_input.send_keys(car['make'])
        model_input.send_keys(car['model'])
        trim_levels_text_area.send_keys("\n".join(car['trim_levels']))
        add_car_button.click()
