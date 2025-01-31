package com.lovenek0.teleportBlock.tools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Locale {
    private static final Map<String, String> messages = new HashMap<>();

    public static void init(JavaPlugin plugin) {
        FileConfiguration cfg = plugin.getConfig();
        String currentLang = cfg.getString("language", "en");

        String basePath = "languages." + currentLang;

        messages.put("teleportBlockName", cfg.getString(basePath + ".teleportBlockName", "Teleportation Block"));
        messages.put("teleportCompassName", cfg.getString(basePath + ".teleportCompassName", "Teleportation Compass"));
    }

    public static String get(String key) {
        return messages.getOrDefault(key, key);
    }
}
