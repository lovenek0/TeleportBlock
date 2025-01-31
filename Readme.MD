# TeleportBlock

**Version:** 1.0.0  
**Author:** [@lovenek0](https://github.com/lovenek0)  
**Compatibility:** Bukkit API 1.21

## Description
`TeleportBlock` is a Minecraft plugin that adds two new items: **Teleportation Block** and **Teleportation Compass**. These items allow players to link a compass to a specific block and teleport to it after a short delay.

## Installation
1. Download `TeleportBlock.jar`.
2. Place the file into your server's `plugins` folder.
3. Start the server to automatically generate the configuration file (`config.yml`).

## Configuration
After the first launch, a `config.yml` file is created in the `plugins/TeleportBlock` folder.

### Parameters
```yaml
teleport-delay: 3000  # Delay before teleportation (in milliseconds)
cancel-timeout: 350   # Timeout for teleportation cancellation if no interaction occurs (in milliseconds)
language: "en"        # Message language (available: "en" and "ru")

languages:
  en:
    teleportBlockName: "Teleportation Block"
    teleportCompassName: "Teleportation Compass"

  ru:
    teleportBlockName: "Блок телепортации"
    teleportCompassName: "Компас телепортации"
```

## Crafting
### Teleportation Block
#### Ingredients:
- `Crying Obsidian: 8`
- `Ender Pearl: 1`

#### Layout:
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>
<br>
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>
<img alt="Ender Pearl" src="./assets/Ender_Pearl.webp" width="32px" height="32px"/>
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>
<br>
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>
<img alt="Crying Obsidian" src="./assets/Crying_Obsidian.webp" width="32px" height="32px"/>

### Teleportation Compass
#### Ingredients:
- `Iron Ingot: 4`
- `Redstone: 1`
- `Ender Pearl: 1`

#### Layout:
<img alt="Iron Ingot" src="./assets/Iron_Ingot.webp" width="32px" height="32px"/>
<img alt="Iron Ingot" src="./assets/Iron_Ingot.webp" width="32px" height="32px"/>
<img alt="Iron Ingot" src="./assets/Iron_Ingot.webp" width="32px" height="32px"/>
<br>
<img alt="Iron Ingot" src="./assets/Iron_Ingot.webp" width="32px" height="32px"/>
<img alt="Redstone_Dust" src="./assets/Redstone_Dust.webp" width="32px" height="32px"/>
<img alt="Iron Ingot" src="./assets/Iron_Ingot.webp" width="32px" height="32px"/>
<br>
<img alt="Iron Ingot" src="./assets/Iron_Ingot.webp" width="32px" height="32px"/>
<img alt="Ender Pearl" src="./assets/Ender_Pearl.webp" width="32px" height="32px"/>
<img alt="Iron Ingot" src="./assets/Iron_Ingot.webp" width="32px" height="32px"/>

## Usage
### Placing the Teleportation Block
1. Craft the **Teleportation Block** and place it in the desired location.
2. The block will automatically save its location.

### Linking the Compass
1. Hold the **Teleportation Compass** in your hand.
2. **Right-click** on the **Teleportation Block** to link the compass to it.

### Teleportation
1. Hold the **Teleportation Compass** in your main hand.
2. **Right-click** in the air to activate teleportation.
3. Wait (default: 3 seconds) without moving.
4. If teleportation is not canceled during this time, you will be teleported.

## Additional Features
- If the player releases right-click or moves during teleportation, it is canceled.
- Teleportation blocks persist even after a server restart.
- When a teleportation block is broken, it drops with the same identifier.
- If an attempt is made to teleport to a removed block, an error sound will play.

