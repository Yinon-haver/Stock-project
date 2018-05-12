package com.stock.api.model;

import com.stock.api.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StocksReder {

    @Autowired
    ValidationService validationService ;

    public void currentStocksPriceReder(){
        boolean insertDatt = true;
        while ( insertDatt){
            Scanner in = new Scanner(System.in);
            System.out.println("Enter Your Stock name:");
            String stockName = in.nextLine();
            System.out.println("Enter Stock current price:");
            double stockPrice = in.nextDouble();
            validationService.addCurrentStockrice(stockName,stockPrice);

            System.out.println("if you want to add more socks ?  true/false");
            insertDatt=in.nextBoolean();
        }
    }

    public void historyStocksPrice(){
        boolean insertDatt = true;

        System.out.println("----------------------------------------------\nEnter History price for stocks for last 7 days\n----------------------------------------------");
        while ( insertDatt){
            Scanner in = new Scanner(System.in);
            System.out.println("Enter Your Stock name:");
            String stockName = in.nextLine();
            for (int i = 1; i < 8 ; i++) {
                System.out.println("Enter Stock prise in day "+ i + " : ");
                double stockPrice = in.nextDouble();
                validationService.addHistoryPrice(stockName,i-1,stockPrice);
            }
            System.out.println("if you want to add more socks history data ?  true/false");
            insertDatt=in.nextBoolean();
        }
        System.out.println("finishing adding history price");
    }
}
