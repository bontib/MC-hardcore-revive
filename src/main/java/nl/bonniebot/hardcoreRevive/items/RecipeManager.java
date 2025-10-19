package nl.bonniebot.hardcoreRevive.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipeManager {
    private final JavaPlugin plugin;
    private final ResurrectionStone resurrectionStone;

    public RecipeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.resurrectionStone = new ResurrectionStone(plugin);
    }

    /**
     * Registers all custom recipes for this plugin.
     */
    public void registerAllRecipes() {
        registerResurrectionStone();
    }

    /**
     * Registers the Resurrection Stone crafting recipe.
     */
    public void registerResurrectionStone() {
        ItemStack stone = resurrectionStone.createStone();
        NamespacedKey key = new NamespacedKey(plugin, "resurrection_stone");

        ShapedRecipe recipe = new ShapedRecipe(key, stone);
        recipe.shape("DGD", "GNG", "DGD");
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('D', Material.DIAMOND);

        plugin.getServer().addRecipe(recipe);
    }
}
