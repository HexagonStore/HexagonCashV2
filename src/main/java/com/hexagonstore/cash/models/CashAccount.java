package com.hexagonstore.cash.models;

import com.hexagonstore.cash.database.Keyable;
import com.hexagonstore.cash.database.datamanager.CachedDataManager;
import com.hexagonstore.cash.database.util.Utils;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter @Setter @AllArgsConstructor @RequiredArgsConstructor
public class CashAccount implements Keyable<String> {

    private final String playerName;
    private double cash;

    public String getKey() { return playerName; }


    public static void loadAll(CachedDataManager<String, CashAccount> dao) {
        Utils.measureTime(() -> {
            int i = 0;
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                if (dao.isCached(player.getName())) continue;
                load(player, dao);
                i++;
            }
            return "Carregado " + i + " objetos em {time}";
        });
    }

    public static void load(OfflinePlayer player, CachedDataManager<String, CashAccount> dao) {
        if (dao.exists(player.getName())) {
            val account = dao.find(player.getName());
            dao.cache(account);
        } else {
            CashAccount account = new CashAccount(player.getName(), 0);
            dao.cache(account);
        }
    }
}
