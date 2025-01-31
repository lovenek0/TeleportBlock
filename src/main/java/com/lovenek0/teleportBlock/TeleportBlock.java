package com.lovenek0.teleportBlock;

import com.lovenek0.teleportBlock.items.TeleportationBlock;
import com.lovenek0.teleportBlock.items.TeleportationCompass;
import com.lovenek0.teleportBlock.tools.Locale;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportBlock extends JavaPlugin implements Listener {
    private static JavaPlugin pluginInstance;

    public static JavaPlugin getPluginInstance(){
        return pluginInstance;
    }

    @Override
    public void onEnable() {
        pluginInstance = this;

        saveDefaultConfig();

        Locale.init(this);
        BlockDB.init();

        TeleportationBlock.init();
        TeleportationCompass.init();
    }
}
