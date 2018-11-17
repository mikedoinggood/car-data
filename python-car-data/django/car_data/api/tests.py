import json
import unittest

from pprint import pprint

from api.models import Car, TrimLevel
from django.contrib.auth.models import User
from django.test import TestCase


class CarDataTest(TestCase):
    MIN_VALID_MODEL_YEAR = 1885
    MAX_VALID_MODEL_YEAR = 3000

    def setUp(self):
        print("\n*** " + self._testMethodName + " ***")
        print("\nCreating user...")
        User.objects.create_user('user', password='password')

        print("Logging in...")
        self.client.login(username='user', password='password')

        print("Creating cars...")
        car1 = Car.objects.create(year=2017, make="Honda", model="Accord")
        car1.trimlevel_set.add(TrimLevel(car=car1, name="EX"), bulk=False)
        car1.trimlevel_set.add(TrimLevel(car=car1, name="LX"), bulk=False)
        car2 = Car.objects.create(year=2017, make="Ford", model="Focus")

        self.car_list = []
        self.car_list.extend((car1, car2))

        self.car = {
            'year': 2010,
            'make': 'Toyota',
            'model': 'Prius Prime',
            'trimLevels': [
                { 'name': 'Plus'},
                { 'name': 'Premium'},
                { 'name': 'Advanced'},
            ]
        }

    def test_read_cars(self):
        response = self.client.get('/api/resources/cars')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)
        self.assertEqual(len(response.json()[0]['trimLevels']), 2)
        self.assertEqual(len(response.json()), 2)

    def test_read_one_car(self):
        car_id = self.car_list[0].id
        response = self.client.get('/api/resources/cars/{}'.format(car_id))

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_read_one_car_not_found(self):
        response = self.client.get('/api/resources/cars/{}'.format(9999))

        self.assertEqual(response.status_code, 404)

    def test_add_car(self):
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_add_car_no_year(self):
        self.car.pop('year', None)
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_add_car_empty_year(self):
        self.car['year'] = ""
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_add_year_1884(self):
        self.do_add_car_test_with_year(1884)

    def test_add_year_1885(self):
        self.do_add_car_test_with_year(1885)

    def test_add_year_1886(self):
        self.do_add_car_test_with_year(1886)

    def test_add_year_2999(self):
        self.do_add_car_test_with_year(2999)

    def test_add_year_3000(self):
        self.do_add_car_test_with_year(3000)

    def test_add_year_3001(self):
        self.do_add_car_test_with_year(3001)

    def test_add_car_no_make(self):
        self.car.pop('make', None)
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_add_car_empty_make(self):
        self.car['make'] = ""
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_add_car_no_model(self):
        self.car.pop('model', None)
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_add_car_empty_model(self):
        self.car['model'] = ""
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_add_car_no_trim_levels(self):
        self.car.pop('trimLevels', None)
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_add_car_empty_trim_levels(self):
        self.car['trimLevels'] = ""
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_add_car_new_empty_trim_level(self):
        self.car['trimLevels'].append({'name': ''})
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_add_car_extra_data(self):
        self.car['extra'] = {
            'something': 'extra'
        }
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_edit_car(self):
        existing_car_id = self.car_list[0].id
        # Limit query set result to 1, get first element of that query set
        existing_trim_level = self.car_list[0].trimlevel_set.all()[:1][0]
        self.car['trimLevels'].append({
                'id': existing_trim_level.id,
                'name': 'Updated'
            }
        )

        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_edit_car_not_found(self):
        response = self.client.put('/api/resources/cars/{}'.format(9999), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 404)

    def test_edit_car_no_year(self):
        existing_car_id = self.car_list[0].id
        self.car.pop('year', None)
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_edit_car_empty_year(self):
        existing_car_id = self.car_list[0].id
        self.car['year'] = ""
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_edit_car_year_1884(self):
        self.do_edit_car_test_with_year(1884)

    def test_edit_car_year_1885(self):
        self.do_edit_car_test_with_year(1885)

    def test_edit_car_year_1886(self):
        self.do_edit_car_test_with_year(1886)

    def test_edit_car_year_2999(self):
        self.do_edit_car_test_with_year(2999)

    def test_edit_car_year_3000(self):
        self.do_edit_car_test_with_year(3000)

    def test_edit_car_year_3001(self):
        self.do_edit_car_test_with_year(3001)

    def test_edit_car_no_make(self):
        existing_car_id = self.car_list[0].id
        self.car.pop('make', None)
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_edit_car_no_model(self):
        existing_car_id = self.car_list[0].id
        self.car.pop('model', None)
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_edit_car_no_trim_levels(self):
        existing_car_id = self.car_list[0].id
        self.car.pop('trimLevels', None)
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_edit_car_existing_trim_level_no_name(self):
        existing_car_id = self.car_list[0].id
        # Limit query set result to 1, get first element of that query set
        existing_trim_level = self.car_list[0].trimlevel_set.all()[:1][0]
        self.car['trimLevels'].append({'id': existing_trim_level.id})
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_edit_car_existing_trim_level_empty_name(self):
        existing_car_id = self.car_list[0].id
        # Limit query set result to 1, get first element of that query set
        existing_trim_level = self.car_list[0].trimlevel_set.all()[:1][0]
        self.car['trimLevels'].append({
                'id': existing_trim_level.id,
                'name': '',
            }
        )
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 400)

    def test_edit_car_trim_level_not_found(self):
        existing_car_id = self.car_list[0].id
        self.car['trimLevels'].append({
                'id': 9999,
                'name': 'Invalid'
            }
        )
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 200)

    def test_edit_car_trim_level_other_car(self):
        existing_car_id = self.car_list[1].id
        other_existing_car = self.car_list[0]
        # Limit query set result to 1, get first element of that query set
        other_existing_trim_level = other_existing_car.trimlevel_set.all()[:1][0]
        self.car['trimLevels'].append({
                'id': other_existing_trim_level.id,
                'name': 'Other Car Trim Level Updated'
            }
        )
        print("\nSubmitting:")
        pprint(self.car)
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')
        print("\nResponse for PUT request:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

        # Check other car's trim level
        response_other_existing_car = self.client.get('/api/resources/cars/{}'.format(other_existing_car.id))
        print("\nResponse for GET request:")
        pprint(response_other_existing_car.json())
        response_other_existing_trim_levels = response_other_existing_car.json()['trimLevels']
        for trim_level in response_other_existing_trim_levels:
            if trim_level['id'] == other_existing_trim_level.id:
                self.assertEqual(trim_level['name'], other_existing_trim_level.name)
                break

    def test_edit_car_new_trim_level(self):
        existing_car_id = self.car_list[0].id
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_edit_car_new_blank_trim_level(self):
        existing_car_id = self.car_list[0].id
        self.car['trimLevels'].append({})
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_edit_car_extra_data(self):
        existing_car_id = self.car_list[0].id
        self.car['extra'] = {
            'something': 'extra'
        }
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        print("\nResponse:")
        pprint(response.json())
        self.assertEqual(response.status_code, 200)

    def test_delete_car(self):
        existing_car_id = self.car_list[0].id
        response = self.client.delete('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 200)

    def test_delete_non_existing_car(self):
        response = self.client.delete('/api/resources/cars/{}'.format(9999), json.dumps(self.car), 'application/json')

        self.assertEqual(response.status_code, 404)

    def test_read_stats(self):
        response = self.client.get('/api/resources/stats')
        stats = response.json()

        makeCounts = stats['makeCounts']
        self.assertEqual(makeCounts['Honda'], 1)
        self.assertEqual(makeCounts['Ford'], 1)

        yearCounts = stats['yearCounts']
        self.assertEqual(yearCounts['2017'], 2)

        self.assertEqual(response.status_code, 200)

    def do_add_car_test_with_year(self, year):
        self.car['year'] = year
        response = self.client.post('/api/resources/cars', json.dumps(self.car), 'application/json')

        if year < self.MIN_VALID_MODEL_YEAR or year > self.MAX_VALID_MODEL_YEAR:
            expected_response = 400
        else:
            expected_response = 200

        self.assertEqual(response.status_code, expected_response)

    def do_edit_car_test_with_year(self, year):
        existing_car_id = self.car_list[0].id
        self.car['year'] = year
        response = self.client.put('/api/resources/cars/{}'.format(existing_car_id), json.dumps(self.car), 'application/json')

        if year < self.MIN_VALID_MODEL_YEAR or year > self.MAX_VALID_MODEL_YEAR:
            expected_response = 400
        else:
            expected_response = 200

        self.assertEqual(response.status_code, expected_response)
