import os

from selenium import webdriver

class WebDriverUtility():
    def get_new_web_driver(self):
        os.environ['MOZ_HEADLESS'] = '1'
        driver = webdriver.Firefox()

        return driver

    def get_home_page_url(self):
        return "http://localhost:8082"

    def get_add_car_page_url(self):
        return self.get_home_page_url() + "/addcar"

    def get_charts_page_url(self):
        return self.get_home_page_url() + "/charts"
