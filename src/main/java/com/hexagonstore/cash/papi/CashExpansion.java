package com.hexagonstore.cash.papi;

import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.utils.NumberFormatter;
import lombok.val;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import com.hexagonstore.cash.CashSpigot;

public class CashExpansion extends PlaceholderExpansion {

    private final String VERSION = "1.0";

    /**
     * Defines the name of the expansion that is also used in the
     * placeholder itself.
     *
     * @return {@code hexagoncash} as String
     */
    @Override
    public String getIdentifier() {
        return "hexagoncash";
    }

    /**
     * The author of the expansion.
     *
     * @return {@code Tiago Dinis} as String
     */
    @Override
    public String getAuthor() {
        return "sailez_";
    }

    /**
     * Returns the version of the expansion as String.
     *
     * @return The VERSION String
     */
    @Override
    public String getVersion() {
        return VERSION;
    }


    /**
     * Used to check if the expansion is able to register.
     *
     * @return true or false depending on if the required plugin is installed
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * This method is called when a placeholder is used and maches the set
     * {@link #getIdentifier() identifier}
     *
     * @param  offlinePlayer
     *         The player to parse placeholders for
     * @param  params
     *         The part after the identifier ({@code %identifier_params%})
     *
     * @return Possible-null String
     */
    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        val cash = CashSpigot.getPlugin().getCashAPI().getCash(offlinePlayer.getName());
        if(params.equalsIgnoreCase("raw")) {
            return String.valueOf(cash);
        }else if(params.equalsIgnoreCase("amount")) {
            return NumberFormatter.formatNumber(cash);
        }
        return String.valueOf(0.0);
    }
}