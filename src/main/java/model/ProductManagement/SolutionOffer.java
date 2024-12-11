/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;

import model.MarketModel.MarketChannelAssignment;

/**
 *
 * @author kal bugrara
 */
public class SolutionOffer {
    ArrayList<Product> products;
    int price;
    MarketChannelAssignment marketChannelComb;

    public SolutionOffer(MarketChannelAssignment m) {
        marketChannelComb = m;
        products = new ArrayList<Product>();
    }

    public void addProduct(Product p) {
        products.add(p);
    }

    public void setPrice(int p) {
        price = p;
    }

    public int getPrice() {
        return price;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public MarketChannelAssignment getMarketChannelAssignment() {
        return marketChannelComb;
    }
}