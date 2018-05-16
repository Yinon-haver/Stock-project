package com.stock.api.database;

import com.stock.api.model.StockState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapDatabase {

    private static Map<Integer,ConcurrentHashMap<String,StockState> > MapUsers = new ConcurrentHashMap<>();;

    public static  Map<Integer,ConcurrentHashMap<String,StockState> > getUsers(){
        return MapUsers;
    }

}
