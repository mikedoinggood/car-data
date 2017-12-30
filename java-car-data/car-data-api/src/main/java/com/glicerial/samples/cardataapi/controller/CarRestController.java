package com.glicerial.samples.cardataapi.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.glicerial.samples.cardataapi.entity.Car;
import com.glicerial.samples.cardataapi.entity.TrimLevel;
import com.glicerial.samples.cardataapi.repository.CarRepository;


@RestController
public class CarRestController {

    private final CarRepository carRepository;

    @Autowired
    public CarRestController(CarRepository carRepository) {
        this.carRepository = carRepository;
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
        Set<TrimLevel> trimLevels = car.getTrimLevels();

        if (trimLevels != null) {
            Iterator<TrimLevel> iterator = trimLevels.iterator();
            while (iterator.hasNext()) {
                TrimLevel tl = iterator.next();

                if (tl.getName().trim().equals("")) {
                    iterator.remove();
                } else {
                    tl.setCar(car);
                }
            }
        }

        carRepository.save(car);

        return car;
    }
}
