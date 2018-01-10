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
    url(r'^$', views.index, name='index'),
]
