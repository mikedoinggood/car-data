package com.glicerial.samples.cardataapi;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.glicerial.samples.cardataapi.entity.Car;
import com.glicerial.samples.cardataapi.entity.TrimLevel;

import com.glicerial.samples.cardataapi.repository.CarRepository;
import com.glicerial.samples.cardataapi.repository.TrimLevelRepository;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(CarRepository carRepository, TrimLevelRepository trimLevelRepository) {
        return (args) -> {
            Set<TrimLevel> hondaCivicTrimLevels = new HashSet<>();
            hondaCivicTrimLevels.add(new TrimLevel("LX"));
            hondaCivicTrimLevels.add(new TrimLevel("DX"));
            hondaCivicTrimLevels.add(new TrimLevel("EX"));
            trimLevelRepository.save(hondaCivicTrimLevels);
            Car hondaCivic = new Car(2010, "Honda",  "Civic", hondaCivicTrimLevels); 

            Set<TrimLevel> fordFocusTrimLevels = new HashSet<>();
            fordFocusTrimLevels.add(new TrimLevel("S"));
            fordFocusTrimLevels.add(new TrimLevel("SE"));
            fordFocusTrimLevels.add(new TrimLevel("RS"));
            trimLevelRepository.save(fordFocusTrimLevels);
            Car fordFocus = new Car(2014, "Ford", "Focus", fordFocusTrimLevels);

            Set<TrimLevel> toyotaCorollaTrimLevels = new HashSet<>();
            toyotaCorollaTrimLevels.add(new TrimLevel("L"));
            toyotaCorollaTrimLevels.add(new TrimLevel("LE"));
            toyotaCorollaTrimLevels.add(new TrimLevel("SE"));
            trimLevelRepository.save(toyotaCorollaTrimLevels);
            Car toyotaCorolla = new Car(2011, "Toyota", "Corolla", toyotaCorollaTrimLevels); 

            carRepository.save(hondaCivic);
            carRepository.save(fordFocus);
            carRepository.save(toyotaCorolla);
        };
    }
}
