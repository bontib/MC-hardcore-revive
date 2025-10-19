package nl.bonniebot.hardcoreRevive.commands;

import nl.bonniebot.hardcoreRevive.items.ResurrectionStone;
import nl.bonniebot.hardcoreRevive.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BindReviveCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;
    private final ResurrectionStone stoneUtil;

    public BindReviveCommand(JavaPlugin plugin, ResurrectionStone stoneUtil) {
        this.plugin = plugin;
        this.stoneUtil = stoneUtil;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("resurrection.bind")) {
            player.sendMessage(ChatColor.RED + "You don’t have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /bindrevive <player>");
            return true;
        }

        String targetName = args[0];

        // Validate player existence
        if (!PlayerUtils.playerExists(targetName)) {
            player.sendMessage(ChatColor.RED + "No player found with name: " + ChatColor.WHITE + targetName);
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!stoneUtil.isResurrectionStone(itemInHand)) {
            player.sendMessage(ChatColor.RED + "You must hold a Resurrection Stone in your main hand to bind it.");
            return true;
        }

        stoneUtil.bindPlayer(itemInHand, targetName);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "The soul of " + ChatColor.AQUA + targetName + ChatColor.LIGHT_PURPLE + " has been bound to this stone.");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player online : sender.getServer().getOnlinePlayers()) {
                if (online.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(online.getName());
                }
            }
        }

        return completions;
    }
}
