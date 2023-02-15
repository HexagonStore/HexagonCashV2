package com.hexagonstore.cash.registry;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.database.datamanager.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventRegistry implements Listener {

    protected HexagonCashAPI cashAPI;
    protected CashSpigot main;
    protected DataManager dataManager;

    public EventRegistry(HexagonCashAPI cashAPI, CashSpigot main, DataManager dataManager) {
        this.cashAPI = cashAPI;
        this.main = main;
        this.dataManager = dataManager;

        Bukkit.getPluginManager().registerEvents(this, main);
    }
}
