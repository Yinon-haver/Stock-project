package com.stock.api.controllers;
import com.stock.api.model.Stock;
import com.stock.api.model.StockState;
import com.stock.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;



@RestController
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String sayHi(){
        logger.info("UsersController.get to root page : Say Welcome ");
        userService.enterDataToDatabaseAndToValidation();
        return "======================\nWelcome to Stock API\n====================== \n\n"+
                "GET /users  : get all users in DB\n" +
                "GET /users/{id} : get specific user list of stocks \n" +
                "GET /users/portfolio/{id} : get specific user portfolio \n" +
                "GET /users/recommendation/{id}/{type} : get some recommendation about stocks , type value between 1-3 and id \n" +
                "POST /users : add new user , need to send list of stock\n" +
                "PUT /users/{id} : update user's  sold/bought , need to send list of stock \n"+
                "DELETE /users/{id} : delete user from DB  \n"+
                "GET /stocks : get all stocks name in DB \n"+
                "POST /stocks : add new stocks to DB , need to send stocks name ans list of last 7 days price \n"+
                "GET /StockHistoryPrice/{stockname} : get specific stock history price\n"+
                "GET /historyStocksPrices : get all history stocks prices ";
    }

    @RequestMapping(value = "/users" , method = RequestMethod.GET)
    public Map<Integer, List<StockState>> getAllUsers(){
        logger.info("UsersController.get all users and their stocks");
        return userService.getAllUsersName();
    }

    @RequestMapping(value = "/users/{id}" , method = RequestMethod.GET)
    public Object getStockState(@PathVariable int id){
        logger.info("UsersController.get user's stocks");
        if(userService.getUser(id) == null){
            return " User not exist";
        }
        return userService.getUser(id);
    }

    @RequestMapping(value = "/users/portfolio/{id}" , method = RequestMethod.GET)
    public String getUserPortfolio(@PathVariable int id){
        logger.info("UsersController.get user's Portfolio ");
       double ans = userService.getPortfolio(id);
       if (ans == 0){
           return "Some  stock does not exist in the system please add the stock first to the DB , you can use that API call -> POST /stocks : add new stock to DB , need to send stock name  ";
       }
        return " Your Portfolio is :   "+ ans;
    }

    @RequestMapping(value = "/users/recommendation/{id}/{type}" , method = RequestMethod.GET)
    public String getRecommendedStock(@PathVariable int id , @PathVariable int type){
        logger.info("UsersController.get recommendations");
        return userService.buyingRecommendations(id , type);
    }

    @RequestMapping(value = "/users" , method = RequestMethod.POST)
    public  String newClient(@Valid @RequestBody List<@Valid StockState>  userRequest ,BindingResult bindingResult){
        logger.info("UsersController.send stocks and create new user");
        if (bindingResult.hasFieldErrors()){
            return "error";
        }
        int ans = userService.addUser(userRequest);
       if(ans == 0 ){
           return "can't sell stock before you bought them or cant enter negative number of stocks ";
       }
        return "new user was created and id number is : "+ ans;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public String updateUser(@Valid @RequestBody List<StockState> userRequest, @PathVariable int id){
        logger.info("UsersController.send update to user's stocks");
        String ans =  userService.updateUser(userRequest,id);
        if (ans != null){
            return ans;
        }
         return "user with id "+ id+ " was update";
    }

    @RequestMapping(value = "/users/{id}",method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable int id){
        logger.info("UsersController.delete user ");
        userService.deleteUser(id);
        return "User "+ id +" was deleted from dataBase ";
    }
}
