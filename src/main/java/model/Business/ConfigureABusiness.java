/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Business;

import java.util.Random;
import java.util.ArrayList;

import com.github.javafaker.Faker;

import model.CustomerManagement.CustomerDirectory;
import model.CustomerManagement.CustomerProfile;
import model.MarketModel.MarketCatalog;
import model.MarketModel.MarketChannelAssignment;
import model.MarketModel.ChannelCatalog;
import model.MarketModel.Market;
import model.OrderManagement.MasterOrderList;
import model.OrderManagement.Order;
import model.ProductManagement.Product;
import model.ProductManagement.ProductCatalog;
import model.Supplier.Supplier;
import model.Supplier.SupplierDirectory;
import model.Personnel.*;
import model.UserAccountManagement.*;

/**
 *
 * @author kal bugrara
 */
public class ConfigureABusiness {

  static int upperPriceLimit = 50;
  static int lowerPriceLimit = 10;
  static int range = 5;
  static int productMaxQuantity = 5;

  public static Business createABusinessAndLoadALotOfData(String name, int supplierCount, int productCount,
      int customerCount, int orderCount, int itemCount) {
    Business business = new Business(name);

    initializeMarketAndChannels(business);
    setAdvertisingExpenses(business);
    initializeAdmin(business);
    
    // Add Suppliers +
    loadSuppliers(business, supplierCount);

    // Add Products +
    loadProducts(business, productCount);

    // Add Customers
    loadCustomers(business, customerCount);

    // Add Order
    loadOrders(business, orderCount, itemCount);

    return business;
  }
  public static void initializeAdmin(Business business) {

        PersonDirectory personDirectory = business.getPersonDirectory();
        EmployeeDirectory employeeDirectory = business.getEmployeeDirectory();
        UserAccountDirectory userAccountDirectory = business.getUserAccountDirectory();

        Person adminPerson = personDirectory.newPerson("AdminUser");
        EmployeeProfile adminProfile = employeeDirectory.newEmployeeProfile(adminPerson);
        userAccountDirectory.newUserAccount(adminProfile, "admin", "admin");
    }

  public static void loadSuppliers(Business b, int supplierCount) {
    Faker faker = new Faker();

    SupplierDirectory supplierDirectory = b.getSupplierDirectory();
    for (int index = 1; index <= supplierCount; index++) {
      supplierDirectory.newSupplier(faker.company().name());
    }
  }

  static void loadProducts(Business b, int productCount) {
    SupplierDirectory supplierDirectory = b.getSupplierDirectory();
    MarketCatalog marketCatalog = b.getMarketCatalog();
    for (Supplier supplier : supplierDirectory.getSupplierList()) {

      int randomProductNumber = getRandom(1, productCount);
      ProductCatalog productCatalog = supplier.getProductCatalog();

      for (int index = 1; index <= randomProductNumber; index++) {

        String productName = "Product #" + index + " from " + supplier.getName();
        int randomFloor = getRandom(lowerPriceLimit, lowerPriceLimit + range);
        int randomCeiling = getRandom(upperPriceLimit - range, upperPriceLimit);
        int randomTarget = getRandom(randomFloor, randomCeiling);

        Product product = productCatalog.newProduct(productName, randomFloor, randomCeiling, randomTarget);

        for (Market market : marketCatalog.getMarkets()) {
          for(MarketChannelAssignment mca : market.getChannels()) {
          int mcaFloor = (int)(randomFloor * (0.9 + getRandom(0, 20) / 100.0));
          int mcaCeiling = (int)(randomCeiling * (0.9 + getRandom(0, 20) / 100.0));
          int mcaTarget = (int)(randomTarget * (0.9 + getRandom(0, 20) / 100.0));
          
          product.setMarketPrice(mca, mcaFloor, mcaCeiling, mcaTarget);
        }
        }
      }
      }
    }
  
    static void setAdvertisingExpenses(Business b) {
      MarketCatalog marketCatalog = b.getMarketCatalog();
      
      for (Market market : marketCatalog.getMarkets()) {
          for (MarketChannelAssignment mca : market.getChannels()) {
              Integer amount = getRandom(500, 2000); 
              mca.setAdvertisingExpense(amount);
          }
      }
  }

  static int getRandom(int lower, int upper) {
    Random r = new Random();

    // nextInt(n) will return a number from zero to 'n'. Therefore e.g. if I want
    // numbers from 10 to 15
    // I will have result = 10 + nextInt(5)
    int randomInt = lower + r.nextInt(upper - lower);
    return randomInt;
  }

  static void loadCustomers(Business b, int customerCount) {
    CustomerDirectory customerDirectory = b.getCustomerDirectory();
    PersonDirectory personDirectory = b.getPersonDirectory();
    Person testPerson = personDirectory.newPerson("test1");
    customerDirectory.newCustomerProfile(testPerson);

    Faker faker = new Faker();

    for (int index = 1; index <= customerCount; index++) {
      Person newPerson = personDirectory.newPerson(faker.name().fullName());
      customerDirectory.newCustomerProfile(newPerson);
    }
  }

  static void loadOrders(Business b, int orderCount, int itemCount) {

    // reach out to masterOrderList
    MasterOrderList mol = b.getMasterOrderList();

    // pick a random customer (reach to customer directory)
    CustomerDirectory cd = b.getCustomerDirectory();
    SupplierDirectory sd = b.getSupplierDirectory();

    MarketCatalog mc = b.getMarketCatalog();
    ArrayList<Market> markets = mc.getMarkets();

    for (int index = 0; index < orderCount; index++) {

      CustomerProfile randomCustomer = cd.pickRandomCustomer();
      if (randomCustomer == null) {
        System.out.println("Cannot generate orders. No customers in the customer directory.");
        return;
      }

      Market randomMarket = markets.get(getRandom(0, markets.size()));
      ArrayList<MarketChannelAssignment> channelAssignments = randomMarket.getChannels();
      MarketChannelAssignment randomMCA = channelAssignments.get(getRandom(0, channelAssignments.size()));


      // create an order for that customer
      Order randomOrder = mol.newOrder(randomCustomer);
      randomOrder.setMarketChannelAssignment(randomMCA);

      // add order items
      // -- pick a supplier first (randomly)
      // -- pick a product (randomly)
      // -- actual price, quantity

      int randomItemCount = getRandom(1, itemCount);
      for (int itemIndex = 0; itemIndex < randomItemCount; itemIndex++) {

        Supplier randomSupplier = sd.pickRandomSupplier();
        if (randomSupplier == null) {
          System.out.println("Cannot generate orders. No supplier in the supplier directory.");
          return;
        }
        ProductCatalog pc = randomSupplier.getProductCatalog();
        Product randomProduct = pc.pickRandomProduct();
        if (randomProduct == null) {
          System.out.println("Cannot generate orders. No products in the product catalog.");
          return;
        }
        
        int[] marketPrice = randomProduct.getMarketPrice(randomMCA);
        int randomPrice = getRandom(marketPrice[0], marketPrice[1]);
        int randomQuantity = getRandom(1, productMaxQuantity);

        randomOrder.newOrderItem(randomProduct, randomPrice, randomQuantity);
      }
    }
    // Make sure order items are connected to the order

  }
  public static void initializeMarketAndChannels(Business business) {
    MarketCatalog marketCatalog = business.getMarketCatalog();
    Market america = marketCatalog.newMarket("America");
    Market europe = marketCatalog.newMarket("Europe");
    Market asia = marketCatalog.newMarket("Asia");

    ChannelCatalog channelCatalog = business.getChannelCatalog();
    model.MarketModel.Channel ecommerce = channelCatalog.newChannel("E-Commerce");
    model.MarketModel.Channel retail = channelCatalog.newChannel("Retail");
    model.MarketModel.Channel wholesale = channelCatalog.newChannel("Wholesale");
    model.MarketModel.Channel directsale = channelCatalog.newChannel("Directsale");

    for(Market market : marketCatalog.getMarkets()) {
        for(model.MarketModel.Channel channel : channelCatalog.getChannels()) {
            market.addChannel(channel);
        }
    }
}

}
