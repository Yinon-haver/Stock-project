package com.stock.api.service;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

    private Map<String,Double> stocksCurrentPrice = new HashMap<>();
    private Map<String,List<Double>> stocksHistoryPrice = new HashMap<>();

    public void addCurrentStockrice(String stockName , Double stockPrice){
        stocksCurrentPrice.put(stockName,stockPrice);
    }

    public void addHistoryPrice(String stockName , int day  , double price){
        if(stocksHistoryPrice.get(stockName) == null){
            List<Double> historyPrice = new ArrayList(10);
            stocksHistoryPrice.put(stockName,historyPrice);
            stocksHistoryPrice.get(stockName).add(day,price);
        }else {
            stocksHistoryPrice.get(stockName).add(day,price);
        }
    }

    public Map<String, Double> getStocksCurrentPrice() {
        return stocksCurrentPrice;
    }

    public void setStocksCurrentPrice(Map<String, Double> stocksCurrentPrice) {
        this.stocksCurrentPrice = stocksCurrentPrice;
    }

    public Map<String, List<Double>> getStocksHistoryPrice() {
        return stocksHistoryPrice;
    }

    public void setStocksHistoryPrice(Map<String, List<Double>> stocksHistoryPrice) {
        this.stocksHistoryPrice = stocksHistoryPrice;
    }
}
