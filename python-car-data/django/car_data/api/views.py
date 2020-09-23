import json

from django.contrib.auth.mixins import LoginRequiredMixin
from django.core import serializers
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from django.db import transaction
from django.db.models import Count
from django.http import HttpResponse
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from django.views.generic import View
from oauth2_provider.views.generic import ProtectedResourceView

from .models import Car, TrimLevel

def cars_get(request):
    page_size = 20
    sort = request.GET.get('sort', '')
    page_number = request.GET.get('page', '')

    if sort == "newest":
        all_cars_list = Car.objects.order_by('-year', 'make').all()
    elif sort == "oldest":
        all_cars_list = Car.objects.order_by('year', 'make').all()
    else:
        all_cars_list = Car.objects.order_by('make').all()

    paginator = Paginator(all_cars_list, page_size)

    try:
        page_number = int(page_number)
    except ValueError as e:
        page_number = 1

    car_page = {
        'number': page_number,
        'totalPages': paginator.num_pages
    }

    try:
        cars = paginator.page(page_number)
        car_list = []

        for car in cars.object_list.all().iterator():
            car_dict = convert_car_to_dict(car)
            car_list.append(car_dict)

        car_page['content'] = car_list
    except EmptyPage:
        car_page['content'] = []

    return HttpResponse(json.dumps(car_page, indent=4), content_type='application/json')

def cars_post(request):
    data = json.loads(request.body)
    year = data.get('year')
    make = data.get('make')
    model = data.get('model')
    trim_levels = data.get('trimLevels')

    if not validate_car(year, make, model, trim_levels):
        return HttpResponse(status=400, content_type='application/json')

    with transaction.atomic():
        car = Car.objects.create(year=year, make=make, model=model)

        if trim_levels:
            for trim_level in trim_levels:
                trim_level_name = trim_level.get('name')
                if trim_level_name and trim_level_name.strip():
                    car.trimlevel_set.add(TrimLevel(car=car, name=trim_level['name']), bulk=False)

    car_dict = convert_car_to_dict(car)

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

    if not validate_car(car.year, car.make, car.model, trim_levels):
        return HttpResponse(status=400, content_type='application/json')

    with transaction.atomic():
        car.save()

        if trim_levels:
            for trim_level in trim_levels:
                if trim_level.get('id'):
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
                    trim_level_name = trim_level.get('name')
                    if trim_level_name and trim_level_name.strip():
                        car.trimlevel_set.add(TrimLevel(car=car, name=trim_level['name']), bulk=False)

    car_dict = convert_car_to_dict(car)

    return HttpResponse(json.dumps(car_dict, indent=4), content_type='application/json')

def car_delete(self, request, *arg, **kwargs):
    try:
        Car.objects.get(pk=kwargs['car_id']).delete()
    except Car.DoesNotExist as e:
        return HttpResponse(status=404, content_type='application/json')

    return HttpResponse(status=200, content_type='application/json')

def validate_car(year, make, model, trim_levels):
    if not year or not make or not make.strip() or not model or not model.strip():
        return False

    try:
        if year < 1885 or year > 3000:
            return False
    except TypeError as e:
        return False

    if trim_levels:
        for trim_level in trim_levels:
            if trim_level.get('id') and not trim_level.get('name'):
                return False

    return True

def stats_get(self, request, *arg, **kwargs):
    make_counts = Car.objects.values('make').annotate(count=Count('id'))
    year_counts = Car.objects.values('year').annotate(count=Count('id'))

    make_data = {}
    for item in make_counts:
        make_data[item['make']] = item['count']

    year_data = {}
    for item in year_counts:
        year_data[item['year']] = item['count']

    data = {
        'makeCounts': make_data,
        'yearCounts': year_data,
    }

    return HttpResponse(json.dumps(data, indent=4), content_type='application/json')

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

class StatsUrl(LoginRequiredMixin, View):
    raise_exception = True

    def get(self, request, *arg, **kwargs):
        return stats_get(self, request, *arg, **kwargs)
