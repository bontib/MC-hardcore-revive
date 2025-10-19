package nl.bonniebot.hardcoreRevive.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ResurrectionStone {
    private final NamespacedKey boundPlayerKey;

    public ResurrectionStone(JavaPlugin plugin) {
        this.boundPlayerKey = new NamespacedKey(plugin, "bound_player");
    }

    /**
     * Creates a new unbound Resurrection Stone item.
     */
    public ItemStack createStone() {
        ItemStack stone = new ItemStack(Material.ECHO_SHARD);
        ItemMeta meta = stone.getItemMeta();

        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Resurrection Stone");
        meta.setLore(List.of(
                ChatColor.GRAY + "A mystical shard infused with life energy.",
                ChatColor.DARK_PURPLE + "Bind a soul using /bindrevive <player>"
        ));

        stone.setItemMeta(meta);
        return stone;
    }

    /**
     * Checks if an ItemStack is a valid Resurrection Stone.
     */
    public boolean isResurrectionStone(ItemStack item) {
        if (item == null || item.getType() != Material.ECHO_SHARD || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return false;

        String stripped = ChatColor.stripColor(meta.getDisplayName());
        return stripped.equalsIgnoreCase("Resurrection Stone");
    }

    /**
     * Binds a player’s name to this Resurrection Stone via metadata.
     */
    public void bindPlayer(ItemStack item, String playerName) {
        if (!isResurrectionStone(item)) return;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(boundPlayerKey, PersistentDataType.STRING, playerName);

        meta.setLore(List.of(
                ChatColor.GRAY + "Bound to the soul of: " + ChatColor.AQUA + playerName,
                ChatColor.DARK_PURPLE + "Throw it into water with a diamond..."
        ));
        item.setItemMeta(meta);
    }

    /**
     * Retrieves the bound player name from this stone, or null if none.
     */
    public String getBoundPlayer(ItemStack item) {
        if (!isResurrectionStone(item)) return null;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.get(boundPlayerKey, PersistentDataType.STRING);
    }

    public NamespacedKey getBoundPlayerKey() {
        return boundPlayerKey;
    }
}
