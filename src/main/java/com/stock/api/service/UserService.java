package com.stock.api.service;

import com.stock.api.database.MapDatabase;
import com.stock.api.model.Stock;
import com.stock.api.model.StockState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private ValidationService validationService;

    private Map<Integer, Map<String, StockState>> users = MapDatabase.getUsers();

    private int ID = 10000;

    public int addUser(Map<String, StockState> user) {

        for (Map.Entry<String, StockState> item : user.entrySet()) {
            StockState stockState = item.getValue();
            if (stockState.isSold() || stockState.getNumberOfStocks() < 0) {
                return 0;
            }
        }

        int id = ID++;
        users.put(id, user);
        return id;
    }

    public void enterDataToDatabaseAndToValidation() {
        for (int i = 0; i < 3; i++) {
            Map<String, StockState> tempMap = new HashMap<>();
            for (int j = 0; j < 7; j++) {
                Stock name = new Stock("lp " + j);
                String nameMap = "lp " + j;
                int numberOfStocks = (int) Math.pow(i, 2);
                boolean sold = false;
                StockState tempStokState = new StockState(name, numberOfStocks, sold);
                tempMap.put(nameMap, tempStokState);
            }
            users.put((int) Math.pow(i, 2), tempMap);
        }

        List<Double> data = new ArrayList<>();
        data.add(100.0);
        data.add(101.0);
        data.add(102.0);
        data.add(103.0);
        data.add(104.0);
        data.add(105.0);
        data.add(106.0);
        validationService.getStocksHistoryPrice().put("FYBER", data);

        List<Double> data1 = new ArrayList<>();
        data1.add(100.0);
        data1.add(101.0);
        data1.add(102.0);
        data1.add(103.0);
        data1.add(104.0);
        data1.add(105.0);
        data1.add(106.0);
        validationService.getStocksHistoryPrice().put("APPLE", data1);

        List<Double> data2 = new ArrayList<>();
        data2.add(100.0);
        data2.add(106.0);
        data2.add(150.0);
        data2.add(55.0);
        data2.add(99.0);
        data2.add(100.0);
        data2.add(108.0);
        validationService.getStocksHistoryPrice().put("GOOGLE", data2);

        List<Double> data3 = new ArrayList<>();
        data3.add(160.0);
        data3.add(1086.0);
        data3.add(159.0);
        data3.add(55.0);
        data3.add(92.0);
        data3.add(169.0);
        data3.add(110.0);
        validationService.getStocksHistoryPrice().put("TEVA", data2);

        List<Double> data4 = new ArrayList<>();
        data4.add(160.0);
        data4.add(1086.0);
        data4.add(159.0);
        data4.add(55.0);
        data4.add(92.0);
        data4.add(169.0);
        data4.add(110.0);
        validationService.getStocksHistoryPrice().put("LP", data2);


        validationService.getStocksCurrentPrice().put("FYBER", 95.0);
        validationService.getStocksCurrentPrice().put("APPLE", 98.0);
        validationService.getStocksCurrentPrice().put("GOOGLE", 100.0);
        validationService.getStocksCurrentPrice().put("TEVA", 150.0);
        validationService.getStocksCurrentPrice().put("LP", 170.0);
    }

    public Map<Integer, Map<String, StockState>> getAllUsersName() {
        return users;
    }

    public Map<String, StockState> getUser(int id) {
        return users.get(id);
    }

    public String updateUser(@Valid Map<String, StockState> userRequest, int id) {
        String ans = null;


        for (Map.Entry<String, StockState> item : userRequest.entrySet()) {
            StockState stockState = item.getValue();
            if (stockState.isSold() && users.get(id).get(stockState.getStock().getName()).getNumberOfStocks() < stockState.getNumberOfStocks()) {
                return "can't sell stock more then you have or stocks dose not exist check stock : "+ stockState.getStock().getName()+" details";
            }
        }

        for (Map.Entry<String, StockState> item : userRequest.entrySet()) {
            StockState stockState = item.getValue();
            if (users.get(id).get(stockState.getStock().getName()) != null) {
                if (stockState.isSold()) {
                    if (stockState.getNumberOfStocks() > users.get(id).get(stockState.getStock().getName()).getNumberOfStocks()) {
                        return "can't sell stocks more then you have";
                    } else {
                        Stock tempStock = new Stock(stockState.getStock().getName());
                        int numberOfStocks = users.get(id).get(stockState.getStock().getName()).getNumberOfStocks() - stockState.getNumberOfStocks();
                        //delete all stocks with 0 amount
                        if (numberOfStocks == 0) {
                            users.get(id).remove(stockState.getStock().getName());
                        } else {
                            StockState tempStockState = new StockState(tempStock, numberOfStocks, false);
                            users.get(id).put(stockState.getStock().getName(), tempStockState);
                        }
                    }
                } else {
                    Stock tempStock = new Stock(stockState.getStock().getName());
                    StockState tempStockState = new StockState(tempStock, users.get(id).get(stockState.getStock().getName()).getNumberOfStocks() + stockState.getNumberOfStocks(), false);

                    users.get(id).put(stockState.getStock().getName(), tempStockState);
                }

            } else {

                users.get(id).put(stockState.getStock().getName(), stockState);
            }
        }

        return ans;
    }

    public double getPortfolio(int id) {
        double portfolio = 0;
        for (Map.Entry<String, StockState> item : users.get(id).entrySet()) {
            StockState stockState = item.getValue();
            if (validationService.getStocksHistoryPrice().get(stockState.getStock().getName()) != null) {
                portfolio += stockState.getNumberOfStocks() * validationService.getStocksCurrentPrice().get(stockState.getStock().getName());
            } else {
                portfolio = 0;
                return portfolio;
            }
        }
        return portfolio;
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

    @NotNull
    @Size(min = 1, max = 3, message = "Enter recommendations number between 1-3 ")
    public String buyingRecommendations(int id, int type) {
        switch (type) {
            case 1:
                return "An already owned stock, who raised the most in value during the last 7 days is : " + getTheMostRaisedStockForLast7Days(id);
            case 2:
                return "Most stable - An already owned stock, with least value fluctuation during the last 7 days is : " + getTheMostStableStock(id);
            case 3:
                return "A not already owned stock, whose current value is the highest among all stocks is : " + getStockWithHighestValue(id);
            default:
                return "NOT A VALID OPTION";
        }
    }

    private String getStockWithHighestValue(int id) {
        String mostHighestStockName="";
        double maxCurrentValue = 0;
        for (Map.Entry<String, Double> stock : validationService.getStocksCurrentPrice().entrySet()) {
            String StockName = stock.getKey();
            double stockValue = stock.getValue();
            if (!isStockExisting(StockName,id)){
                if(stockValue > maxCurrentValue){
                    maxCurrentValue = stockValue;
                    mostHighestStockName=StockName;
                }
            }
        }
        return  mostHighestStockName;
    }

    private boolean isStockExisting(String stockName, int id) {
        return  users.get(id).get(stockName) != null;
    }

    private String getTheMostStableStock(int id) {
        String mostStableStock = "";
        double minMovement = Double.MAX_VALUE;

        for (Map.Entry<String, StockState> item : users.get(id).entrySet()) {
            StockState stockState = item.getValue();
            double movement = checkMomentInStock(stockState.getStock().getName());
            if (movement < minMovement) {
                minMovement = movement;
                mostStableStock = stockState.getStock().getName();
            }
        }
        return mostStableStock;
    }

    private String getTheMostRaisedStockForLast7Days(int id) {
        String bestStock = "";
        double maxProfit=0;

        for (Map.Entry<String, StockState> item : users.get(id).entrySet()) {
            StockState stockState = item.getValue();
            double profit = validationService.getStocksHistoryPrice().get(stockState.getStock().getName()).get(6) - validationService.getStocksHistoryPrice().get(stockState.getStock().getName()).get(0);
            if (profit > maxProfit){
                maxProfit = profit;
                bestStock = stockState.getStock().getName();
            }
        }
        return bestStock;
    }

    private double checkMomentInStock(String stockName){
        double max = 0;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < validationService.getStocksHistoryPrice().get(stockName).size() ; i++) {
            max = Math.max(max,validationService.getStocksHistoryPrice().get(stockName).get(i));
            min = Math.min(min,validationService.getStocksHistoryPrice().get(stockName).get(i));
        }

        return max-min;
    }

}
