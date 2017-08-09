package com.glicerial.samples.cardataapi.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int year;
    private String make;
    private String model;
    @OneToMany(mappedBy="car", cascade = CascadeType.ALL)
    private Set<TrimLevel> trimLevels;

    public Car(int year, String make, String model) {
        this.year = year;
        this.make = make;
        this.model = model;
    }

    public Car() {
    // JPA only
    }

    public Long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Set<TrimLevel> getTrimLevels() {
        return trimLevels;
    }

    public void setTrimLevels(Set<TrimLevel> trimLevels) {
        this.trimLevels = trimLevels;
    }
}
