from django.db import models
  

class Car(models.Model):
    year = models.PositiveSmallIntegerField()
    make = models.CharField(max_length=255)
    model = models.CharField(max_length=255)

class TrimLevel(models.Model):
    car = models.ForeignKey(Car, on_delete=models.CASCADE)
    name = models.CharField(max_length=255)

