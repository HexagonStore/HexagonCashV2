package com.hexagonstore.cash.database.datamanager;

import java.util.ArrayList;
import java.util.List;

import com.hexagonstore.cash.models.CashAccount;
import com.hexagonstore.cash.database.datasource.AbstractDataSource;

public class DataManager {

    public final CachedDataManager<String, CashAccount> USERS;

    @SuppressWarnings("rawtypes")
    private List<CachedDataManager> daos;

    public DataManager(AbstractDataSource abstractDataSource) {
        this.daos = new ArrayList<>();
        daos.add(USERS = new CachedDataManager<>(abstractDataSource, "players_cash", CashAccount.class));

    }

    public int saveCached(boolean async) {
        daos.forEach(dao -> dao.saveCached(async));
        return daos.stream().mapToInt(dao -> dao.getCached().size()).sum();
    }
}