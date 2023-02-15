package com.hexagonstore.cash.models;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ShopItem {

    private String name;
    private ArrayList<String> commands;
    private ItemStack it;

    private double price;
    private int slot;

    public ShopItem(String name, ArrayList<String> commands, ItemStack it, double price, int slot) {
        this.name = name;
        this.commands = commands;
        this.it = it;
        this.price = price;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIt() {
        return it;
    }

    public void setIt(ItemStack it) {
        this.it = it;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }
}
