package com.glicerial.samples.cardataapi.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.glicerial.samples.cardataapi.entity.Car;
import com.glicerial.samples.cardataapi.repository.CarRepository;
import com.glicerial.samples.cardataapi.repository.TrimLevelRepository;


@RestController
public class CarRestController {

    private final CarRepository carRepository;
    private final TrimLevelRepository trimLevelRepository;

    @Autowired
    public CarRestController(CarRepository carRepository, TrimLevelRepository trimLevelRepository) {
        this.carRepository = carRepository;
        this.trimLevelRepository = trimLevelRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/api/cars", "/resources/cars" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    List<Car> readCars() {
        Iterable<Car> carIterable = carRepository.findAll();
        List<Car> carList = new ArrayList<Car>();
        Iterator<Car> iterator = carIterable.iterator();
        while(iterator.hasNext()) {
            carList.add(iterator.next());
        }
        return carList;
    }    

    @RequestMapping(method = RequestMethod.POST, value = { "api/cars", "/resources/cars" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    Car addCar(@RequestBody Car car) {
        trimLevelRepository.save(car.getTrimLevels());
        carRepository.save(car);
        return car;
    }
}
