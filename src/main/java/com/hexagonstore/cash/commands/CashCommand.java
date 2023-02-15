package com.hexagonstore.cash.commands;

import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.commands.impl.*;
import com.hexagonstore.cash.commands.impl.top.TopSubCommand;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.registry.CommandRegistry;
import com.hexagonstore.cash.utils.Language;
import com.hexagonstore.cash.utils.NumberFormatter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand extends CommandRegistry {

    private AddSubCommand addSubCommand;
    private RemoveSubCommand removeSubCommand;
    private SetSubCommand setSubCommand;
    private TopSubCommand topSubCommand;
    public CashCommand(CashSpigot main, DataManager dataManager, HexagonCashAPI cashAPI) {
        super(main, dataManager, cashAPI);

        this.addSubCommand = new AddSubCommand();
        this.removeSubCommand = new RemoveSubCommand();
        this.setSubCommand = new SetSubCommand();
        this.topSubCommand = new TopSubCommand();

        main.getCommand("cash").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lb, String[] a) {
        if (a.length == 0) {
            if (!(s instanceof Player)) {
                s.sendMessage(Language.get("no_console"));
                return true;
            }

            val player = (Player) s;
            if (player.hasPermission(main.getCfg().getString("cmd permission.normal")) || main.getCfg().getString("cmd permission.normal").isEmpty()) {
                val cash = dataManager.USERS.getCached(player.getName()).getCash();
                s.sendMessage(Language.get("cash").replace("{cash}", NumberFormatter.formatNumber(cash)));
            } else {
                s.sendMessage(Language.get("no_permission"));
            }
            return true;
        }
        if (a.length >= 1) {

            val player = (Player) s;
            val target = Bukkit.getOfflinePlayer(a[0]);
            if(!target.hasPlayedBefore()) {
                switch (a[0].toLowerCase()) {

                    case "add":
                    case "adicionar":
                        addSubCommand.execute(dataManager, cashAPI, main.getCfg(), s, a);
                        break;
                    case "remove":
                    case "remover":
                        removeSubCommand.execute(dataManager, cashAPI, main.getCfg(), s, a);
                        break;
                    case "set":
                    case "setar":
                        setSubCommand.execute(dataManager, cashAPI, main.getCfg(), s, a);
                        break;
                    case "top":
                        topSubCommand.execute(dataManager, cashAPI, main.getCfg(), s, a);
                        break;
                    default:
                    case "help":
                    case "ajuda":
                        if (player.hasPermission(main.getCfg().getString("cmd permission.normal")) || main.getCfg().getString("cmd permission.normal").isEmpty()) {
                            Language.showHelp(s);
                        } else {
                            s.sendMessage(Language.get("no_permission"));
                        }
                        break;
                }
                return true;
            }
            if (player.hasPermission(main.getCfg().getString("cmd permission.normal")) || main.getCfg().getString("cmd permission.normal").isEmpty()) {
                val account = dataManager.USERS.getCached(target.getName());
                val cash = account.getCash();
                s.sendMessage(Language.get("cash_target").replace("{cash}", NumberFormatter.formatNumber(cash)).replace("{player}", player.getName()));
            } else {
                s.sendMessage(Language.get("no_permission"));
            }
            return true;
        }
        return true;
    }
}
