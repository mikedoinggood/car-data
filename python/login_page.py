from locators import LoginPageLocators
from page import BasePage

class LoginPage(BasePage):
    def login(self, username, password):
        username_input = self.driver.find_element(*LoginPageLocators.USERNAME_INPUT)
        password_input = self.driver.find_element(*LoginPageLocators.PASSWORD_INPUT)
        sign_in_button = self.driver.find_element(*LoginPageLocators.SIGN_IN_BUTTON)

        username_input.send_keys("user")
        password_input.send_keys("password")
        sign_in_button.click()
