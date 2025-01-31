package com.lovenek0.teleportBlock.items;

import com.lovenek0.teleportBlock.BlockDB;
import com.lovenek0.teleportBlock.TeleportBlock;
import com.lovenek0.teleportBlock.tools.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class TeleportationCompass implements Listener {
    private static final HashMap<Player, Float> teleportingPlayers = new HashMap<>();
    private static final Map<Player, Long> lastInteractionTime = new HashMap<>();

    private static float teleportDelay = 3f;
    private static int cancelDelay = 350;
    private static final float maxSpeed = 0.2f;

    public static void init() {
        teleportDelay = TeleportBlock.getConfigInstance().getInt("teleport-delay") / 1000f;
        cancelDelay = TeleportBlock.getConfigInstance().getInt("cancel-timeout");

        createRecipe();
        runTeleportationTimer();
        getServer().getPluginManager().registerEvents(new TeleportationCompass(), TeleportBlock.getPluginInstance());
    }

    private static void createRecipe() {
        ItemStack teleportCompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = teleportCompass.getItemMeta();

        meta.displayName(Component.text(Locale.get("teleportCompassName")).color(TextColor.color(0xFF00EB)));

        NamespacedKey blockIdKey = new NamespacedKey(TeleportBlock.getPluginInstance(), "block_id");
        meta.getPersistentDataContainer().set(blockIdKey, PersistentDataType.STRING, "");

        teleportCompass.setItemMeta(meta);

        ShapedRecipe compassRecipe = new ShapedRecipe(
                new NamespacedKey(TeleportBlock.getPluginInstance(), "teleportation_compass"),
                teleportCompass
        );
        compassRecipe.shape("III", "IRI", "IPI");
        compassRecipe.setIngredient('I', Material.IRON_INGOT);
        compassRecipe.setIngredient('R', Material.REDSTONE);
        compassRecipe.setIngredient('P', Material.ENDER_PEARL);
        Bukkit.addRecipe(compassRecipe);
    }

    private static void runTeleportationTimer(){
        Bukkit.getScheduler().runTaskTimer(TeleportBlock.getPluginInstance(), () -> {
            for (Map.Entry<Player, Float> entry : teleportingPlayers.entrySet()) {

                long currentTime = System.currentTimeMillis();

                Player player = entry.getKey();
                float time = entry.getValue();

                if (currentTime - lastInteractionTime.getOrDefault(player, 0L) > cancelDelay) {
                    teleportingPlayers.remove(player);
                    lastInteractionTime.remove(player);
                    teleportingPlayers.remove(player);
                    player.setWalkSpeed(maxSpeed);
                    return;
                }

                if (time >= teleportDelay) {
                    ItemStack item = player.getInventory().getItemInMainHand();

                    ItemMeta meta = item.getItemMeta();
                    if(meta != null) {
                        NamespacedKey blockIdKey = new NamespacedKey(TeleportBlock.getPluginInstance(), "block_id");
                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        if (container.has(blockIdKey, PersistentDataType.STRING)) {
                            String uuid = container.get(blockIdKey, PersistentDataType.STRING);
                            if (!uuid.isEmpty()) {
                                Location location = BlockDB.getBlock(uuid);
                                if (location != null) {
                                    Location oldLocation = player.getLocation();
                                    location.setYaw(player.getYaw());
                                    location.setPitch(player.getPitch());
                                    player.teleport(location.add(0.5, 1, 0.5));

                                    Bukkit.getOnlinePlayers().forEach(p -> {
                                        p.stopSound(Sound.BLOCK_PORTAL_AMBIENT);
                                        p.playSound(
                                            oldLocation,
                                            Sound.ENTITY_ENDERMAN_TELEPORT,
                                            2.0f,
                                            1.0f);
                                        p.playSound(
                                            player.getLocation(),
                                            Sound.ENTITY_ENDERMAN_TELEPORT,
                                            2.0f,
                                            1.0f);
                                    });
                                }
                            }
                        }
                    }
                    teleportingPlayers.remove(player);
                    lastInteractionTime.remove(player);
                    player.setWalkSpeed(maxSpeed);
                } else {
                    teleportingPlayers.put(player, time + 0.25f);
                    player.setWalkSpeed(Math.max(0.05f, maxSpeed - time * (maxSpeed - 0.05f) / teleportDelay));

                    float volume = Math.min(1.0f, time / teleportDelay);
                    float pitch = 1.0f + time / teleportDelay;

                    Bukkit.getOnlinePlayers().forEach(p ->
                            p.playSound(
                                player.getLocation(),
                                Sound.BLOCK_PORTAL_AMBIENT,
                                volume,
                                pitch));

                    Location location = player.getLocation().add(0, 1, 0);
                    int particleCount = (int) (time * 50);

                    player.getWorld().spawnParticle(
                            Particle.PORTAL,
                            location,
                            particleCount,
                            0.5, 0.5, 0.5,
                            0.1
                    );
                }
            }
        }, 0L, 5L);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        teleportingPlayers.remove(event.getPlayer());
        lastInteractionTime.remove(event.getPlayer());
        event.getPlayer().setWalkSpeed(maxSpeed);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            if(teleportingPlayers.containsKey(event.getPlayer())) {
                lastInteractionTime.put(event.getPlayer(), System.currentTimeMillis());
            }
            else {
                ItemStack item = event.getItem();
                if (item != null && item.getType() == Material.COMPASS) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        NamespacedKey blockIdKey = new NamespacedKey(TeleportBlock.getPluginInstance(), "block_id");
                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        if (container.has(blockIdKey, PersistentDataType.STRING)) {
                            Block block = event.getClickedBlock();
                            if (block != null && block.getType() == Material.CRYING_OBSIDIAN) {
                                UUID uuid = BlockDB.getBlock(block.getLocation());
                                if (uuid != null) {
                                    teleportingPlayers.remove(event.getPlayer());
                                    meta.getPersistentDataContainer().set(blockIdKey, PersistentDataType.STRING, uuid.toString());
                                    meta.lore(List.of(Component.text(uuid.toString()).color(TextColor.color(0x808080)).decorate(TextDecoration.OBFUSCATED)));
                                    ((CompassMeta) meta).setLodestone(block.getLocation());
                                    ((CompassMeta) meta).setLodestoneTracked(false);
                                    item.setItemMeta(meta);
                                    Bukkit.getOnlinePlayers().forEach(p ->
                                            p.playSound(
                                                    event.getPlayer().getLocation(),
                                                    Sound.BLOCK_ANVIL_LAND,
                                                    1.0f,
                                                    1.0f
                                            ));
                                    return;
                                }
                            }
                            String uuid = meta.getPersistentDataContainer().get(blockIdKey, PersistentDataType.STRING);
                            Location location = BlockDB.getBlock(uuid);
                            if (location == null) {
                                Bukkit.getOnlinePlayers().forEach(p ->
                                        p.playSound(
                                                event.getPlayer().getLocation(),
                                                Sound.ENTITY_ITEM_BREAK,
                                                1.0f,
                                                1.0f
                                        ));
                                return;
                            }
                            teleportingPlayers.put(event.getPlayer(), 0f);
                            lastInteractionTime.put(event.getPlayer(), System.currentTimeMillis());
                        }
                    }
                }
            }
        }
    }
}