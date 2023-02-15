package com.hexagonstore.cash;

import com.hexagonstore.cash.models.CashAccount;
import lombok.val;
import org.bukkit.entity.Player;

import java.util.*;

public class HexagonCashAPI {

    private CashSpigot main;
    private String metaData;

    public HexagonCashAPI(CashSpigot main) {
        this.main = main;
        this.metaData = "armorstand_hexagoncashv2";
    }

    public String getMetaData() {
        return metaData;
    }
    public Collection<CashAccount> getAllAccounts() {
        return main.getMainDataManager().USERS.getCached();
    }

    public CashAccount getUser(Player player) {
        return main.getMainDataManager().USERS.getCached(player.getName());
    }

    public CashAccount getUser(String playerName) {
        return main.getMainDataManager().USERS.getCached(playerName);
    }

    public boolean containsCash(CashAccount user, double cash) {
        return user.getCash() >= cash;
    }

    public boolean containsCash(String playerName, double cash) {
        return this.getUser(playerName).getCash() >= cash;
    }

    public double getCash(CashAccount user) {
        return user.getCash();
    }

    public double getCash(String playerName) {
        return this.getUser(playerName).getCash();
    }

    public void setCash(CashAccount user, double cash) {
        user.setCash(cash);
    }

    public void setCash(String playerName, double cash) {
        this.getUser(playerName).setCash(cash);
    }

    public void removeCash(CashAccount user, double cash) {
        user.setCash(user.getCash() < cash ? 0 : user.getCash() - cash);
    }

    public void removeCash(String playerName, double cash) {
        val user = this.getUser(playerName);
        user.setCash(user.getCash() < cash ? 0 : user.getCash() - cash);
    }

    public void addCash(CashAccount user, double cash) {
        user.setCash(user.getCash() + cash);
    }

    public void addCash(String playerName, double cash) {
        val user = this.getUser(playerName);
        user.setCash(user.getCash() + cash);
    }
}
