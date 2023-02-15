package com.hexagonstore.cash.utils;

import com.hexagonstore.cash.CashSpigot;
import org.bukkit.command.CommandSender;

public class Language {

    static EC_Config config;

    static {
        config = CashSpigot.getPlugin().getCfg();
    }

    public static void showHelp(CommandSender s) {
        String helpType = "normal";
        if(s.hasPermission(config.getString("cmd permission.admin"))) {
            helpType = "admin";
        }else if(!s.hasPermission(config.getString("cmd permission.normal"))) {
            s.sendMessage(Language.get("no_permission"));
            return;
        }
        config.getStringList("Messages.help." + helpType).forEach(line -> {
            s.sendMessage(line.replace("&", "§"));
        });
    }

    public static String get(String path) {
        return config.getString("Messages." + path).replace("&", "§");
    }
}
