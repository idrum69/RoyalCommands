package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.exceptions.InvalidItemNameException;

public class CmdHelmet implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdHelmet(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("helmet")) {
            if (!plugin.isAuthorized(cs, "rcmds.helmet")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            PConfManager pcm = PConfManager.getPConfManager(p);
            String name = args[0];
            if (name.equalsIgnoreCase("none")) {
                ItemStack helm = p.getInventory().getHelmet();
                if (plugin.requireHelm) {
                    if (pcm.getString("helmet") != null) {
                        ItemStack stack = RUtils.getItem(pcm.getString("helmet"), 1);
                        if (stack == null) {
                            cs.sendMessage(ChatColor.RED + "You have no helmet!");
                            return true;
                        }
                        if (helm == null || helm.getType() == Material.AIR || helm.getType() != stack.getType()) {
                            p.sendMessage(ChatColor.RED + "You have already removed your helmet!");
                            pcm.set("helmet", null);
                            return true;
                        }
                        p.getInventory().addItem(stack);
                    }
                }
                p.getInventory().setHelmet(null);
                p.sendMessage(ChatColor.BLUE + "Removed your helmet.");
                return true;
            }
            ItemStack stack = RUtils.getItem(name, 1);
            if (stack == null) {
                try {
                    stack = RUtils.getItemFromAlias(name, 1);
                } catch (InvalidItemNameException e) {
                    cs.sendMessage(ChatColor.RED + "Invalid item name!");
                    return true;
                } catch (NullPointerException e) {
                    cs.sendMessage(ChatColor.RED + "ItemNameManager was not loaded. Let an administrator know.");
                    return true;
                }
            }
            if (stack == null) {
                p.sendMessage(ChatColor.RED + "Invalid item name!");
                return true;
            }
            if (plugin.requireHelm) {
                if (!p.getInventory().contains(stack)) {
                    p.sendMessage(ChatColor.RED + "You don't have that item!");
                    return true;
                }
                pcm.set("helmet", stack.getType().name());
                p.getInventory().remove(stack);
            }
            p.getInventory().setHelmet(stack);
            p.sendMessage(ChatColor.BLUE + "Set your helmet to " + ChatColor.GRAY + stack.getType().name().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
