package com.stock.api;


import com.stock.api.model.StocksReder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StockApiProject {
        @Autowired
        static StocksReder stocksReder = new StocksReder();

        public static void main(String[] args){
                SpringApplication.run(StockApiProject.class,args);
                stocksReder.currentStocksPriceReder();
                stocksReder.historyStocksPrice();
        }
}

