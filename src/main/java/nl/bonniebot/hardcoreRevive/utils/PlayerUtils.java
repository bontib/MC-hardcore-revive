package nl.bonniebot.hardcoreRevive.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtils {
    /**
     * Checks if a player is currently considered "dead" (spectator mode).
     */
    public static boolean isDead(Player player) {
        return player.getGameMode() == GameMode.SPECTATOR;
    }

    /**
     * Forces a player into spectator mode (on death).
     */
    public static void setSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(0.0);
    }

    /**
     * Attempts to find an OfflinePlayer by name (cached or not).
     */
    public static OfflinePlayer findOfflinePlayer(String name) {
        OfflinePlayer cached = Bukkit.getOfflinePlayerIfCached(name);
        if (cached != null) return cached;
        return Bukkit.getOfflinePlayer(name);
    }

    /**
     * Checks if a given name corresponds to a real player who has joined before.
     */
    public static boolean playerExists(String name) {
        OfflinePlayer player = findOfflinePlayer(name);
        return player.hasPlayedBefore() || player.isOnline();
    }

    /**
     * Returns true if a player with this UUID is online.
     */
    public static boolean isOnline(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null && player.isOnline();
    }

    /**
     * Returns the Player object if online, otherwise null.
     */
    public static Player getOnlinePlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }
}
