/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import model.MarketModel.*;
import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class SolutionOfferCatalog {
    ArrayList<SolutionOffer> solutionoffers;

    public SolutionOfferCatalog() {
        solutionoffers = new ArrayList<>();
    }

    public void addSolutionOffer(SolutionOffer offer) {
        solutionoffers.add(offer);
    }

    public ArrayList<SolutionOffer> getSolutionOffers() {
        return solutionoffers;
    }

    public ArrayList<SolutionOffer> getSolutionOffersForMarketChannel(MarketChannelAssignment mca) {
        ArrayList<SolutionOffer> offers = new ArrayList<>();
        for(SolutionOffer offer : solutionoffers) {
            if(offer.getMarketChannelAssignment() == mca) {
                offers.add(offer);
            }
        }
        return offers;
    }
}