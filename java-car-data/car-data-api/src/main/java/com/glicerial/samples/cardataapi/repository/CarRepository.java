package com.glicerial.samples.cardataapi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.glicerial.samples.cardataapi.entity.Car;

public interface CarRepository extends PagingAndSortingRepository<Car, Long>{

}
