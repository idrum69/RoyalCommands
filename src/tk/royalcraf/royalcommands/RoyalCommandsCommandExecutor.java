package tk.royalcraf.royalcommands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoyalCommandsCommandExecutor implements CommandExecutor {

	Logger log = Logger.getLogger("Minecraft");

	// getFinalArg taken from EssentialsCommand.java - Essentials by
	// EssentialsTeam
	public static String getFinalArg(final String[] args, final int start) {
		final StringBuilder bldr = new StringBuilder();
		for (int i = start; i < args.length; i++) {
			if (i != start) {
				bldr.append(" ");
			}
			bldr.append(args[i]);
		}
		return bldr.toString();
	}

	public static boolean isAuthorized(final Player player, final String node) {
		if (player.isOp()) {
			return true;
		} else if (player.hasPermission(node)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAuthorized(final CommandSender player,
			final String node) {
		if (player.isOp()) {
			return true;
		} else if (player.hasPermission(node)) {
			return true;
		} else if (!player.hasPermission(node)) {
			return false;
		} else {
			return false;
		}
	}

	private RoyalCommands plugin;

	public RoyalCommandsCommandExecutor(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	public static boolean getOnline(final String person) {
		Player player = Bukkit.getServer().getPlayer(person);

		if (player == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		Player player = null;
		Player victim = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("level")) {
			if (player == null) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be used by players!");
			} else {
				if (!isAuthorized(sender, "rcmds.level")) {
					sender.sendMessage(ChatColor.RED
							+ "You don't have permission for that!");
					log.warning("[RoyalCommands] " + sender.getName()
							+ " was denied access to the command!");
					return true;
				} else {
					player.setLevel(player.getLevel() + 1);
					sender.sendMessage(ChatColor.BLUE
							+ "XP level raised by one! You may need to relog to see the changes.");
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("setlevel")) {
			if (player == null) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be used by players!");
			} else {
				if (!isAuthorized(sender, "rcmds.setlevel")) {
					sender.sendMessage(ChatColor.RED
							+ "You don't have permission for that!");
					log.warning("[RoyalCommands] " + sender.getName()
							+ " was denied access to the command!");
					return true;
				} else {
					if (args.length < 1) {
						return false;
					} else {
						int toLevel = 0;
						if (args.length == 2) {
							if (getOnline(args[1]) == false) {
								sender.sendMessage(ChatColor.RED
										+ "You must input a valid player!");
							} else {
								victim = (Player) plugin.getServer().getPlayer(
										args[1]);
								try {
									toLevel = Integer.parseInt(args[0]);
								} catch (NumberFormatException e) {
									sender.sendMessage(ChatColor.RED
											+ "Your input was not an integer!");
									return false;
								}
								if (toLevel < 0) {
									sender.sendMessage(ChatColor.RED
											+ "You cannot input anything below 0.");
								} else {
									player.setLevel(toLevel);
									sender.sendMessage(ChatColor.BLUE
											+ victim.getName()
											+ "'s XP level was set to "
											+ toLevel
											+ "! They may need to relog to see the changes.");
								}
							}
						} else if (args.length < 2 && args.length != 0) {
							try {
								toLevel = Integer.parseInt(args[0]);
							} catch (NumberFormatException e) {
								sender.sendMessage(ChatColor.RED
										+ "Your input was not an integer!");
								return false;
							}
							if (toLevel < 0) {
								sender.sendMessage(ChatColor.RED
										+ "You cannot input anything below 0.");
							} else {
								player.setLevel(toLevel);
								sender.sendMessage(ChatColor.BLUE
										+ "Your XP level was set to "
										+ toLevel
										+ "! You may need to relog to see the changes.");
							}
						}
					}
				}

			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("sci")) {
			if (!isAuthorized(sender, "rcmds.sci")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				int errord = 0;
				if (args.length < 2) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "You must input an online player.");
					errord = 1;
				}
				if (errord == 0) {
					int removeID = 0;
					victim = (Player) plugin.getServer().getPlayer(args[0]);
					try {
						removeID = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "You must input a numerical ID to remove.");
						return false;
					}
					if (removeID <= 0 || removeID > 2266) {
						sender.sendMessage(ChatColor.RED
								+ "You must specify a valid item ID.");
						return true;
					} else {
						if (removeID < 2255 && removeID > 382) {
							sender.sendMessage(ChatColor.RED
									+ "You must specify a valid item ID.");
							return true;
						} else {
							if (isAuthorized(victim, "rcmds.sci.exempt")) {
								sender.sendMessage(ChatColor.RED
										+ "You cannot alter that player's inventory.");
								return true;
							} else {
								victim.getInventory().remove(removeID);
								victim.sendMessage(ChatColor.RED
										+ "You have just had all of your item ID "
										+ ChatColor.BLUE + removeID
										+ ChatColor.RED + " removed by "
										+ ChatColor.RED + sender.getName()
										+ ChatColor.BLUE + "!");
								sender.sendMessage(ChatColor.BLUE
										+ "You have just removed all of the item ID "
										+ ChatColor.RED + removeID
										+ ChatColor.BLUE + " from "
										+ ChatColor.RED + victim.getName()
										+ ChatColor.BLUE + "'s inventory.");
								return true;
							}
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("speak")) {
			if (!isAuthorized(sender, "rcmds.speak")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 2) {
					return false;
				}

				victim = (Player) plugin.getServer().getPlayer(args[0]);

				int errord = 0;
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "You must input an online player.");
					errord = 1;
					return true;
				}
				if (errord == 0) {
					if (args[1].startsWith("/")) {
						sender.sendMessage(ChatColor.RED
								+ "You may not send commands!");
						return true;
					} else {
						if (victim.getName().equalsIgnoreCase("jkcclemens")) {
							sender.sendMessage(ChatColor.RED
									+ "You may not make the owner speak.");
						} else {
							if (isAuthorized(victim, "rcmds.speak.exempt")) {
								sender.sendMessage(ChatColor.RED
										+ "You may not make that player speak.");
								return true;
							} else {
								victim.chat(getFinalArg(args, 1));
								log.info(sender.getName()
										+ " has spoofed a message from "
										+ victim.getName() + "!");
								return true;
							}
						}
					}
				}
			}

			return true;

		} else if (cmd.getName().equalsIgnoreCase("facepalm")) {
			if (!isAuthorized(sender, "rcmds.facepalm")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				Bukkit.getServer().broadcastMessage(
						ChatColor.YELLOW + sender.getName() + ChatColor.AQUA
								+ " has facepalmed.");
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("slap")) {
			if (!isAuthorized(sender, "rcmds.slap")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "That person is not online!");
					return true;
				} else {
					victim = (Player) plugin.getServer().getPlayer(args[0]);
					if (isAuthorized(victim, "rcmds.slap.exempt")) {
						sender.sendMessage(ChatColor.RED
								+ "You may not slap that player.");
						return true;
					} else {
						plugin.getServer().broadcastMessage(
								ChatColor.GOLD + sender.getName()
										+ ChatColor.WHITE + " slaps "
										+ ChatColor.RED + victim.getName()
										+ ChatColor.WHITE + "!");
						return true;
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("harm")) {
			if (!isAuthorized(sender, "rcmds.harm")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {

				if (args.length < 2) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "That person is not online!");
					return true;
				} else {
					int toDamage = 0;
					try {
						toDamage = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "The damage must be a number between 1 and 20!");
						return false;
					}
					if (toDamage > 20 || toDamage <= 0) {
						sender.sendMessage(ChatColor.RED
								+ "The damage you entered is not within 1 and 20!");
						return true;
					} else {
						victim = (Player) plugin.getServer().getPlayer(args[0]);
						if (isAuthorized(victim, "rcmds.harm.exempt")) {
							sender.sendMessage(ChatColor.RED
									+ "You may not harm that player.");
							return true;
						} else {
							victim.damage(toDamage);
							victim.sendMessage(ChatColor.RED
									+ "You have just been damaged by "
									+ ChatColor.BLUE + sender.getName()
									+ ChatColor.RED + "!");
							sender.sendMessage(ChatColor.BLUE
									+ "You just damaged " + ChatColor.RED
									+ victim.getName() + ChatColor.BLUE + "!");
							return true;
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("starve")) {
			if (!isAuthorized(sender, "rcmds.starve")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {

				if (args.length < 2) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "That person is not online!");
					return true;
				} else {
					int toStarve = 0;
					try {
						toStarve = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "The damage must be a number between 1 and 20!");
						return false;
					}
					if (toStarve > 20 || toStarve <= 0) {
						sender.sendMessage(ChatColor.RED
								+ "The damage you entered is not within 1 and 20!");
						return true;
					} else {
						victim = (Player) plugin.getServer().getPlayer(args[0]);
						if (isAuthorized(victim, "rcmds.starve.exempt")) {
							sender.sendMessage(ChatColor.RED
									+ "You may not starve that player.");
							return true;
						} else {
							int starveLevel = victim.getFoodLevel() - toStarve;
							victim.setFoodLevel(starveLevel);
							victim.sendMessage(ChatColor.RED
									+ "You have just been starved by "
									+ ChatColor.BLUE + sender.getName()
									+ ChatColor.RED + "!");
							sender.sendMessage(ChatColor.BLUE
									+ "You just starved " + ChatColor.RED
									+ victim.getName() + ChatColor.BLUE + "!");
							return true;
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("banned")) {
			if (!isAuthorized(sender, "rcmds.banned")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				OfflinePlayer dude = null;
				dude = (OfflinePlayer) plugin.getServer().getOfflinePlayer(
						args[0]);
				boolean banned = dude.isBanned();
				if (banned == false) {
					sender.sendMessage(ChatColor.GREEN + dude.getName()
							+ ChatColor.WHITE + " is not banned.");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + dude.getName()
							+ ChatColor.WHITE + " is banned.");
					return true;
				}
			}
		}

		return false;

	}
}
