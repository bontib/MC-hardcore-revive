package nl.bonniebot.hardcoreRevive.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * Reads configuration from config.yml under resurrection_stone and falls back to defaults on invalid/missing values.
     */
    public void registerResurrectionStone() {
        // Check if recipe registration is enabled in config
        if (!plugin.getConfig().getBoolean("resurrection_stone.enabled", true)) {
            plugin.getLogger().info("Resurrection Stone recipe is disabled in config.yml");
            return;
        }

        // Default recipe values
        List<String> defaultShape = List.of("DGD", "GNG", "DGD");

        // Read shape from config and validate
        List<String> shape = plugin.getConfig().getStringList("resurrection_stone.shape");
        if (shape.size() != 3) {
            plugin.getLogger().warning("Invalid or missing shape for Resurrection Stone in config.yml, using default shape");
            shape = defaultShape;
        } else {
            // Ensure each row is exactly 3 chars
            boolean valid = true;
            for (String row : shape) {
                if (row == null || row.length() != 3) {
                    valid = false;
                    break;
                }
            }
            if (!valid) {
                plugin.getLogger().warning("One or more shape rows are invalid (must be 3 characters). Using default shape.");
                shape = defaultShape;
            }
        }

        ItemStack stone = resurrectionStone.createStone();
        NamespacedKey key = new NamespacedKey(plugin, "resurrection_stone");

        ShapedRecipe recipe = new ShapedRecipe(key, stone);
        recipe.shape(shape.get(0), shape.get(1), shape.get(2));

        // Read ingredients mapping from config
        ConfigurationSection ingredientsSec = plugin.getConfig().getConfigurationSection("resurrection_stone.ingredients");

        // Collect unique characters used in the shape (excluding space)
        Set<Character> usedChars = new HashSet<>();
        for (String row : shape) {
            for (char c : row.toCharArray()) {
                if (c != ' ') usedChars.add(c);
            }
        }

        // Resolve materials for each used character
        for (char c : usedChars) {
            Material mat = null;
            String keyName = String.valueOf(c);
            if (ingredientsSec != null && ingredientsSec.contains(keyName)) {
                String matName = ingredientsSec.getString(keyName); // nullable
                try {
                    if (matName != null && !matName.isEmpty()) mat = Material.valueOf(matName);
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().warning("Invalid material name for recipe character '" + c + "' in config.yml: '" + matName + "'");
                }
            }

            // Fallbacks for known characters
            if (mat == null) {
                switch (c) {
                    case 'N':
                        mat = Material.NETHER_STAR;
                        break;
                    case 'G':
                        mat = Material.GOLD_INGOT;
                        break;
                    case 'D':
                        mat = Material.DIAMOND;
                        break;
                    default:
                        plugin.getLogger().warning("No material configured for recipe character '" + c + "' and no fallback available. Aborting recipe registration.");
                        return; // Abort registration to avoid incomplete recipe
                }
            }

            // If AIR, abort
            if (mat == Material.AIR) {
                plugin.getLogger().warning("Resolved material for '" + c + "' is invalid (AIR). Aborting recipe registration.");
                return;
            }

            recipe.setIngredient(c, mat);
        }

        // Finally, register the recipe
        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Registered Resurrection Stone recipe (configurable).");
    }
}
