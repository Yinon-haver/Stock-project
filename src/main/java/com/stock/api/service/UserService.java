package com.stock.api.service;

import com.stock.api.database.Database;
import com.stock.api.model.Stock;
import com.stock.api.model.StockState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private ValidationService validationService;

    private Map<Integer,List<StockState>> users = Database.getUsers();

    private int ID = 10000;

    public int addUser(List<StockState> user){

        for (StockState stock : user) {
            if(stock.isSold()==true || stock.getNumberOfStocks() < 0){
                return 0;
            }
        }
        int id = ID++;
        users.put(id,user);
        return id;
    }

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

    public Map<Integer, List<StockState>> getAllUsersName(){
        return users;
    }

    public List<StockState> getUser(int id){
        return users.get(id);
    }

    public String updateUser(List<StockState> updateUser , int id) {
        String ans = null;

        //validate that the user cans sell when he have 0 stocks
        for (StockState stock :updateUser ) {
            if(stock.isSold()){
                for (StockState stockState :users.get(id)) {
                    if(stockState.getNumberOfStocks()==0){
                        return "can't sell stock when have 0 amount ";
                    }
                }
            }
        }

        for (StockState stock : updateUser) {
            if (isStockExisting(stock.getStock().getName(), id)) {
                if (stock.isSold()) {
                    users.get(id).forEach(p -> {
                        if (p.getStock().getName().equals(stock.getStock().getName()) && ( p.getNumberOfStocks() !=  0)) {
                            p.setNumberOfStocks(p.getNumberOfStocks() - stock.getNumberOfStocks());
                        }
                    });
                } else {
                    users.get(id).forEach(p -> {
                        if (p.getStock().getName().equals(stock.getStock().getName())) {
                            p.setNumberOfStocks(p.getNumberOfStocks() + stock.getNumberOfStocks());
                        }
                    });
                }

            }else {

                users.get(id).add(stock);
            }
        }

        //delete all stocks with 0 value
        for (int i = 0; i < users.get(id).size(); i++) {
            if (users.get(id).get(i).getNumberOfStocks() == 0 ){
                users.get(id).remove(i);
            }

        }


        return ans;
    }

    public double getPortfolio(int id){
        double portfolio=0 ;

            for (StockState stock :users.get(id) ) {
                if (validationService.getStocksCurrentPrice().get(stock.getStock().getName()) != null){
                    portfolio+= stock.getNumberOfStocks() * validationService.getStocksCurrentPrice().get(stock.getStock().getName()) ;

                }else {
                    portfolio=0;
                    return portfolio;

                }
            }

        return portfolio;
    }

    public void enterDataToDatabaseAndToValidation(){
        for (int i = 0; i <10 ; i++) {
            List<StockState> tempList = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                Stock name = new Stock("lp " + j);
                int numberOfStocks = (int)Math.pow(i,2);
                boolean sold=false;
                StockState tempStokState = new StockState(name,numberOfStocks,sold);
                tempList.add(tempStokState);
            }
            users.put((int)Math.pow(i,2),tempList);
        }

        List<Double> data = new ArrayList<>();
        data.add(100.0);
        data.add(101.0);
        data.add(102.0);
        data.add(103.0);
        data.add(104.0);
        data.add(105.0);
        data.add(106.0);
        validationService.getStocksHistoryPrice().put("FYBER",data);

        List<Double> data1 = new ArrayList<>();
        data1.add(100.0);
        data1.add(101.0);
        data1.add(102.0);
        data1.add(103.0);
        data1.add(104.0);
        data1.add(105.0);
        data1.add(106.0);
        validationService.getStocksHistoryPrice().put("APPLE",data1);

        List<Double> data2 = new ArrayList<>();
        data2.add(100.0);
        data2.add(106.0);
        data2.add(150.0);
        data2.add(55.0);
        data2.add(99.0);
        data2.add(100.0);
        data2.add(108.0);
        validationService.getStocksHistoryPrice().put("GOOGLE",data2);

        List<Double> data3 = new ArrayList<>();
        data3.add(160.0);
        data3.add(1086.0);
        data3.add(159.0);
        data3.add(55.0);
        data3.add(92.0);
        data3.add(169.0);
        data3.add(110.0);
        validationService.getStocksHistoryPrice().put("TEVA",data2);

        List<Double> data4 = new ArrayList<>();
        data4.add(160.0);
        data4.add(1086.0);
        data4.add(159.0);
        data4.add(55.0);
        data4.add(92.0);
        data4.add(169.0);
        data4.add(110.0);
        validationService.getStocksHistoryPrice().put("LP",data2);


        validationService.getStocksCurrentPrice().put("FYBER",95.0);
        validationService.getStocksCurrentPrice().put("APPLE",98.0);
        validationService.getStocksCurrentPrice().put("GOOGLE",100.0);
        validationService.getStocksCurrentPrice().put("TEVA",150.0);
        validationService.getStocksCurrentPrice().put("LP",170.0);
    }

    public String getStockWithHighestValue(int id){
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

    public String getTheMostRaisedStockForLast7Days(int id){
        String bestStock = "";
        double maxProfint=0;
        for (StockState stock: users.get(id)) {
                double profint = validationService.getStocksHistoryPrice().get(stock.getStock().getName()).get(6) - validationService.getStocksHistoryPrice().get(stock.getStock().getName()).get(0);
                if (profint > maxProfint){
                    maxProfint = profint;
                    bestStock = stock.getStock().getName();
                }
        }
        return bestStock;
    }

    public String getTheMostStableStock(int id){
        String mostStableStock="";
        double minMovement = Double.MAX_VALUE ;
        for (StockState stock : users.get(id)) {
            double movement = checkMovmentInStock(stock.getStock().getName());
            if (movement < minMovement){
                minMovement = movement;
                mostStableStock = stock.getStock().getName();
            }
        }
        return mostStableStock;
    }

    private boolean isStockExisting(String stockName , int id){
        boolean ans = false;
        for (StockState stock :users.get(id)) {
            if(stock.getStock().getName().equals(stockName)){
                return true;
            }
        }
        return ans;
    }

    private double checkMovmentInStock(String stockName){
        double max = 0;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < validationService.getStocksHistoryPrice().get(stockName).size() ; i++) {
             max = Math.max(max,validationService.getStocksHistoryPrice().get(stockName).get(i));
             min = Math.min(min,validationService.getStocksHistoryPrice().get(stockName).get(i));
        }

        return max-min;
    }

    public void deleteUser(int id){
        users.remove(id);
    }
    @NotNull
    @Size(min=1, max=3, message = "Enter recommendations number between 1-3 ")
    public String buyingRecommendations(int id , int type){
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


    public Map<String, List<Double>> getAllHistoryStockPrices() {
        return validationService.getStocksHistoryPrice();
    }

    public void updateStockCurrentPrice(String stockName) {

    }
}
