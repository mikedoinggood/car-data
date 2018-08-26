import os

from selenium import webdriver

class WebDriverUtility():
    def get_new_web_driver(self):
        os.environ['MOZ_HEADLESS'] = '1'
        driver = webdriver.Firefox()

        return driver

    def get_home_page(self):
        return "http://localhost:8082"

    def get_add_car_page(self):
        return self.get_home_page() + "/addcar"