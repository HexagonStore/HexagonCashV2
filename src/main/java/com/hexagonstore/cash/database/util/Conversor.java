package com.hexagonstore.cash.database.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import lombok.val;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Conversor {

    public static String serialize(ItemStack item) throws IllegalStateException {
        try {
            val outputStream = new ByteArrayOutputStream();
            val dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);
            dataOutput.close();

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }catch(IOException ignored) {}
        return "";
    }

    public static ItemStack deserialize(String data) throws ClassNotFoundException {
        try {
            val inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            val dataInput = new BukkitObjectInputStream(inputStream);

            val item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (IOException ignored) {}
        return null;
    }
}