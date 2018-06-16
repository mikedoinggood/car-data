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
    trim_levels = data.get('trimLevels')

    if not year or not make or not model:
        return HttpResponse(status=400, content_type='application/json')

    with transaction.atomic():
        car = Car.objects.create(year=year, make=make, model=model)

        if trim_levels:
            for trim_level in trim_levels:
                if trim_level['name']:
                    car.trimlevel_set.add(TrimLevel(car=car, name=trim_level['name']), bulk=False)

    saved_car = Car.objects.get(id=car.id)
    car_dict = convert_car_to_dict(saved_car)

    return HttpResponse(json.dumps(car_dict, indent=4), content_type='application/json')

def car_detail_get(self, request, *arg, **kwargs):
    try:
        car = Car.objects.get(pk=kwargs['car_id'])
    except Car.DoesNotExist as e:
        return HttpResponse(status=404, content_type='application/json')

    car_dict = convert_car_to_dict(car)

    return HttpResponse(json.dumps(car_dict, indent=4), content_type='application/json')

def car_update_put(self, request, *args, **kwargs):
    try:
        car = Car.objects.get(pk=kwargs['car_id'])
    except Car.DoesNotExist as e:
        return HttpResponse(status=404, content_type='application/json')

    data = json.loads(request.body)

    car.year = data.get('year')
    car.make = data.get('make')
    car.model = data.get('model')
    trim_levels = data.get('trimLevels')

    if not car.year or not car.make or not car.model:
        return HttpResponse(status=400, content_type='application/json')

    with transaction.atomic():
        car.save()

        if trim_levels:
            for trim_level in trim_levels:
                if trim_level.get('id'):
                    if trim_level.get('name'):
                        try:
                            existing_trim_level =  car.trimlevel_set.get(pk=trim_level.get('id'))
                        except TrimLevel.DoesNotExist as e:
                            continue

                        if trim_level.get('delete'):
                            existing_trim_level.delete()
                        else:
                            existing_trim_level.name = trim_level['name']
                            existing_trim_level.save()
                    else:
                        return HttpResponse(status=400, content_type='application/json')
                else:
                    if trim_level.get('name'):
                        car.trimlevel_set.add(TrimLevel(car=car, name=trim_level['name']), bulk=False)

    saved_car = Car.objects.get(id=car.id)
    car_dict = convert_car_to_dict(saved_car)

    return HttpResponse(json.dumps(car_dict, indent=4), content_type='application/json')

def car_delete(self, request, *arg, **kwargs):
    try:
        Car.objects.get(pk=kwargs['car_id']).delete()
    except Car.DoesNotExist as e:
        return HttpResponse(status=404, content_type='application/json')

    return HttpResponse(status=200, content_type='application/json')

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
    raise_exception = True

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

class CarDetailUrl(LoginRequiredMixin, View):
    raise_exception = True

    def get(self, request, *arg, **kwargs):
        return car_detail_get(self, request, *arg, **kwargs)

    def put(self, request, *arg, **kwargs):
        return car_update_put(self, request, *arg, **kwargs)

    def delete(self, request, *arg, **kwargs):
        return car_delete(self, request, *arg, **kwargs)
