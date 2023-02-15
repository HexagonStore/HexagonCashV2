package com.hexagonstore.cash.commands.impl.top;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.manager.TopManager;
import com.hexagonstore.cash.utils.EC_Config;
import com.hexagonstore.cash.utils.Language;
import com.hexagonstore.cash.commands.util.ISubCommand;
import com.hexagonstore.cash.database.util.Utils;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopSubCommand implements ISubCommand {

    private SetTopSubCommand setTopSubCommand;
    private DeleteTopSubCommand deleteTopSubCommand;

    public TopSubCommand() {
        setTopSubCommand = new SetTopSubCommand();
        deleteTopSubCommand = new DeleteTopSubCommand();
    }

    @Override
    public void execute(DataManager dataManager, HexagonCashAPI cashAPI, EC_Config config, CommandSender s, String[] a) {
        if (s.hasPermission(config.getString("cmd permission.admin")) || config.getString("cmd permission.admin").isEmpty()) {
            if(a.length < 2) {
                Language.showHelp(s);
                return;
            }
            switch (a[1].toLowerCase()) {
                case "set":
                case "setar":
                case "spawn":
                case "spawnar":
                case "add":
                case "adicionar":
                    if (!(s instanceof Player)) {
                        s.sendMessage(Language.get("no_console"));
                        return;
                    }
                    setTopSubCommand.execute(CashSpigot.getPlugin().getTopManager(), config, s, a);
                    break;
                case "unset":
                case "unsetar":
                case "dessetar":
                case "delete":
                case "del":
                case "remove":
                case "deletar":
                case "unspawn":
                case "unspawnar":
                    deleteTopSubCommand.execute(CashSpigot.getPlugin().getTopManager(), config, s, a);
                    break;
                default:
                case "help":
                case "ajuda":
                    if (s.hasPermission(config.getString("cmd permission.normal")) || config.getString("cmd permission.normal").isEmpty()) {
                        Language.showHelp(s);
                    } else {
                        s.sendMessage(Language.get("no_permission"));
                    }
                    break;
            }
        }else {
            s.sendMessage(Language.get("no_permission"));
        }
    }
}
