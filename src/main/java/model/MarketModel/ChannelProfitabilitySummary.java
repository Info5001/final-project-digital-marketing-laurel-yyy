package model.MarketModel;
import model.Business.*;
import model.OrderManagement.MasterOrderList;
import model.OrderManagement.Order;

public class ChannelProfitabilitySummary {
    private Channel channel;
    private int totalRevenue;
    private Integer adBudget;
    private int orderCount;
    
    public ChannelProfitabilitySummary(Channel channel, MasterOrderList masterOrderList) {
        this.channel = channel;
        calculateMetrics(masterOrderList);
    }
    
    private void calculateMetrics(MasterOrderList masterOrderList) {
        totalRevenue = 0;
        orderCount = 0;
        adBudget = 0;
        
        for(Order order : masterOrderList.getOrders()) {
            MarketChannelAssignment mca = order.getMarketChannelAssignment();
            if(mca != null && mca.getChannel() == channel) {
                totalRevenue += order.getOrderTotal();
                orderCount++;
                adBudget = mca.getAdExpense();
            }
        }
    }
    
    public double getEfficiencyRatio() {
        return adBudget != 0 ? (double)totalRevenue / adBudget : 0;
    }

    public String getChannelName() {
        return channel.getName();
    }

    public int getTotalRevenue() { return totalRevenue; }
    public Integer getAdBudget() { return adBudget; }
    public int getOrderCount() { return orderCount; }
}