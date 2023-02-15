package com.hexagonstore.cash.listeners;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.database.util.Utils;
import com.hexagonstore.cash.models.CashAccount;
import com.hexagonstore.cash.registry.EventRegistry;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent extends EventRegistry {

    public JoinEvent(HexagonCashAPI cashAPI, CashSpigot main, DataManager dataManager) {
        super(cashAPI, main, dataManager);
    }

    @EventHandler
    void evento(PlayerJoinEvent e) {
        Utils.async(() -> {
            val player = e.getPlayer();
            CashAccount.load(player, dataManager.USERS);
        });
    }
}
