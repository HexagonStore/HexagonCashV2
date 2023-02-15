package com.hexagonstore.cash.database.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hexagonstore.cash.database.datamanager.GenericDataManager;
import com.hexagonstore.cash.database.exception.DatabaseException;
import com.hexagonstore.cash.database.util.Utils;

public class MySQLDataSource extends AbstractDataSource {

    private Connection connection;

    public MySQLDataSource(String ip, String database, String user, String password) throws DatabaseException {
        openConnection(ip, database, user, password);
    }

    private void openConnection(String ip, String database, String user, String password) throws DatabaseException {
        try {
            String url = "jdbc:mysql://" + ip + "/" + database + "?autoReconnect=true";
            this.connection = DriverManager.getConnection(url, user, password);
            Utils.debug(Utils.LogType.INFO, "conexão com mysql inicializada com sucesso");
        } catch (Exception e) {
            throw new DatabaseException("não foi possivel iniciar conexão com banco de dados mysql", e);
        }
    }

    @Override
    public <V> void insert(String key, V value, boolean async, String tableName) {
        Runnable runnable = () -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `" + tableName + "`(`key`, `json`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `json` = VALUES(`json`)")) {
                preparedStatement.setString(1, key);
                preparedStatement.setString(2, gson.toJson(value));
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) executor.submit(runnable);
        else runnable.run();
    }

    @Override
    public void delete(String key, boolean async, String tableName) {
        Runnable runnable = () -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `" + tableName + "` WHERE `key` = ?")) {
                preparedStatement.setString(1, key);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) executor.submit(runnable);
        else runnable.run();
    }

    @Override
    public <V> V find(String key, String tableName, Class<V> vClass) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + tableName + "` WHERE `key` = ?")) {
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return gson.fromJson(resultSet.getString("json"), vClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <V> List<V> findAll(String tableName, Class<V> vClass) {
        List<V> values = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + tableName + "`")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                values.add(gson.fromJson(resultSet.getString("json"), vClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public boolean exists(String key, String tableName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + tableName + "` WHERE `key` = ?")) {
            preparedStatement.setString(1, key);
            return preparedStatement.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void createTable(GenericDataManager<?, ?> dao) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + dao.getTableName() + "`(`key` VARCHAR(64) NOT NULL, `json` TEXT NOT NULL, PRIMARY KEY (`key`))")) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("não foi possivel fechar conexão com mysql", e);
        }
    }
}