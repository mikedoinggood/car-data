package com.glicerial.samples.cardata.model;


import java.util.Set;

public class Car {
    private Long id;
    private int year;
    private String make;
    private String model;
    private Set<TrimLevel> trimLevels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
