package com.hexagonstore.cash.database.method;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.database.util.Utils;
import lombok.val;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave extends BukkitRunnable {

    private final DataManager dataManager;

    public AutoSave(CashSpigot main, DataManager dataManager) {
        this.dataManager = dataManager;

        runTaskTimerAsynchronously(main, 20L * 60 * 30, 20L * 60 * 30);
    }

    @Override
    public void run() {
        Utils.debug(Utils.LogType.DEBUG, "Iniciando auto save");
        val before = System.currentTimeMillis();
        val i = dataManager.saveCached(false);
        val now = System.currentTimeMillis();
        val total = now - before;
        Utils.debug(Utils.LogType.INFO, "Auto completo, salvo " + i + " objetos em " + total + "ms.");
    }

}