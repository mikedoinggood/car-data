from django.contrib.auth.views import logout_then_login
from django.contrib.auth.decorators import login_required
from django.contrib.auth.forms import AuthenticationForm 
from django.shortcuts import render
from django.views.decorators.csrf import ensure_csrf_cookie


def login(request):
    form = AuthenticationForm(request)
    context = {'form': form}
    form.fields['username'].widget.attrs['class'] = "form-control"
    form.fields['password'].widget.attrs['class'] = "form-control"
    return render(request, 'web/login.html', context)

@login_required
def logout(request):
    return logout_then_login(request)

@login_required
def index(request):
    return render(request, 'web/index.html')

@login_required
def charts(request):
    return render(request, 'web/charts.html')

@login_required
@ensure_csrf_cookie
def addCar(request):
    return render(request, 'web/addcar.html')

@login_required
def car_detail(request, car_id):
    return render(request, 'web/cardetail.html')

@login_required
def edit_car(request, car_id):
    return render(request, 'web/editcar.html')
