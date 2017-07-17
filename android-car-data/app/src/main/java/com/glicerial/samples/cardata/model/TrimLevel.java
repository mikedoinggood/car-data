package com.glicerial.samples.cardata.model;


public class TrimLevel {
    private Long id;
    private String name;

    public TrimLevel(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
