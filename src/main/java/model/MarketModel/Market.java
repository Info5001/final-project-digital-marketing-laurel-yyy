/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;

import model.ProductManagement.SolutionOffer;

/**
 *
 * @author kal bugrara
 */
public class Market {
  ArrayList<SolutionOffer> so;
  ArrayList<MarketChannelAssignment> channels;
  ArrayList<String> characteristics;
  ArrayList<Market> submarkets;
  String name;
  int size;

  public Market(String s) {
    name = s;
    so = new ArrayList<SolutionOffer>();
    characteristics = new ArrayList<String>();
    characteristics.add(s);
    channels = new ArrayList<MarketChannelAssignment>();
    submarkets = new ArrayList<Market>();
  }

  public void addChannel(Channel c) {
    channels.add(new MarketChannelAssignment(this, c));
  }

  public String getName() {
    return name;
  }

  public ArrayList<MarketChannelAssignment> getChannels() {
    return channels;
  }
}
