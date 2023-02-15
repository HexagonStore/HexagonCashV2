package com.hexagonstore.cash.utils;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class LocationSerializer {

    public static String getSerializedLocation(Location loc) {
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getName();
    }

    public static Location getDeserializedLocation(String s) {
        String [] parts = s.split(";");
        val x = Double.parseDouble(parts[0]);
        val y = Double.parseDouble(parts[1]);
        val z = Double.parseDouble(parts[2]);
        World w = Bukkit.getWorld(parts[3]);
        return new Location(w, x, y, z);
    }
}
