package com.hexagonstore.cash.manager;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.hexagonstore.cash.CashSpigot;
import com.hexagonstore.cash.HexagonCashAPI;
import com.hexagonstore.cash.models.TopAs;
import com.hexagonstore.cash.utils.*;
import lombok.Getter;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopManager {

    private Map<String, Double> accounts;
    private TreeMap<Integer, Map.Entry<String, Double>> values;
    @Getter
    final private Map<Integer, TopAs> armorStands;

    @Getter private int delay;

    private HexagonCashAPI cashAPI;
    private EC_Config topCfg;

    @Getter
    private double xHologram, yHologram, zHologram;

    public TopManager(EC_Config topCfg, HexagonCashAPI cashAPI, JavaPlugin main) {
        accounts = new HashMap<>();
        values = new TreeMap<>();
        armorStands = new HashMap<>();


        delay = 3;

        this.cashAPI = cashAPI;
        this.topCfg = topCfg;

        /*SUL:
        X:
        Esquerda: Positivo
        DIreita: Negativo
        Z:
        Frente: Positivo
        Trás: Negativo

        NORTE:
        X:
        Esquerda: Negativo
        DIreita: Positivo
        Z:
        Frente: Negativo
        Trás: Positivo

        LESTE:
        X:
        Frente: Positivo
        Trás: Negativo
        Z:
        Esquerda: Negativo
        DIreita: Positivo

        OESTE:
        X:
        Frente: Negativo
        Trás: Positivo
        Z:
        Esquerda: Positivo
        DIreita: Negativo*/

        this.xHologram = 0;
        this.yHologram = (topCfg.getBoolean("ArmorStand.small") ? 1.0 : 2.0) + topCfg.getDouble("ArmorStand.hologram.distance");
        this.zHologram = 0;

        load();
        run(main);
    }

    public void save(int pos, Location loc) {
        val newList = new ArrayList<>(topCfg.getStringList("Locations"));
        newList.add(pos + ":" + LocationSerializer.getSerializedLocation(loc));

        topCfg.set("Locations", newList);
        topCfg.saveConfig();
    }

    public void unSave(int pos) {
        val newList = new ArrayList<>(topCfg.getStringList("Locations"));
        for (String obj : topCfg.getStringList("Locations")) {
            String[] split = obj.split(":");
            val one = Integer.parseInt(split[0]);
            if(one == pos) newList.remove(obj);
        }

        topCfg.set("Locations", newList);
        topCfg.saveConfig();
    }

    private void run(JavaPlugin main) {
        int delayTimer = delay * (20 * 60);
        new BukkitRunnable() {
            public void run() {
                reload();
            }
        }.runTaskTimer(main, 20*15, 20*15);
    }

    public boolean isSpawned(int pos) {
        return armorStands.containsKey(pos);
    }

    private void configureTopHologram(Hologram hologram, String player, double price, int pos) {
        for (String line : topCfg.getStringList("ArmorStand.hologram.lines")) {
            line = ChatColor.translateAlternateColorCodes('&', line.replace("{value}", NumberFormatter.formatNumber(price)).replace("{player}", player).replace("{pos}", "" + pos));
            if (line.startsWith("$icon:")) {
                String[] split = line.replace("$icon:", "").split(":");
                hologram.appendItemLine(new ItemStack(Integer.parseInt(split[0]), 1, Short.parseShort(split[1])));
            } else if (line.startsWith("$icon-url:")) {
                hologram.appendItemLine(SkullURL.getSkull(line.replace("$icon-url:", "")));
            } else if (line.equalsIgnoreCase("$icon-owner")) {
                hologram.appendItemLine(new ItemBuilder(397, (short) 3).setSkullOwner(player).toItemStack());
            } else hologram.appendTextLine(line);
        }
    }

    private void reload() {
        cashAPI.getAllAccounts().forEach(user -> {
            val price = cashAPI.getCash(user);
            accounts.put(user.getPlayerName().toLowerCase(), price);
        });

        Stream<Map.Entry<String, Double>> streamOrdenada = accounts.entrySet().stream()
                .sorted((x, y) -> y.getValue().compareTo(x.getValue()));

        int i = 1;
        for (Map.Entry<String, Double> entry : streamOrdenada.collect(Collectors.toList())) {
            if (i >= getMax()) {
                break;
            }
            val player = entry.getKey();
            val price = entry.getValue();


            values.put(i, entry);

            val topAs = armorStands.get(i);
            if (topAs != null) {
                if (topCfg.getBoolean("ArmorStand.skull.player")) {
                    topAs.getAs().setHelmet(new ItemBuilder(397, (short) 3).setSkullOwner(player).toItemStack());
                } else {
                    if (!topCfg.getString("ArmorStand.skull.url").isEmpty()) {
                        topAs.getAs().setHelmet(SkullURL.getSkull(topCfg.getString("skull.url")));
                    } else {
                        if (topCfg.getBoolean("ArmorStand.acessories.helmet.enable")) {
                            topAs.getAs().setHelmet(new ItemStack(topCfg.getInt("ArmorStand.acessories.helmet.item.id"), 1, (short) topCfg.getInt("ArmorStand.acessories.helmet.item.data")));
                        }
                    }
                }
                topAs.clearLines();

                if (topAs.getHologram() == null)
                    topAs.setHologram(HologramsAPI.createHologram(CashSpigot.getPlugin(), topAs.getAs().getLocation().clone().add(0, yHologram, 0)));
                val hologram = topAs.getHologram();
                this.configureTopHologram(hologram, player, price, topAs.getPos());
            }
            i++;
        }
    }

    public void unload() {
        for (TopAs topAs : armorStands.values()) {
            if(topAs.getAs() != null) topAs.getAs().remove();
            if(topAs.getHologram() != null) topAs.getHologram().delete();
        }
    }

    public void load() {
        reload();

        val locations = topCfg.getStringList("Locations");
        for (String obj : locations) {
            String[] split = obj.split(":");
            val pos = Integer.parseInt(split[0]);
            val loc = LocationSerializer.getDeserializedLocation(split[1]);
            spawnTopAs(loc, pos);
        }
    }

    public int getMax() {
        return topCfg.getInt("Defaults.max");
    }

    private final BlockFace[] axis = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private final BlockFace[] radial = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};

    /**
     * Gets the horizontal Block Face from a given yaw angle<br>
     * This includes the NORTH_WEST faces
     *
     * @param yaw angle
     * @return The Block Face of the angle
     */
    private BlockFace yawToFace(float yaw) {
        return yawToFace(yaw, true);
    }

    /**
     * Gets the horizontal Block Face from a given yaw angle
     *
     * @param yaw                      angle
     * @param useSubCardinalDirections setting, True to allow NORTH_WEST to be returned
     * @return The Block Face of the angle
     */
    private BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return radial[Math.round(yaw / 45f) & 0x7];
        } else {
            return axis[Math.round(yaw / 90f) & 0x3];
        }
    }

    private ArmorStand createTopAs(Location loc, String player) {
        val as = loc.getWorld().spawn(loc, ArmorStand.class);
        as.setMetadata(cashAPI.getMetaData(), new FixedMetadataValue(CashSpigot.getPlugin(), new Object()));
        as.setSmall(topCfg.getBoolean("ArmorStand.small"));
        if (topCfg.getBoolean("ArmorStand.skull.player")) {
            as.setHelmet(new ItemBuilder(397, (short) 3).setSkullOwner(player).toItemStack());
        } else {
            if (!topCfg.getString("ArmorStand.skull.url").isEmpty()) {
                if (topCfg.getBoolean("ArmorStand.acessories.helmet.enable")) {
                    as.setHelmet(new ItemStack(topCfg.getInt("ArmorStand.acessories.helmet.item.id"), 1, (short) topCfg.getInt("ArmorStand.acessories.helmet.item.data")));
                }
            }
        }

        val acessories = "ArmorStand.acessories.";
        if (topCfg.getBoolean(acessories + "itemInHand.enable")) {
            as.setItemInHand(new ItemStack(topCfg.getInt(acessories + "itemInHand.id"), 1, topCfg.getShort(acessories + "itemInHand.data")));
        }

        if (topCfg.getBoolean(acessories + "chestplate.enable")) {
            as.setItemInHand(new ItemStack(topCfg.getInt(acessories + "chestplate.id"), 1, topCfg.getShort(acessories + "chestplate.data")));
            as.setHelmet(SkullURL.getSkull(topCfg.getString("skull.url")));
        }

        if (topCfg.getBoolean(acessories + "leggings.enable")) {
            as.setItemInHand(new ItemStack(topCfg.getInt(acessories + "leggings.id"), 1, topCfg.getShort(acessories + "leggings.data")));
        }

        if (topCfg.getBoolean(acessories + "boots.enable")) {
            as.setItemInHand(new ItemStack(topCfg.getInt(acessories + "boots.id"), 1, topCfg.getShort(acessories + "boots.data")));
        }
        return as;
    }

    public boolean hasTop(int pos) {
        return values.containsKey(pos);
    }

    public ArmorStand spawnTopAs(Location loc, int pos) {
        val entry = values.get(pos);
        val player = entry.getKey();
        val price = entry.getValue();

        val as = createTopAs(loc, player);
        val face = yawToFace(loc.getYaw(), false);

        Hologram hologram = null;
        switch (face) {
            case EAST:
                hologram = HologramsAPI.createHologram(CashSpigot.getPlugin(), loc.clone().add(-xHologram, yHologram, zHologram));
                break;
            case SOUTH:
                hologram = HologramsAPI.createHologram(CashSpigot.getPlugin(), loc.clone().add(-xHologram, yHologram, -xHologram));
                break;
            case WEST:
                hologram = HologramsAPI.createHologram(CashSpigot.getPlugin(), loc.clone().add(xHologram, yHologram, -zHologram));
                break;
            case NORTH:
            default:
                hologram = HologramsAPI.createHologram(CashSpigot.getPlugin(), loc.clone().add(xHologram, yHologram, zHologram));
                break;
        }

        this.configureTopHologram(hologram, player, price, pos);

        val topAs = new TopAs(pos, as);
        topAs.setHologram(hologram);
        armorStands.put(pos, topAs);

        if(!topCfg.getStringList("Locations").contains(pos + ":" + LocationSerializer.getSerializedLocation(topAs.getAs().getLocation()))) {
            save(pos, topAs.getAs().getLocation());
        }
        return as;
    }

    public void deleteTopAs(int pos) {
        val as = armorStands.get(pos);
        if (as != null) {
            if (as.getAs() != null) as.getAs().remove();
            if (as.getHologram() != null) as.getHologram().delete();
        }
        armorStands.remove(pos);
        unSave(pos);
    }
}
