import json

from django.contrib.auth.mixins import LoginRequiredMixin
from django.core import serializers
from django.db import transaction
from django.http import HttpResponse
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from django.views.generic import View
from oauth2_provider.views.generic import ProtectedResourceView

from .models import Car, TrimLevel

def cars_get(request):
    car_list = []

    for car in Car.objects.all().iterator():
        car_dict = convert_car_to_dict(car)
        car_list.append(car_dict)

    return HttpResponse(json.dumps(car_list, indent=4), content_type='application/json')

def cars_post(request):
    data = json.loads(request.body)
    year = data.get('year')
    make = data.get('make')
    model = data.get('model')
    trim_levels = data.get('trimLevels', [])

    with transaction.atomic():
        car = Car.objects.create(year=year, make=make, model=model)

        for trim_level in trim_levels:
            if trim_level['name']:
                car.trimlevel_set.add(TrimLevel(car=car, name=trim_level['name']), bulk=False)

    saved_car = Car.objects.get(id=car.id)
    car_dict = convert_car_to_dict(saved_car)

    return HttpResponse(json.dumps(car_dict, indent=4), content_type='application/json')

def convert_car_to_dict(car):
    trim_levels = []

    for trim_level in car.trimlevel_set.all().iterator():
        trim_level_dict = {
            'id': trim_level.id,
            'name': trim_level.name
        }

        trim_levels.append(trim_level_dict)

    car_dict = {
        'id': car.id,
        'year': car.year,
        'make': car.make,
        'model': car.model,
        'trimLevels': trim_levels
    }

    return car_dict

class CarsUrl(LoginRequiredMixin, View):
    def get(self, request):
        return cars_get(request)

    def post(self, request):
        return cars_post(request)

@method_decorator(csrf_exempt, name='dispatch')
class OauthCarsUrl(ProtectedResourceView):
    def get(self, request):
        return cars_get(request)

    def post(self, request):
        return cars_post(request)