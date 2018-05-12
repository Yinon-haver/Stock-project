package com.stock.api.model;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StockState {
    @NotNull(message ="enter stock name ")
    private Stock stock ;
    @NotNull
    @Min(0)
    private Integer numberOfStocks;
    @NotNull
    private boolean sold;


    public StockState(Stock stock, int numberOfStocks,boolean sold) {
        this.stock = stock;
        this.numberOfStocks = numberOfStocks;
        this.sold = sold;
    }
    public StockState() {
    }

    public Stock getStock() {
        return stock;
    }

    public int getNumberOfStocks() {
        return numberOfStocks;
    }

    public void setNumberOfStocks(int numberOfStocks) {
        this.numberOfStocks = numberOfStocks;
    }

    public boolean isSold() {
        return sold;
    }


}
