from django.conf.urls import include, url
from django.conf import settings
from .views import OauthCarsUrl
from .views import CarsUrl
from .views import CarDetailUrl

app_name = 'api'

urlpatterns = [
    url(r'^resources/cars/(?P<car_id>[0-9]+)$', CarDetailUrl.as_view(), name='car_detail'),
    url(r'^resources/cars$', CarsUrl.as_view(), name='cars'),
    # OAuth 2 endpoints:
    url(r'^cars$', OauthCarsUrl.as_view()),
]
