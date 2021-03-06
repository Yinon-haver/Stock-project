package com.stock.api.controllers;

import com.stock.api.model.Stock;
import com.stock.api.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    StockService stockService;

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    public List<String> getAllStocks(){
        logger.info("StockController.get all stocks name");
        return stockService.getAllStocks();
    }

    @RequestMapping(value = "/stocks" ,method = RequestMethod.POST)
    public String newStock(@RequestBody Map<Stock,List<Double> > stocksMap){
        logger.info("StockController.sent map of new stock and their value for last 7 days ");
        stockService.addStocks(stocksMap);
        return "new stocks added";
    }

    @RequestMapping(value = "/StockHistoryPrice/{stockname}",method = RequestMethod.GET)
    public String getHistoryStockPrice(@PathVariable Stock stockname){
        logger.info("StockController.get history prices for specific stock");
        if(stockService.getHistoryStockPrice(stockname) == null){
            return "the stock does not exist in DB";
        }else {
            return "stock prices is :" +stockService.getHistoryStockPrice(stockname);
        }
    }

    @RequestMapping(value = "/historyStocksPrices" , method = RequestMethod.GET)
    public Map<String, List<Double>> getHistoryStocksPrices(){
        logger.info("StockController.get history prices for all stocks");
        return stockService.getAllHistoryStockPrices();
    }
}
