import logging
import logging.handlers

def get_logger(name):
    LOG = logging.getLogger(name)
    LOG.setLevel(logging.INFO)
    log_format = logging.Formatter('%(asctime)s - %(name)s - [%(levelname)s]: %(message)s')
    file_handler = logging.handlers.TimedRotatingFileHandler("car-data-ui-tests.log", when='midnight', backupCount=7)
    file_handler.setFormatter(log_format)
    console_handler = logging.StreamHandler()
    console_handler.setFormatter(log_format)
    LOG.addHandler(file_handler)
    LOG.addHandler(console_handler)

    return LOG