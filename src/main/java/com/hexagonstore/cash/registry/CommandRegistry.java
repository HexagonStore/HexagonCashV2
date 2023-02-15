package com.hexagonstore.cash.registry;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.database.datamanager.DataManager;
import org.bukkit.command.CommandExecutor;

public abstract class CommandRegistry implements CommandExecutor {

    protected CashSpigot main;
    protected DataManager dataManager;
    protected HexagonCashAPI cashAPI;

    public CommandRegistry(CashSpigot main, DataManager dataManager, HexagonCashAPI cashAPI) {
        this.main = main;
        this.dataManager = dataManager;
        this.cashAPI = cashAPI;
    }
}
