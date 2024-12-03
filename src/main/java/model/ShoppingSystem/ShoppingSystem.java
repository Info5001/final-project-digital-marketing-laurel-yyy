package model.ShoppingSystem;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import model.Business.*;
import model.OrderManagement.*;
import model.MarketModel.*;
import model.CustomerManagement.*;
import model.Supplier.*;
import model.ProductManagement.*;

public class ShoppingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private Business business;
    private Market selectedMarket;
    private Channel selectedChannel;
    private CustomerProfile customerProfile;
    private ArrayList<OrderItem> shoppingCart = new ArrayList<>();

    public ShoppingSystem(Business business) {
        this.business = business;
    }

    public void start() {

        if (!login()) {
            return;
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Shopping System Menu ===");
            System.out.println("1. Select Market and Channel");
            System.out.println("2. View Advertisement");
            System.out.println("3. Browse Products");
            System.out.println("4. View Shopping Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1: selectMarketAndChannel(); break;
                case 2: showAdvertisement(); break;
                case 3: browseProducts(); break;
                case 4: viewShoppingCart(); break;
                case 5: checkout(); break;
                case 6: exit = true; break;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private boolean login() {
        System.out.print("\nEnter customer ID: ");
        String customerId = scanner.nextLine();
        CustomerProfile customer = business.getCustomerDirectory().findCustomer(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
            return false;
        }
        
        customerProfile = customer;
        System.out.println("Login Succeed!");
        return true;
    }

    private void selectMarketAndChannel() {

        System.out.println("\n=== Available Markets ===");
        ArrayList<Market> markets = business.getMarketCatalog().getMarkets();
        for (int i = 0; i < markets.size(); i++) {
            System.out.println((i+1) + ". " + markets.get(i).getName());
        }
        System.out.print("Select market (1-" + markets.size() + "): ");
        int marketChoice = scanner.nextInt() - 1;
        selectedMarket = markets.get(marketChoice);

        System.out.println("\n=== Available Channels ===");
        ArrayList<MarketChannelAssignment> channels = selectedMarket.getChannels();
        for (int i = 0; i < channels.size(); i++) {
            System.out.println((i+1) + ". " + channels.get(i).getChannel().getName());
        }
        System.out.print("Select channel (1-" + channels.size() + "): ");
        int channelChoice = scanner.nextInt() - 1;
        selectedChannel = channels.get(channelChoice).getChannel();

        System.out.println("Selected: " + selectedMarket.getName() + " through " + selectedChannel.getName());
    }

    private void showAdvertisement() {
        if (selectedMarket == null || selectedChannel == null) {
            System.out.println("Please select market and channel first!");
            return;
        }

        System.out.println("\n=== Advertisement ===");
        System.out.println("Special offers for " + selectedMarket.getName() + 
                          " market through " + selectedChannel.getName() + "!");
        Integer adExpense = null;
        for (MarketChannelAssignment mca : selectedMarket.getChannels()) {
            if (mca.getChannel() == selectedChannel) {
                adExpense = mca.getAdExpense();
                break;
            }
        }
    
        if (adExpense == null) {
            System.out.println("No advertising data available.");
            return;
        }
    
        ArrayList<Product> products = new ArrayList<>();
        for (Supplier supplier : business.getSupplierDirectory().getSupplierList()) {
            products.addAll(supplier.getProductCatalog().getProductList());
        }
        Random random = new Random();
        int numProductsToShow = Math.max(1, adExpense / 300);
        
        System.out.println("\nFeatured Products:");
        for (int i = 0; i < Math.min(numProductsToShow, products.size()); i++) {
            int randomIndex = random.nextInt(products.size());
            Product p = products.get(randomIndex);
            System.out.println((i+1) + ". " + p.getName() + 
                            " - Special Price: $" + p.getMarketTargetPrice(selectedMarket.getName()));
        }
    }

    private void browseProducts() {
        if (selectedMarket == null) {
            System.out.println("Please select market first!");
            return;
        }
    
        System.out.println("\n=== Available Products in " + selectedMarket.getName() + " ===");
        ArrayList<Supplier> suppliers = business.getSupplierDirectory().getSupplierList();
        ArrayList<Product> availableProducts = new ArrayList<>();
        
        int index = 1;
        for (Supplier supplier : suppliers) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                int[] marketPrices = product.getMarketPrice(selectedMarket.getName());
                if (marketPrices != null) { 
                    System.out.println(index + ". " + product.getName() + 
                                     " - Price: $" + product.getMarketTargetPrice(selectedMarket.getName()));
                    availableProducts.add(product);
                    index++;
                }
            }
        }
    
        if (availableProducts.isEmpty()) {
            System.out.println("No products available in this market.");
            return;
        }
    
        while (true) {
            System.out.print("\nEnter product number to add to cart (0 to finish): ");
            int choice = scanner.nextInt();
            if (choice == 0) break;
            if (choice > 0 && choice <= availableProducts.size()) {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                Product selectedProduct = availableProducts.get(choice - 1);
                Order tempOrder = new Order(customerProfile);
                OrderItem item = new OrderItem(selectedProduct, 
                                            selectedProduct.getMarketTargetPrice(selectedMarket.getName()),
                                            quantity,
                                            tempOrder);
                shoppingCart.add(item);
                System.out.println("Added to cart!");
            }
        }
    }

    private void viewShoppingCart() {
        System.out.println("\n=== Shopping Cart ===");
        if (shoppingCart.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }

        int total = 0;
        for (OrderItem item : shoppingCart) {
            System.out.println(item.getSelectedProduct().getName() + 
                             " x" + item.getQuantity() + 
                             " - $" + item.getOrderItemTotal());
            total += item.getOrderItemTotal();
        }
        System.out.println("Total: $" + total);
    }

    private void checkout() {
        if (shoppingCart.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }

        Order order = business.getMasterOrderList().newOrder(customerProfile);
        
        MarketChannelAssignment mca = null;
        for (MarketChannelAssignment assignment : selectedMarket.getChannels()) {
            if (assignment.getChannel() == selectedChannel) {
                mca = assignment;
                break;
            }
        }
        order.setMarketChannelAssignment(mca);

        for (OrderItem item : shoppingCart) {
            order.newOrderItem(item.getSelectedProduct(), 
                             item.getActualPrice(), 
                             item.getQuantity());
        }

        order.Submit();
        
        System.out.println("\n=== Order Confirmation ===");
        System.out.println("Total Amount: $" + order.getOrderTotal());
        System.out.println("Thank you for your purchase!");

        shoppingCart.clear();
    }
}