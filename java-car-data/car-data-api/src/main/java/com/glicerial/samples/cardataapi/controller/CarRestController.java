package com.glicerial.samples.cardataapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    Page<Car> readCars(@RequestParam("sort") Optional<String> sort, @RequestParam("page") Optional<String> page) {
        List<Car> carList = new ArrayList<Car>();
        Direction sortDirection = Sort.Direction.ASC;
        String sortProperty = "make";
        int pageSize = 20;
        int pageNumber = 1;

        if (page.isPresent()) {
            try {
                pageNumber = Integer.max(1, Integer.parseInt(page.get()));
            } catch (NumberFormatException ex) {
                pageNumber = 1;
            }
        }

        if (sort.isPresent()) {
            switch (sort.get()) {
                case "oldest":
                    sortProperty = "year";
                    break;
                case "newest":
                    sortDirection = Sort.Direction.DESC;
                    sortProperty = "year";
                    break;
                default:
                    break;
            }
        }

        Page<Car> carsPage = carRepository.findAll(new PageRequest(pageNumber - 1, pageSize, sortDirection, sortProperty, "make"));
        Iterator<Car> iterator = carsPage.iterator();
        while(iterator.hasNext()) {
            carList.add(iterator.next());
        }

        return carsPage;
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

    @RequestMapping(method = RequestMethod.POST, value = { "/api/cars", "/resources/cars" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    ResponseEntity<?> addCar(@RequestBody Car submittedCar) {
        if (!validateCar(submittedCar)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).build();
        }

        Car newCar = new Car(submittedCar.getYear(), submittedCar.getMake(), submittedCar.getModel());
        newCar.setTrimLevels(new HashSet<TrimLevel>());

        Set<TrimLevel> submittedTrimLevels = submittedCar.getTrimLevels();

        if (submittedTrimLevels != null) {
            Iterator<TrimLevel> iterator = submittedTrimLevels.iterator();

            while (iterator.hasNext()) {
                TrimLevel trimLevel = iterator.next();

                if (StringUtils.isBlank(trimLevel.getName())) {
                    iterator.remove();
                } else {
                    trimLevel.setCar(newCar);
                    newCar.getTrimLevels().add(trimLevel);
                }
            }
        }

        carRepository.save(newCar);

        return ResponseEntity.ok(newCar);
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/api/stats", "/resources/stats" }, produces = {MediaType.APPLICATION_JSON_VALUE, "application/json"})
    ResponseEntity<?> readStats() {
        List<Object[]> makeCountResult = carRepository.findMakeCount();
        List<Object[]> yearCountResult = carRepository.findYearCount();
        Map<String, Long> makeCountMap = new HashMap<String, Long>();
        Map<String, Long> yearCountMap = new HashMap<String, Long>();

        makeCountResult.forEach(item->{
            makeCountMap.put(item[0].toString(), (Long) item[1]);
        });

        yearCountResult.forEach(item->{
            yearCountMap.put(item[0].toString(), (Long) item[1]);
        });

        HashMap<String, Map<String, Long>> resultsMap = new HashMap<String, Map<String, Long>>();
        resultsMap.put("makeCounts", makeCountMap);
        resultsMap.put("yearCounts", yearCountMap);

        return ResponseEntity.ok(resultsMap);
    }

    private boolean validateCar(Car submittedCar) {
        int year = submittedCar.getYear();

        if (year < 1885 || year > 3000) {
            return false;
        }

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
