package com.hexagonstore.cash.commands.impl.top;

import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.commands.util.ISubCommand;
import com.hexagonstore.cash.commands.util.top.ITopSubCommand;
import com.hexagonstore.cash.database.datamanager.DataManager;
import com.hexagonstore.cash.manager.TopManager;
import com.hexagonstore.cash.utils.EC_Config;
import com.hexagonstore.cash.utils.Language;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTopSubCommand implements ITopSubCommand {

    @Override
    public void execute(TopManager topManager, EC_Config config, CommandSender s, String[] a) {

        if (!(s instanceof Player)) {
            s.sendMessage(Language.get("no_console"));
            return;
        }

        val player = (Player) s;
        if (!player.hasPermission(config.getString("cmd permission.admin"))) {
            player.sendMessage(Language.get("no_permission"));
            return;
        }
        if (a.length < 3) {
            player.sendMessage(Language.get("no_args.settop"));
            return;
        }


        try {
            val pos = Integer.parseInt(a[2]);
            if (pos <= 0) {
                player.sendMessage(Language.get("number_must_higher"));
                return;
            }
            if (pos > topManager.getMax()) {
                player.sendMessage(Language.get("top_number_max").replace("{max}", "" + topManager.getMax()));
                return;
            }

            if (!topManager.hasTop(pos)) {
                player.sendMessage(Language.get("no_has_top").replace("{pos}", "" + pos));
                return;
            }

            if (topManager.isSpawned(pos)) {
                val topAs = topManager.getArmorStands().get(pos);
                topAs.getAs().teleport(player.getLocation());
                val loc = topAs.getAs().getLocation();

                topAs.getHologram().teleport(loc.clone().add(topManager.getXHologram(), topManager.getYHologram(), topManager.getZHologram()));

                topManager.unSave(pos);
                topManager.save(pos, loc);

                player.sendMessage(Language.get("top_substituted").replace("{pos}", "" + pos));
                player.sendMessage(Language.get("top_set").replace("{pos}", "" + pos));
                return;
            }
            topManager.spawnTopAs(player.getLocation().clone(), pos);
            player.sendMessage(Language.get("top_set").replace("{pos}", "" + pos));

        } catch (NumberFormatException ignored) {
            player.sendMessage(Language.get("invalid_number"));
        }
    }
}
