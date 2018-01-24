from django.conf.urls import include, url
from django.contrib.auth import views as auth_views
from django.contrib.auth.forms import AuthenticationForm
  
from . import views

app_name = 'web'
urlpatterns = [
    # url(r'^oauth/', include('oauth2_provider.urls', namespace='oauth2_provider')),
    url(r'^login$', auth_views.LoginView.as_view(template_name='web/login.html'), name='login'),
    url(r'^logout$', views.logout, name='logout'),
    url(r'^addcar$', views.addCar, name='addcar'),
    url(r'^cars/(?P<car_id>[0-9]+)/edit$', views.edit_car, name='edit_car'),
    url(r'^cars/(?P<car_id>[0-9]+)$', views.car_detail, name='car_detail'),
    url(r'^$', views.index, name='index'),
]
