package com.lovenek0.teleportBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockDB {
    private static File file;
    private static FileConfiguration config;

    public static void init() {
        file = new File(TeleportBlock.getPluginInstance().getDataFolder(), "blocks.yml");
        config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveBlock(String id, Location location) {
        config.set(id + ".world", location.getWorld().getName());
        config.set(id + ".x", location.getBlockX());
        config.set(id + ".y", location.getBlockY());
        config.set(id + ".z", location.getBlockZ());

        saveConfig();
    }

    public static Location getBlock(String blockId) {
        return getBlock(UUID.fromString(blockId));
    }
    public static Location getBlock(UUID blockId) {
        String id = blockId.toString();
        if (!config.contains(id)) return null;

        String worldName = config.getString(id + ".world");
        int x = config.getInt(id + ".x");
        int y = config.getInt(id + ".y");
        int z = config.getInt(id + ".z");

        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        return new Location(world, x, y, z);
    }
    public static UUID getBlock(Location location){
        return getBlock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    public static UUID getBlock(String world, int x, int y, int z){
        Map<UUID, Location> allBlocks = loadAllBlocks();
        return allBlocks.entrySet().stream()
                .filter(item ->
                        item.getValue().getBlockX() == x &&
                        item.getValue().getBlockY() == y &&
                        item.getValue().getBlockZ() == z &&
                        item.getValue().getWorld().getName().equals(world))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public static void deleteBlock(UUID blockId) {
        String id = blockId.toString();
        if (config.contains(id)) {
            config.set(id, null);
            saveConfig();
        }
    }

    public static Map<UUID, Location> loadAllBlocks() {
        Map<UUID, Location> blocks = new HashMap<>();

        for (String key : config.getKeys(false)) {
            UUID blockId = UUID.fromString(key);
            Location location = getBlock(blockId);
            if (location != null) {
                blocks.put(blockId, location);
            }
        }

        return blocks;
    }

    private static void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
