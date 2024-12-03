/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

/**
 *
 * @author kal bugrara
 */
public class MarketChannelAssignment {

  Market market;
  Channel channel;
  Integer advertisingExpense;

  public MarketChannelAssignment(Market m, Channel c) {

    market = m;
    channel = c;

  }

  public void setAdvertisingExpense(Integer ep) {
      advertisingExpense = ep;
  }

  public Market getMarket() {
    return market;
  }

  public Channel getChannel() {
    return channel;
  }

  public Integer getAdExpense() {
    return advertisingExpense;
  }

}
