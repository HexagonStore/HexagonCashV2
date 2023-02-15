package com.hexagonstore.cash.listeners;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.registry.EventRegistry;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent extends EventRegistry {

    public QuitEvent(HexagonCashAPI cashAPI, CashSpigot main, DataManager dataManager) {
        super(cashAPI, main, dataManager);
    }

    @EventHandler
    void evento(PlayerQuitEvent e) {
        val player = e.getPlayer();
        if (!dataManager.USERS.isCached(player.getName())) return;
        val user = dataManager.USERS.getCached(player.getName());
        dataManager.USERS.insert(user, true);
        dataManager.USERS.uncache(player.getName());
    }
}
