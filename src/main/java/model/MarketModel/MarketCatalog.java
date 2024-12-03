/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;

import model.Business.Business;

/**
 *
 * @author kal bugrara
 */
public class MarketCatalog {

    ArrayList<Market> markets;
    Business business;
    public MarketCatalog(Business b) {
        markets = new ArrayList<>();
        business = b;
    }

    public Market newMarket(String name) {
        Market market = new Market(name);
        markets.add(market);
        return market;
    }

    public ArrayList<Market> getMarkets() {
        return markets;
    }

}
