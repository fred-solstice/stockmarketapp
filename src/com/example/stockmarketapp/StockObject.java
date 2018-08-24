package com.example.stockmarketapp;

import java.sql.Timestamp;

public class StockObject {

    private String    symbol;
    private float     lowestPrice;
    private float     highestPrice;
    private float     totalVolume;
    private float     closePrice;
    private Timestamp date;

    public StockObject(String symbol, float price, int volume, Timestamp date){


    }
}
