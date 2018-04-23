from locators import AddCarPageLocators
from page import BasePage

class AddCarPage(BasePage):
    def add_car(self, car):
        year_input = self.driver.find_element(*AddCarPageLocators.YEAR_INPUT)
        make_input = self.driver.find_element(*AddCarPageLocators.MAKE_INPUT)
        model_input = self.driver.find_element(*AddCarPageLocators.MODEL_INPUT)
        add_trim_level_button = self.driver.find_element(*AddCarPageLocators.ADD_TRIM_LEVEL_BUTTON)
        submit_car_button = self.driver.find_element(*AddCarPageLocators.SUBMIT_CAR_BUTTON)

        year_input.send_keys(car['year'])
        make_input.send_keys(car['make'])
        model_input.send_keys(car['model'])

        # Trim Levels
        for i in range(0, len(car['trim_levels'])):
            add_trim_level_button.click()

        trim_levels_inputs = self.driver.find_elements(*AddCarPageLocators.TRIM_LEVEL_INPUTS)

        for i in range(0, len(car['trim_levels'])):
            trim_level = car['trim_levels'][i]
            trim_levels_inputs[i].send_keys(trim_level)

        submit_car_button.click()