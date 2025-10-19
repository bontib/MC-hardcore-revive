package nl.bonniebot.hardcoreRevive;

import nl.bonniebot.hardcoreRevive.commands.BindReviveCommand;
import nl.bonniebot.hardcoreRevive.data.DeadPlayerStorage;
import nl.bonniebot.hardcoreRevive.events.ItemDropListener;
import nl.bonniebot.hardcoreRevive.events.PlayerDeathListener;
import nl.bonniebot.hardcoreRevive.items.RecipeManager;
import nl.bonniebot.hardcoreRevive.items.ResurrectionStone;
import nl.bonniebot.hardcoreRevive.utils.RevivalManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcoreRevive extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("HardcoreRevive has been enabled!");
        ResurrectionStone stoneUtil = new ResurrectionStone(this);
        DeadPlayerStorage storage = new DeadPlayerStorage(this);
        BindReviveCommand bindReviveCommand = new BindReviveCommand(this, stoneUtil);
        RevivalManager revivalManager = new RevivalManager( storage);

        getCommand("bindrevive").setExecutor(bindReviveCommand);
        getCommand("bindrevive").setTabCompleter(bindReviveCommand);

        // Register custom events
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(storage), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(this, stoneUtil, revivalManager, storage), this);

        new RecipeManager(this).registerAllRecipes();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("HardcoreRevive has been disabled!");
    }
}
