package nl.bonniebot.hardcoreRevive;

import nl.bonniebot.hardcoreRevive.items.RecipeManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcoreRevive extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("HardcoreRevive has been enabled!");

        new RecipeManager(this).registerAllRecipes();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("HardcoreRevive has been disabled!");
    }
}
