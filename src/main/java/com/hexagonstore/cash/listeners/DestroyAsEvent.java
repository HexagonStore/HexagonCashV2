package com.hexagonstore.cash.listeners;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.registry.EventRegistry;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class DestroyAsEvent extends EventRegistry {

    public DestroyAsEvent(HexagonCashAPI cashAPI, CashSpigot main, DataManager dataManager) {
        super(cashAPI, main, dataManager);
    }

    @EventHandler
    void evento(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof ArmorStand) {
            if (e.getEntity().hasMetadata(cashAPI.getMetaData())) {
                e.setCancelled(true);
            }
        }
    }
}
