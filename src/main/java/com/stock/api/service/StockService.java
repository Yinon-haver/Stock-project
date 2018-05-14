package com.stock.api.service;

import com.stock.api.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class StockService {

    @Autowired
    private ValidationService validationService;

    public List<String> getAllStocks(){
        List<String> allStock = new ArrayList<>();
        for (String stockName : validationService.getStocksCurrentPrice().keySet()) {
            allStock.add(stockName);
        }
        return allStock;
    }

    public void addStocks(Map<Stock,List<Double> > stocksMap){
        for (Stock stock :stocksMap.keySet()) {
            //insert current stock to StocksCurrentPrice map
            Random randomGenerator = new Random();
            Double currentVal  = randomGenerator.nextDouble()+ 100;
            validationService.getStocksCurrentPrice().put(stock.getName(),currentVal);

            //inset current stock list price for lsat 7 days to the
            validationService.getStocksHistoryPrice().put(stock.getName(),stocksMap.get(stock));
        }
    }

    public List<Double> getHistoryStockPrice(Stock stock){
        return   validationService.getStocksHistoryPrice().get(stock.getName());
    }

    public Map<String, List<Double>> getAllHistoryStockPrices() {
        return validationService.getStocksHistoryPrice();
    }
}
