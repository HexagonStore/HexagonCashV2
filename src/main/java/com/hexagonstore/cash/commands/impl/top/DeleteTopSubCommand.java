package com.hexagonstore.cash.commands.impl.top;

import com.hexagonstore.cash.commands.util.top.ITopSubCommand;
import com.hexagonstore.cash.manager.TopManager;
import com.hexagonstore.cash.utils.EC_Config;
import com.hexagonstore.cash.utils.Language;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteTopSubCommand implements ITopSubCommand {

    public void execute(TopManager topManager, EC_Config config, CommandSender s, String[] a) {
        if(!(s instanceof Player)) {
            s.sendMessage(Language.get("no_console"));
            return;
        }

        val player = (Player) s;
        if (!player.hasPermission(config.getString("cmd permission.admin"))) {
            player.sendMessage(Language.get("no_permission"));
            return;
        }
        if (a.length < 3) {
            player.sendMessage(Language.get("no_args.deltop"));
            return;
        }


        try {
            val pos = Integer.parseInt(a[2]);
            if (pos <= 0) {
                player.sendMessage(Language.get("number_must_higher"));
                return;
            }
            if(pos > topManager.getMax()) {
                player.sendMessage(Language.get("top_number_max").replace("{max}", ""+topManager.getMax()));
                return;
            }

            if(!topManager.hasTop(pos) || !topManager.isSpawned(pos)) {
                player.sendMessage(Language.get("no_has_top").replace("{pos}", ""+pos));
                return;
            }

            topManager.deleteTopAs(pos);
            player.sendMessage(Language.get("top_delete").replace("{pos}", ""+pos));
        } catch (NumberFormatException ignored) {
            player.sendMessage(Language.get("invalid_number"));
        }
    }
}
