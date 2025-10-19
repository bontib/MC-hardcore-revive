package nl.bonniebot.hardcoreRevive.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeadPlayerStorage {
    private final File file;
    private final FileConfiguration configuration;
    private final Set<UUID> deadPlayers = new HashSet<>();

    public DeadPlayerStorage(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "dead_players.yml");
        configuration = YamlConfiguration.loadConfiguration(file);
        load();
    }

    public void addDeadPlayer(UUID playerUUID) {
        deadPlayers.add(playerUUID);
        save();
    }

    public boolean isDeadPlayer(UUID playerUUID) {
        return deadPlayers.contains(playerUUID);
    }

    public void removeDeadPlayer(UUID playerUUID) {
        deadPlayers.remove(playerUUID);
        save();
    }

    private void save() {
        configuration.set("dead", deadPlayers.stream().map(UUID::toString).toList());
        try { configuration.save(file); } catch (IOException e) { e.printStackTrace(); }
    }

    private void load() {
        List<String> list = configuration.getStringList("dead");
        for (String s : list) deadPlayers.add(UUID.fromString(s));
    }

    public Set<UUID> getDeadPlayers() {
        return deadPlayers;
    }
}
