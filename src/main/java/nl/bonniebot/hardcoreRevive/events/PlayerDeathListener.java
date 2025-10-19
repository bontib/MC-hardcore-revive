package nl.bonniebot.hardcoreRevive.events;

import nl.bonniebot.hardcoreRevive.data.DeadPlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final DeadPlayerStorage storage;

    public PlayerDeathListener(DeadPlayerStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        storage.addDeadPlayer(player.getUniqueId());

        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has fallen... Their soul lingers in the void.");
    }
}