package com.lovenek0.teleportBlock;

import com.lovenek0.teleportBlock.items.TeleportationBlock;
import com.lovenek0.teleportBlock.items.TeleportationCompass;
import com.lovenek0.teleportBlock.tools.Locale;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportBlock extends JavaPlugin implements Listener {
    private static JavaPlugin pluginInstance;
    private static FileConfiguration configInstance;

    public static JavaPlugin getPluginInstance(){
        return pluginInstance;
    }
    public static FileConfiguration getConfigInstance(){
        return configInstance;
    }

    @Override
    public void onEnable() {
        pluginInstance = this;
        configInstance = getConfig();

        saveDefaultConfig();

        Locale.init();
        BlockDB.init();

        TeleportationBlock.init();
        TeleportationCompass.init();
    }
}
