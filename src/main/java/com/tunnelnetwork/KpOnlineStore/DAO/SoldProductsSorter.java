package com.tunnelnetwork.KpOnlineStore.DAO;

import java.util.Comparator;

import com.tunnelnetwork.KpOnlineStore.Models.Product;
 
public class SoldProductsSorter implements Comparator<Product> 
{
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getNumberOfSold().compareTo(p1.getNumberOfSold());
    }
}