from selenium.webdriver.common.by import By

class MainPageLocators():
    ADD_CARS_LINK = (By.LINK_TEXT, "Add Car")
    CAR_ROWS = (By.XPATH, "//div[@class='maincontent']//table[@id='carstable']/tbody/tr")

class LoginPageLocators():
    USERNAME_INPUT = (By.ID, "username")
    PASSWORD_INPUT = (By.ID, "password")
    SIGN_IN_BUTTON = (By.XPATH, "//form[@id='loginform']//input[@type='submit']")

class AddCarPageLocators():
    YEAR_INPUT = (By.ID, "year")
    MAKE_INPUT = (By.ID, "make")
    MODEL_INPUT = (By.ID, "model")
    TRIM_LEVEL_INPUTS = (By.CLASS_NAME, "trimlevel")
    ADD_TRIM_LEVEL_BUTTON = (By.ID, "addtrimlevelbutton")
    SUBMIT_CAR_BUTTON = (By.ID, "submitcarbutton")

class CarDetailPageLocators():
    CAR_DETAILS = (By.ID, "cardetails")
    EDIT_CAR_LINK = (By.ID, "editlink")
    DELETE_CAR_BUTTON = (By.ID, "deletecarbutton")
    YEAR = (By.ID, "year")
    MAKE = (By.ID, "make")
    MODEL = (By.ID, "model")
    TRIM_LEVELS = (By.ID, "trimlevels")

class EditCarPageLocators():
    YEAR_INPUT = (By.ID, "year")
    MAKE_INPUT = (By.ID, "make")
    MODEL_INPUT = (By.ID, "model")
    TRIM_LEVELS = (By.ID, "trimlevels")
    ADD_TRIM_LEVEL_BUTTON = (By.ID, "addtrimlevelbutton")
    SUBMIT_CAR_BUTTON = (By.ID, "submitcarbutton")

class ChartsPageLocators():
    CHARTS_DIV = (By.ID, "charts")