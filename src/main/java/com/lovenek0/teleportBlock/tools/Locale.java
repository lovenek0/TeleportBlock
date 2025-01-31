package com.lovenek0.teleportBlock.tools;

import com.lovenek0.teleportBlock.TeleportBlock;

import java.util.HashMap;
import java.util.Map;

public class Locale {
    private static final Map<String, String> messages = new HashMap<>();

    public static void init() {
        String currentLang = TeleportBlock.getConfigInstance().getString("language", "en");

        String basePath = "languages." + currentLang;

        messages.put("teleportBlockName", TeleportBlock.getConfigInstance().getString(basePath + ".teleportBlockName", "Teleportation Block"));
        messages.put("teleportCompassName", TeleportBlock.getConfigInstance().getString(basePath + ".teleportCompassName", "Teleportation Compass"));
    }

    public static String get(String key) {
        return messages.getOrDefault(key, key);
    }
}
