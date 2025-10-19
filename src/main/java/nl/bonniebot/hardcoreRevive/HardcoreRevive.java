package nl.bonniebot.hardcoreRevive;

import nl.bonniebot.hardcoreRevive.commands.BindReviveCommand;
import nl.bonniebot.hardcoreRevive.items.RecipeManager;
import nl.bonniebot.hardcoreRevive.items.ResurrectionStone;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcoreRevive extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("HardcoreRevive has been enabled!");
        ResurrectionStone stoneUtil = new ResurrectionStone(this);
        BindReviveCommand bindReviveCommand = new BindReviveCommand(this, stoneUtil);

        getCommand("bindrevive").setExecutor(bindReviveCommand);
        getCommand("bindrevive").setTabCompleter(bindReviveCommand);

        new RecipeManager(this).registerAllRecipes();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("HardcoreRevive has been disabled!");
    }
}
