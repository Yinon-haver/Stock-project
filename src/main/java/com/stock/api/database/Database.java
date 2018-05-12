package com.stock.api.database;

import com.stock.api.model.Stock;
import com.stock.api.model.StockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static Map<Integer,List<StockState>>  Users = new HashMap<>();

    public static  Map<Integer,List<StockState>> getUsers(){
        return Users;
    }

}
