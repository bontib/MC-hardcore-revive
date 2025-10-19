package nl.bonniebot.hardcoreRevive.events;

import nl.bonniebot.hardcoreRevive.data.DeadPlayerStorage;
import nl.bonniebot.hardcoreRevive.items.ResurrectionStone;
import nl.bonniebot.hardcoreRevive.utils.RevivalManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ItemDropListener implements Listener {
    private final JavaPlugin plugin;
    private final ResurrectionStone stoneUtil;
    private final RevivalManager revivalManager;
    private final DeadPlayerStorage storage;

    public ItemDropListener(JavaPlugin plugin, ResurrectionStone stoneUtil, RevivalManager revivalManager, DeadPlayerStorage storage) {
        this.plugin = plugin;
        this.stoneUtil = stoneUtil;
        this.revivalManager = revivalManager;
        this.storage = storage;
    }

    @EventHandler
    public void onItemDrop(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack stack = item.getItemStack();

        if (!stoneUtil.isResurrectionStone(stack)) return;

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks += 10;
                if (ticks > 200) { // 10 seconds
                    cancel();
                    return;
                }

                Location loc = item.getLocation();
                Block block = loc.getBlock();

                if (block.getType() == Material.WATER_CAULDRON) {
                    List<Item> nearbyItems = item.getNearbyEntities(1, 1, 1)
                            .stream()
                            .filter(e -> e instanceof Item)
                            .map(e -> (Item) e)
                            .toList();

                    for (Item nearby : nearbyItems) {
                        if (nearby.getItemStack().getType() == Material.DIAMOND) {
                            String boundName = stoneUtil.getBoundPlayer(stack);
                            item.remove();
                            nearby.remove();


                            if (boundName == null) {
                                plugin.getServer().broadcastMessage("A Resurrection Stone without a bound soul was destroyed in a cauldron.");
                                cancel();
                                return;
                            }

                            revivalManager.revivePlayer(boundName, loc);
                            block.setType(Material.CAULDRON);

                            cancel();
                            return;
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }
}
