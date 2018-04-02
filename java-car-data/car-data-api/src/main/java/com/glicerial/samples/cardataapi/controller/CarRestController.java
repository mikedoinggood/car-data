package com.glicerial.samples.cardataapi.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(method = RequestMethod.GET, value = { "/api/cars/{id}", "/resources/cars/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    ResponseEntity<?> readCar(@PathVariable String id) {
        Car car = carRepository.findOne(Long.valueOf(id));

        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).build();
        }

        return ResponseEntity.ok(car);
    }

    @RequestMapping(method = RequestMethod.PUT, value = { "/api/cars/{id}", "/resources/cars/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    ResponseEntity<?> editCar(@PathVariable String id, @RequestBody Car submittedCar) {
        Car existingCar = carRepository.findOne(Long.valueOf(id));

        if (existingCar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).build();
        }

        if (!validateCar(submittedCar)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).build();
        }


        existingCar.setYear(submittedCar.getYear());
        existingCar.setMake(submittedCar.getMake());
        existingCar.setModel(submittedCar.getModel());

        Set<TrimLevel> existingCarTrimLevels = existingCar.getTrimLevels();
        Set<TrimLevel> submittedCarTrimLevels = submittedCar.getTrimLevels();

        if (submittedCarTrimLevels == null) {
            existingCarTrimLevels.clear();
        } else {
            // Deletes
            deleteTrimLevels(existingCarTrimLevels, submittedCarTrimLevels);

            // Updates
            updateTrimLevels(existingCar, existingCarTrimLevels, submittedCarTrimLevels);
        }

        carRepository.save(existingCar);

        return ResponseEntity.ok(existingCar);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = { "/api/cars/{id}", "/resources/cars/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    ResponseEntity<?> deleteCar(@PathVariable String id) {
        try {
            carRepository.delete(Long.valueOf(id));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).build();
        }

        return ResponseEntity.ok().body("{}");
    }

    @RequestMapping(method = RequestMethod.POST, value = { "api/cars", "/resources/cars" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    ResponseEntity<?> addCar(@RequestBody Car submittedCar) {
        if (!validateCar(submittedCar)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).build();
        }

        Set<TrimLevel> trimLevels = submittedCar.getTrimLevels();

        if (trimLevels != null) {
            Iterator<TrimLevel> iterator = trimLevels.iterator();

            while (iterator.hasNext()) {
                TrimLevel trimLevel = iterator.next();

                if (StringUtils.isBlank(trimLevel.getName())) {
                    iterator.remove();
                } else {
                    trimLevel.setCar(submittedCar);
                }
            }
        }

        carRepository.save(submittedCar);

        return ResponseEntity.ok(submittedCar);
    }

    private boolean validateCar(Car submittedCar) {
        if (StringUtils.isBlank(submittedCar.getMake()) || StringUtils.isBlank(submittedCar.getModel())) {
            return false;
        }

        Set<TrimLevel> submittedCarTrimLevels = submittedCar.getTrimLevels();

        if (submittedCarTrimLevels != null) {
            Iterator<TrimLevel> submittedCarTrimLevelsIterator = submittedCarTrimLevels.iterator();

            while (submittedCarTrimLevelsIterator.hasNext()) {
                TrimLevel submittedTrimLevel = submittedCarTrimLevelsIterator.next();

                if (submittedTrimLevel.getId() != null && StringUtils.isBlank(submittedTrimLevel.getName())) {
                    return false;
                }
            }
        }

        return true;
    }

    private void deleteTrimLevels(Set<TrimLevel> existingCarTrimLevels, Set<TrimLevel> submittedCarTrimLevels) {
        Iterator<TrimLevel> existingCarTrimLevelsIterator = existingCarTrimLevels.iterator();

        while (existingCarTrimLevelsIterator.hasNext()) {
            TrimLevel existingTrimLevel = existingCarTrimLevelsIterator.next();
            Iterator<TrimLevel> submittedCarTrimLevelsIterator = submittedCarTrimLevels.iterator();
            Boolean existingTrimLevelFound = false;

            while (submittedCarTrimLevelsIterator.hasNext()) {
                TrimLevel submittedTrimLevel = submittedCarTrimLevelsIterator.next();
                if (existingTrimLevel.getId().equals(submittedTrimLevel.getId())) {
                    existingTrimLevelFound = true;
                    break;
                }
            }

            if (!existingTrimLevelFound) {
                existingCarTrimLevelsIterator.remove();
            }
        }
    }

    private void updateTrimLevels(Car existingCar, Set<TrimLevel> existingCarTrimLevels, Set<TrimLevel> submittedCarTrimLevels) {
        Iterator<TrimLevel> submittedCarTrimLevelsIterator = submittedCarTrimLevels.iterator();

        while (submittedCarTrimLevelsIterator.hasNext()) {
            TrimLevel submittedTrimLevel = submittedCarTrimLevelsIterator.next();

            // Adds
            if (submittedTrimLevel.getId() == null) {
                if (!StringUtils.isBlank(submittedTrimLevel.getName())) {
                    submittedTrimLevel.setCar(existingCar);
                    existingCar.getTrimLevels().add(submittedTrimLevel);
                }
            // Edits
            } else {
                Iterator<TrimLevel> existingCarTrimLevelsIterator = existingCarTrimLevels.iterator();

                while (existingCarTrimLevelsIterator.hasNext()) {
                    TrimLevel existingTrimLevel = existingCarTrimLevelsIterator.next();

                    if (submittedTrimLevel.getId().equals(existingTrimLevel.getId())) {
                        existingTrimLevel.setName(submittedTrimLevel.getName());
                    }
                }
            }
        }
    }
}
