package model.MarketModel;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.Business.*;
import model.OrderManagement.*;
public class MarketingReports {

    public static void printMarketProfitabilityReport(Business business) {
        System.out.println("\n=== Market Profitability Report ===");
        System.out.printf("%-15s %-15s %-15s %-15s\n", 
            "Market", "Revenue", "Ad Cost", "Profit");
        System.out.println("------------------------------------------------");
        
        int totalRevenue = 0;
        int totalAdCost = 0;
        int totalProfit = 0;
        
        for(Market market : business.getMarketCatalog().getMarkets()) {
            MarketProfitabilitySummary summary = new MarketProfitabilitySummary(market, business.getMasterOrderList());
            
            System.out.printf("%-15s $%-14d $%-14d $%-14d\n",
                summary.getMarketName(),
                summary.getTotalRevenue(),
                summary.getTotalAdvertisingCost(),
                summary.getProfit()
            );
            
            totalRevenue += summary.getTotalRevenue();
            totalAdCost += summary.getTotalAdvertisingCost();
            totalProfit += summary.getProfit();
        }
        
        System.out.println("------------------------------------------------");
        System.out.printf("%-15s $%-14d $%-14d $%-14d\n",
            "TOTAL", totalRevenue, totalAdCost, totalProfit);
        System.out.println("================================================");
    }

    public static void printChannelProfitabilityReport(Business business) {
        System.out.println("\n=== Channel Profitability Report ===");
        System.out.printf("%-15s %-12s %-12s %-12s %-12s\n", 
            "Channel", "Revenue", "Ad Budget", "Orders", "Efficiency");
        System.out.println("-----------------------------------------------------------");
        
        MasterOrderList mol = business.getMasterOrderList();
        for(Channel channel : business.getChannelCatalog().getChannels()) {
            ChannelProfitabilitySummary summary = new ChannelProfitabilitySummary(channel, mol);
            System.out.printf("%-15s $%-11d $%-11d %-12d %.2f\n",
                summary.getChannelName(),
                summary.getTotalRevenue(),
                summary.getAdBudget(),
                summary.getOrderCount(),
                summary.getEfficiencyRatio()
            );
        }
        System.out.println("-----------------------------------------------------------");
    }
    
    public static void printAdvertisingEfficiencyReport(Business business) {
        System.out.println("\n=== Advertising Efficiency Report ===");
        System.out.printf("%-12s %-12s %-12s %-12s %-12s %-12s\n", 
            "Market", "Channel", "Revenue", "Ad Budget", "Orders", "ROI");
        System.out.println("------------------------------------------------------------------------");
        
        MasterOrderList mol = business.getMasterOrderList();
        for(Market market : business.getMarketCatalog().getMarkets()) {
            for(MarketChannelAssignment mca : market.getChannels()) {
                AdvertisingEfficiencySummary summary = new AdvertisingEfficiencySummary(
                    market, mca.getChannel(), mol);
                
                System.out.printf("%-12s %-12s $%-11d $%-11d %-12d %.2f\n",
                    summary.getMarketName(),
                    summary.getChannelName(),
                    summary.getTotalRevenue(),
                    summary.getAdBudget(),
                    summary.getNumberOfOrders(),
                    summary.getROI()
                );
            }
        }
        System.out.println("------------------------------------------------------------------------");
    }

    public static void showReportMenu(Business business) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Marketing Reports Menu ===");
            System.out.println("1. Market Profitability Report");
            System.out.println("2. Channel Profitability Report");
            System.out.println("3. Advertising Efficiency Report");
            System.out.println("4. Logout");
            System.out.print("\nPlease enter your choice (1-4): ");

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        printMarketProfitabilityReport(business);
                        break;
                    case 2:
                        printChannelProfitabilityReport(business);
                        break;
                    case 3:
                        printAdvertisingEfficiencyReport(business);
                        break;
                    case 4:
                        exit = true;
                        System.out.println("\nLogged out successfully.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                if (!exit) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine(); 
                    scanner.nextLine(); 
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
            }
        }
    }


}