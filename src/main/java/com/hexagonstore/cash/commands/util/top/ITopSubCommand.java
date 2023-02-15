package com.hexagonstore.cash.commands.util.top;

import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.manager.TopManager;
import com.hexagonstore.cash.utils.EC_Config;
import org.bukkit.command.CommandSender;

public interface ITopSubCommand {

    public void execute(TopManager topManager, EC_Config config, CommandSender s, String[] a);
}
