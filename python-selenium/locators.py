from selenium.webdriver.common.by import By

class MainPageLocators(object):
    ADD_CARS_LINK = (By.LINK_TEXT, "Add Car")
    CAR_ROWS = (By.XPATH, "//tbody[@id='tablebody']/tr")

class LoginPageLocators(object):
    USERNAME_INPUT = (By.ID, "username")
    PASSWORD_INPUT = (By.ID, "password")
    SIGN_IN_BUTTON = (By.XPATH, "//form[@id='loginform']//input[@type='submit']")

class AddCarPageLocators(object):
    YEAR_INPUT = (By.ID, "year")
    MAKE_INPUT = (By.ID, "make")
    MODEL_INPUT = (By.ID, "model")
    TRIM_LEVELS_TEXT_AREA = (By.ID, "trimlevels")
    ADD_CAR_BUTTON = (By.ID, "addcarbutton")