package com.glicerial.samples.cardataapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.glicerial.samples.cardataapi.entity.Car;

public interface CarRepository extends PagingAndSortingRepository<Car, Long>{
    @Query("SELECT c.make, COUNT(c) " +
           "FROM Car c " +
           "GROUP BY c.make")
    List<Object[]> findMakeCount();

    @Query("SELECT c.year, COUNT(c) " +
           "FROM Car c " +
           "GROUP BY c.year")
    List<Object[]> findYearCount();
}
