package com.lovenek0.teleportBlock.items;

import com.lovenek0.teleportBlock.BlockDB;
import com.lovenek0.teleportBlock.TeleportBlock;
import com.lovenek0.teleportBlock.tools.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;


public class TeleportationBlock implements Listener {
    public static void init(){
        createRecipe();
        runParticlesTimer();
        getServer().getPluginManager().registerEvents(new TeleportationBlock(), TeleportBlock.getPluginInstance());
    }

    private static void createRecipe(){
        ItemStack teleportBlockItem = new ItemStack(Material.CRYING_OBSIDIAN);
        ItemMeta meta = teleportBlockItem.getItemMeta();

        meta.displayName(Component.text(Locale.get("teleportBlockName")).color(TextColor.color(0xFF00EB)));

        NamespacedKey blockIdKey = new NamespacedKey(TeleportBlock.getPluginInstance(), "block_id");
        UUID uniqueId = UUID.randomUUID();
        meta.getPersistentDataContainer().set(blockIdKey, PersistentDataType.STRING, uniqueId.toString());

        meta.lore(List.of(Component.text(uniqueId.toString()).color(TextColor.color(0x808080)).decorate(TextDecoration.OBFUSCATED)));

        teleportBlockItem.setItemMeta(meta);

        ShapedRecipe blockRecipe = new ShapedRecipe(
                new NamespacedKey(TeleportBlock.getPluginInstance(), "teleportation_block"),
                teleportBlockItem
        );
        blockRecipe.shape("PPP", "PEP", "PPP");
        blockRecipe.setIngredient('P', Material.CRYING_OBSIDIAN);
        blockRecipe.setIngredient('E', Material.ENDER_PEARL);

        Bukkit.addRecipe(blockRecipe);
    }

    private static void runParticlesTimer(){
        Bukkit.getScheduler().runTaskTimer(TeleportBlock.getPluginInstance(), () -> {
            for(Location location : BlockDB.loadAllBlocks().values().stream().toList())
                location.getWorld().spawnParticle(
                        Particle.PORTAL,
                        location.getBlockX() + 0.5,
                        location.getBlockY() + 1,
                        location.getBlockZ() + 0.5,
                        40,
                        0.2, 0.6, 0.2,
                        0.8
                );
        }, 0L, 20L);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();

        if (item.getType() == Material.CRYING_OBSIDIAN) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                NamespacedKey blockIdKey = new NamespacedKey(TeleportBlock.getPluginInstance(), "block_id");
                PersistentDataContainer container = meta.getPersistentDataContainer();

                if (container.has(blockIdKey, PersistentDataType.STRING)) {
                    String blockId = container.get(blockIdKey, PersistentDataType.STRING);
                    BlockDB.saveBlock(blockId, event.getBlock().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        if(block.getType() == Material.CRYING_OBSIDIAN){
            UUID uuid = BlockDB.getBlock(block.getLocation());
            if(uuid != null){
                ItemStack drop = new ItemStack(Material.CRYING_OBSIDIAN);
                ItemMeta meta = drop.getItemMeta();

                NamespacedKey blockIdKey = new NamespacedKey(TeleportBlock.getPluginInstance(), "block_id");
                meta.getPersistentDataContainer().set(blockIdKey, PersistentDataType.STRING, uuid.toString());

                meta.displayName(Component.text(Locale.get("teleportBlockName")).color(TextColor.color(0xFF00EB)));
                meta.lore(List.of(Component.text(uuid.toString()).color(TextColor.color(0x808080)).decorate(TextDecoration.OBFUSCATED)));

                drop.setItemMeta(meta);

                BlockDB.deleteBlock(uuid);
                event.setDropItems(false);
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
            }
        }
    }
}
