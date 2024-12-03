package model.MarketModel;

import model.OrderManagement.MasterOrderList;
import model.OrderManagement.Order;

public class MarketProfitabilitySummary {
    private Market market;
    private int totalRevenue;
    private int totalAdvertisingCost;
    
    private MasterOrderList masterOrderList;
    
    public MarketProfitabilitySummary(Market market, MasterOrderList mol) {
        this.market = market;
        this.masterOrderList = mol;
        calculateRevenue();
        calculateAdvertisingCost();
    }
    
    private void calculateRevenue() {
        totalRevenue = 0;
        for(MarketChannelAssignment mca : market.getChannels()) {
            for(Order order : masterOrderList.getOrders()) {
                if(order.getMarketChannelAssignment() == mca) {
                    totalRevenue += order.getOrderTotal();
                }
            }
        }
    }

    
    private void calculateAdvertisingCost() {
        totalAdvertisingCost = 0;
        for(MarketChannelAssignment mca : market.getChannels()) {
            Integer expense = mca.getAdExpense();
            if(expense != null) {
                totalAdvertisingCost += expense;
            }
        }
    }
    
    public int getProfit() {
        return totalRevenue - totalAdvertisingCost;
    }
    
    public String getMarketName() {
        return market.getName();
    }
    
    public int getTotalRevenue() {
        return totalRevenue;
    }
    
    public int getTotalAdvertisingCost() {
        return totalAdvertisingCost;
    }
}
