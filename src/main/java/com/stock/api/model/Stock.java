package com.stock.api.model;

public class Stock {

    private String name;

    public Stock(String name ) {
        this.name = name;
    }

    public Stock(){
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


}

