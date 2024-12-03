/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;

import model.Business.Business;

/**
 *
 * @author kal bugrara
 */
public class ChannelCatalog {
    Business business;
    ArrayList<Channel> channels;

    public ChannelCatalog(Business b) {
        business = b;
        channels = new ArrayList();
    }

    public Channel newChannel(String name) {
        Channel channel = new Channel(name);
        channels.add(channel);
        return channel;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }
};
