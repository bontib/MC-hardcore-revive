package nl.bonniebot.hardcoreRevive.utils;

import nl.bonniebot.hardcoreRevive.data.DeadPlayerStorage;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RevivalManager {
    private final DeadPlayerStorage storage;

    public RevivalManager(DeadPlayerStorage storage) {
        this.storage = storage;
    }

    /**
     * Revives a dead player at a given location.
     *
     * @param targetName Name of the player to revive
     * @param location   Where to revive them
     * @return true if successful, false if not
     */
    public boolean revivePlayer(String targetName, Location location) {
        OfflinePlayer offline = Bukkit.getOfflinePlayer(targetName);

        // Player must be online for proper revival
        if (!offline.isOnline()) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + targetName + " must be online to be revived!");
            return false;
        }

        Player player = offline.getPlayer();
        if (player == null) return false;

        UUID uuid = player.getUniqueId();

        if (!storage.isDeadPlayer(uuid)) {
            player.sendMessage(ChatColor.RED + "You are not marked as dead.");
            return false;
        }

        // Teleport + effects
        World world = location.getWorld();
        world.strikeLightningEffect(location);
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, location, 100, 1, 1, 1, 0.1);
        world.playSound(location, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);

        // Reset player state
        player.teleport(location);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getInventory().clear();

        // Play Totem of Undying animation without giving the item
        player.playEffect(EntityEffect.TOTEM_RESURRECT);

        storage.removeDeadPlayer(uuid);

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has been resurrected!");
        return true;
    }
}
