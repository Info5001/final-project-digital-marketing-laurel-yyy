/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

import com.github.javafaker.Faker;

import model.Business.Business;
import model.Business.ConfigureABusiness;
import model.MarketModel.*;
import model.ShoppingSystem.ShoppingSystem;
import model.UserAccountManagement.UserAccount;
import model.UserAccountManagement.UserAccountDirectory;;


/**
 *
 * @author kal bugrara
 */
public class DigitalMarketingApplication {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    // TODO - Digital Marketing
    /*
     * ## Part 1 - Populating the model
     * 
     * 1. Your company sells products to three different markets and uses four
     * channels. You can create your own names of the company, markets and channels.
     * 
     * 2. You sell more than thirty different products, pricing them differently for
     * different markets. You are also using different channels to reach your
     * customers.
     * 
     * 3. Every quarter your advertising agency gives you a breakdown of advertising
     * costs, for each market and by each channel. Link to advertising expense
     * table:
     * (https://northeastern-my.sharepoint.com/:i:/g/personal/
     * a_lelashvili_northeastern_edu/Ea1mOtQvG9pGqrSyVmxA_e4BFZx-LZCVa-nu4XLYW3h5Uw?
     * e=0YozCW)
     * 
     * 4. Autogenerate sales orders and randomly pick the following:
     * - Solution Offer
     * - Market
     * - Channel
     * - Price
     * 
     * ## Part 2 – Build reports
     * 
     * 1. Create Market profitability report. This report should show how same
     * product sold under different solution offers performed in different Markets.
     * You should show total sales revenues as well as advertising costs and profit.
     * 
     * 2. Create Channel profitability report. This report should show which channel
     * was easier to sell, required less advertising budget and still sold well.
     * 
     * 3. Advertising Efficiency. This report should do a breakdown of the
     * advertising budget for the solution offers sold in each market sold through
     * each channel. Create a way to allocate Advertising costs and show the results
     * in a table.
     * 
     * 4. Implement a user interaction where the user (with Admin role) can choose
     * from list of reports and see the report.
     * 
     * ## Part 3 - User Interaction
     * 
     * 1. Implement a user interaction through the terminal.
     * 2. The user can choose options from menus and pick its own choices.
     * 3. Implement the following features:
     * - User can identify (or pick) its profile with Market and Channel choices.
     * - User will see an advertisement based on the choices above.
     * - User can see list of available solution offers and prices and can choose
     * one or more to add to a shopping cart.
     * - User can complete the purchase and get order confirmation.
     * 
     * 4. Add any additional features you think will improve user experience
     */

    Business business = ConfigureABusiness.createABusinessAndLoadALotOfData("Amazon", 50, 10, 30,
        100,
        10);
    
    Scanner scanner = new Scanner(System.in);
        System.out.println("1. Admin Login");
        System.out.println("2. Customer Shopping");
        System.out.print("Choose option: ");
        
        int choice = scanner.nextInt();
        if (choice == 1) {
          UserAccountDirectory uad = business.getUserAccountDirectory();
          UserAccount userAccount = null;
          while(userAccount == null) {
              System.out.println("\n=== Login ===");
              System.out.print("Username: ");
              String username = scanner.nextLine();
              System.out.print("Password: ");
              String password = scanner.nextLine();
              
              userAccount = uad.AuthenticateUser(username, password);
              
              if(userAccount == null) {
                  System.out.println("Invalid username or password.");
                  continue;
              }
              
              if(!userAccount.getRole().equals("Admin")) {
                  System.out.println("Admin required.");
                  userAccount = null;
                  continue;
              }
            }
          MarketingReports.showReportMenu(business);
                  
        } else if (choice == 2) {
            ShoppingSystem shoppingSystem = new ShoppingSystem(business);
            shoppingSystem.start();
        }
          }
  }

