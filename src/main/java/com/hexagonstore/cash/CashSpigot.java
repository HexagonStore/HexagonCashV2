package com.hexagonstore.cash;

import com.hexagonstore.cash.listeners.DestroyAsEvent;
import com.hexagonstore.cash.listeners.QuitEvent;
import com.hexagonstore.cash.manager.TopManager;
import com.hexagonstore.cash.models.CashAccount;
import com.hexagonstore.cash.papi.CashExpansion;
import com.hexagonstore.cash.utils.EC_Config;
import com.hexagonstore.cash.commands.CashCommand;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.database.datasource.AbstractDataSource;
import com.hexagonstore.cash.database.method.AutoSave;
import com.hexagonstore.cash.database.method.SaveAndLoad;
import com.hexagonstore.cash.database.util.Utils;
import com.hexagonstore.cash.listeners.JoinEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CashSpigot extends JavaPlugin {

    private static CashSpigot plugin;

    private EC_Config cfg;
    private EC_Config shopConfig;
    private EC_Config topConfig;

    @Setter private AbstractDataSource abstractDataSource;
    @Setter private DataManager mainDataManager;

    private TopManager topManager;
    private HexagonCashAPI cashAPI;

    public static CashSpigot getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        register();
    }

    private void register() {
        cfg = new EC_Config(null, "config.yml", false);
        topConfig = new EC_Config(null, "top.yml", false);

        Utils.DEBUGGING = cfg.getBoolean("Database.Debug");
        if (!SaveAndLoad.prepareDatabase(this)) return;
        CashAccount.loadAll(this.mainDataManager.USERS);

        cashAPI = new HexagonCashAPI(this);

        topManager = new TopManager(topConfig, cashAPI, this);

        new JoinEvent(cashAPI, this, mainDataManager);
        new QuitEvent(cashAPI, this, mainDataManager);
        new DestroyAsEvent(cashAPI, this, mainDataManager);

        new CashCommand(this, mainDataManager, cashAPI);

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().warning("PlaceholderAPI não encontrado, hook desativado.");
        }else {
            new CashExpansion().register();
            getLogger().info("Placeholder encontrado, hook ativado!");
        }
        new AutoSave(this, mainDataManager);
    }

    @Override
    public void onDisable() {
        topManager.unload();
        SaveAndLoad.saveAll(this);
    }
}
