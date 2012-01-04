package tk.royalcraf.rcommands;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Warn implements CommandExecutor {

	RoyalCommands plugin;

	public Warn(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("warn")) {
			if (!plugin.isAuthorized(cs, "rcmds.warn")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (args.length < 1) {
				return false;
			}

			Player t = plugin.getServer().getPlayer(args[0].trim());
			if (t != null) {
				if (plugin.isAuthorized((Player) t, "rcmds.exempt.warn")) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot warn that player!");
					return true;
				}
				File pconfl = new File(plugin.getDataFolder() + "/userdata/"
						+ t.getName().toLowerCase() + ".yml");
				if (pconfl.exists()) {
					FileConfiguration pconf = YamlConfiguration
							.loadConfiguration(pconfl);
					Integer numwarns = null;
					String warnreason = null;
					if (pconf.get("warns") == null) {
						numwarns = 0;
					} else {
						numwarns = pconf.getConfigurationSection("warns")
								.getValues(false).size();
					}
					if (numwarns == null) {
						numwarns = 0;
					}
					if (args.length == 1) {
						warnreason = plugin.defaultWarn;
						pconf.set("warns." + (numwarns + 1), warnreason);
						try {
							pconf.save(pconfl);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (args.length > 1) {
						warnreason = plugin.getFinalArg(args, 1);
						pconf.set("warns." + (numwarns + 1), warnreason);
						try {
							pconf.save(pconfl);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					cs.sendMessage(ChatColor.BLUE + "You have warned "
							+ ChatColor.GRAY + t.getName() + ChatColor.BLUE
							+ ".");
					t.sendMessage(ChatColor.RED + "You have been warned by "
							+ ChatColor.GRAY + cs.getName() + ChatColor.RED
							+ " for " + ChatColor.GRAY + warnreason
							+ ChatColor.RED + ".");
					plugin.getServer().broadcast(
							ChatColor.RED + "The player " + ChatColor.GRAY
									+ t.getName() + ChatColor.RED
									+ " has been warned for " + ChatColor.GRAY
									+ warnreason + ChatColor.RED + ".",
							"rcmds.see.warn");
					if (plugin.warnBan > 0) {
						if ((numwarns + 1) >= plugin.warnBan) {
							t.setBanned(true);
							t.kickPlayer(ChatColor.DARK_RED
									+ "You have been banned for reaching the max warn limit.");
							plugin.getServer()
									.broadcast(
											ChatColor.RED
													+ "The player "
													+ ChatColor.GRAY
													+ t.getName()
													+ ChatColor.RED
													+ " has been banned for "
													+ ChatColor.DARK_RED
													+ "You have been banned for reaching the max warn limit."
													+ ChatColor.RED + ".",
											"rcmds.see.ban");
						}
					}
					return true;
				} else {
					cs.sendMessage(ChatColor.RED + "That user does not exist!");
					return true;
				}
			}
			OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(
					args[0].trim());
			if (t2.isOp()) {
				cs.sendMessage(ChatColor.RED + "You cannot warn that player!");
				return true;
			}
			if (t2.isOnline()) {
				if (plugin.isAuthorized((Player) t2, "rcmds.exempt.warn")) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot warn that player!");
					return true;
				}
			}
			File pconfl = new File(plugin.getDataFolder() + "/userdata/"
					+ t2.getName().toLowerCase() + ".yml");
			if (pconfl.exists()) {
				FileConfiguration pconf = YamlConfiguration
						.loadConfiguration(pconfl);
				Integer numwarns = null;
				String warnreason = null;
				if (pconf.get("warns") == null) {
					numwarns = 0;
				} else {
					numwarns = pconf.getConfigurationSection("warns")
							.getValues(false).size();
				}
				if (numwarns == null) {
					numwarns = 0;
				}
				if (args.length == 1) {
					warnreason = plugin.defaultWarn;
					pconf.set("warns." + (numwarns + 1), warnreason);
					try {
						pconf.save(pconfl);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (args.length > 1) {
					warnreason = plugin.getFinalArg(args, 1);
					pconf.set("warns." + (numwarns + 1), warnreason);
					try {
						pconf.save(pconfl);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				cs.sendMessage(ChatColor.BLUE + "You have warned "
						+ ChatColor.GRAY + t2.getName() + ChatColor.BLUE + ".");
				if (t2.isOnline()) {
					((Player) t2)
							.sendMessage(ChatColor.RED
									+ "You have been warned by "
									+ ChatColor.GRAY + cs.getName()
									+ ChatColor.RED + " for " + ChatColor.GRAY
									+ warnreason + ChatColor.RED + ".");
				}
				plugin.getServer().broadcast(
						ChatColor.RED + "The player " + ChatColor.GRAY
								+ t2.getName() + ChatColor.RED
								+ " has been warned for " + ChatColor.GRAY
								+ warnreason + ChatColor.RED + ".",
						"rcmds.see.warn");
				if (plugin.warnBan > 0) {
					if ((numwarns + 1) >= plugin.warnBan) {
						t2.setBanned(true);
						if (t2.isOnline()) {
							((Player) t2)
									.kickPlayer(ChatColor.DARK_RED
											+ "You have been banned for reaching the max warn limit.");
						}
						plugin.getServer()
								.broadcast(
										ChatColor.RED
												+ "The player "
												+ ChatColor.GRAY
												+ t2.getName()
												+ ChatColor.RED
												+ " has been banned for "
												+ ChatColor.DARK_RED
												+ "You have been banned for reaching the max warn limit."
												+ ChatColor.RED + ".",
										"rcmds.see.ban");
					}
				}
				return true;
			} else {
				cs.sendMessage(ChatColor.RED + "That user does not exist!");
				return true;
			}
		}
		return false;
	}
}