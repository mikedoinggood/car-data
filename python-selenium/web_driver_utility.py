from selenium import webdriver

class WebDriverUtility():
    FIREFOX_DRIVER = 0
    PHANTOMJS_DRIVER = 1

    def get_new_web_driver(self, driver_type):
        if driver_type == self.PHANTOMJS_DRIVER:
            driver = webdriver.PhantomJS()
            driver.set_window_size(1440, 900)
        elif driver_type == self.FIREFOX_DRIVER:
            driver = webdriver.Firefox()

        return driver

    def get_home_page(self):
        return "http://localhost:8082"

    def get_add_car_page(self):
        return self.get_home_page() + "/addcar"