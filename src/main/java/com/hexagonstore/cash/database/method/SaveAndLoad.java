package com.hexagonstore.cash.database.method;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.database.datasource.HikariDataSource;
import com.hexagonstore.cash.database.datasource.MySQLDataSource;
import com.hexagonstore.cash.database.datasource.SQLiteDataSource;
import com.hexagonstore.cash.database.exception.DatabaseException;
import com.hexagonstore.cash.database.util.Utils;
import lombok.val;
import org.bukkit.Bukkit;

public class SaveAndLoad {

    public static void saveAll(CashSpigot main) {
        try {
            if (main.getAbstractDataSource() == null) return;
            main.getMainDataManager().saveCached(false);
            main.getAbstractDataSource().close();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public static boolean prepareDatabase(CashSpigot main) {
        try {
            val databaseType = main.getCfg().getString("Database.type");
            if (databaseType.equalsIgnoreCase("MYSQL_FAST")) {
                main.setAbstractDataSource(new HikariDataSource(main.getCfg().getString("Database.host"), main.getCfg().getString("Database.database"), main.getCfg().getString("Database.user"), main.getCfg().getString("Database.pass")));
            } else if (databaseType.equalsIgnoreCase("MYSQL")) {
                main.setAbstractDataSource(new MySQLDataSource(main.getCfg().getString("Database.host"), main.getCfg().getString("Database.database"), main.getCfg().getString("Database.user"), main.getCfg().getString("Database.pass")));
            } else {
                main.setAbstractDataSource(new SQLiteDataSource());
            }
            main.setMainDataManager(new DataManager(main.getAbstractDataSource()));

            return true;
        } catch (DatabaseException e) {
            Utils.debug(Utils.LogType.INFO, "Erro ao inicializar conexão com banco de dados.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(main);
        }
        return false;
    }

}