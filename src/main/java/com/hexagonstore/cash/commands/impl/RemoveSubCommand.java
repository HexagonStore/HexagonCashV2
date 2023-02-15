package com.hexagonstore.cash.commands.impl;

import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.commands.util.ISubCommand;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.utils.EC_Config;
import com.hexagonstore.cash.utils.Language;
import com.hexagonstore.cash.utils.NumberFormatter;
import lombok.val;
import org.bukkit.command.CommandSender;

public class RemoveSubCommand implements ISubCommand {
    @Override
    public void execute(DataManager dataManager, HexagonCashAPI cashAPI, EC_Config config, CommandSender s, String[] a) {
        if (s.hasPermission(config.getString("cmd permission.admin"))) {
            if (a.length < 3) {
                s.sendMessage(Language.get("no_args.remove"));
                return;
            }

            val nick = a[1];
            if (!dataManager.USERS.isCached(nick)) {
                s.sendMessage(Language.get("no_exists"));
                return;
            }
            try {
                val amount = NumberFormatter.parseString(a[2]);
                if (amount < 1) {
                    s.sendMessage(Language.get("invalid_number"));
                    return;
                }

                val account = dataManager.USERS.getCached(nick);
                cashAPI.removeCash(account, amount);
                s.sendMessage(Language.get("removed").replace("{cash}", NumberFormatter.formatNumber(amount)).replace("{player}", nick));
            } catch (Exception e) {
                s.sendMessage(Language.get("invalid_number"));
            }
        } else {
            s.sendMessage(Language.get("no_permission"));
        }
    }
}
