package model.MarketModel;
import model.OrderManagement.MasterOrderList;
import model.OrderManagement.Order;

public class AdvertisingEfficiencySummary {
    private Market market;
    private Channel channel;
    private int totalRevenue;
    private Integer adBudget;
    private int numberOfOrders;
    
    public AdvertisingEfficiencySummary(Market market, Channel channel, MasterOrderList mol) {
        this.market = market;
        this.channel = channel;
        calculateMetrics(mol);
    }
    
    private void calculateMetrics(MasterOrderList mol) {
        totalRevenue = 0;
        numberOfOrders = 0;
        
        for(Order order : mol.getOrders()) {
            MarketChannelAssignment mca = order.getMarketChannelAssignment();
            if(mca != null && mca.getMarket() == market && mca.getChannel() == channel) {
                totalRevenue += order.getOrderTotal();
                numberOfOrders++;
                adBudget = mca.getAdExpense();
            }
        }
    }
    
    public double getROI() {
        return adBudget != 0 ? (double)totalRevenue / adBudget : 0;
    }
    
    public String getMarketName() { return market.getName(); }
    public String getChannelName() { return channel.getName(); }
    public int getTotalRevenue() { return totalRevenue; }
    public Integer getAdBudget() { return adBudget; }
    public int getNumberOfOrders() { return numberOfOrders; }
}