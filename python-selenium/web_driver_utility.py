from selenium import webdriver

class WebDriverUtility():
    FIREFOX_DRIVER = 0
    PHANTOMJS_DRIVER = 1
        
    def get_new_web_driver(self, driver_type):
        if driver_type == self.PHANTOMJS_DRIVER:
            driver = webdriver.PhantomJS()
        elif driver_type == self.FIREFOX_DRIVER:
            driver = webdriver.Firefox()

        driver.set_window_size(1440, 900)

        return driver

    def get_home_page(self):
        return "http://localhost:8082"