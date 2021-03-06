package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.DecimalFormat;
import java.util.Date;

public class CmdWhois implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdWhois(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("whois")) {
            if (!plugin.isAuthorized(cs, "rcmds.whois")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!t.isOnline() && !t.hasPlayedBefore()) {
                cs.sendMessage(ChatColor.RED + "That player has never played before!");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            DecimalFormat df = new DecimalFormat("#.##");
            String ip = pcm.getString("ip");
            String name = pcm.getString("name");
            String dispname = pcm.getString("dispname");
            cs.sendMessage(ChatColor.BLUE + "=====================");
            cs.sendMessage(ChatColor.BLUE + ((t.isOnline()) ? "Whois" : "Whowas") + " for " + ChatColor.GRAY + name);
            cs.sendMessage(ChatColor.BLUE + "Nickname: " + ChatColor.GRAY + dispname);
            cs.sendMessage(ChatColor.BLUE + "IP: " + ChatColor.GRAY + ip);
            cs.sendMessage(ChatColor.BLUE + "Is VIP: " + ChatColor.GRAY + BooleanUtils.toStringYesNo(pcm.getBoolean("vip")));
            cs.sendMessage(ChatColor.BLUE + "Is muted: " + ChatColor.GRAY + BooleanUtils.toStringYesNo(pcm.getBoolean("muted")));
            cs.sendMessage(ChatColor.BLUE + "Is frozen: " + ChatColor.GRAY + BooleanUtils.toStringYesNo(pcm.getBoolean("frozen")));
            cs.sendMessage(ChatColor.BLUE + "Is jailed: " + ChatColor.GRAY + BooleanUtils.toStringYesNo(pcm.getBoolean("jailed")));
            long timestamp = RUtils.getTimeStamp(t, "seen");
            String lastseen = (timestamp < 0) ? "unknown" : RUtils.formatDateDiff(timestamp);
            cs.sendMessage(ChatColor.BLUE + "Last seen:" + ChatColor.GRAY + ((t.isOnline()) ? " now" : lastseen));
            cs.sendMessage(ChatColor.BLUE + "First played:" + ChatColor.GRAY + RUtils.formatDateDiff(t.getFirstPlayed()));
            if (t.isOnline()) {
                Player p = (Player) t;
                cs.sendMessage(ChatColor.BLUE + "Gamemode: " + ChatColor.GRAY + p.getGameMode().name().toLowerCase());
                cs.sendMessage(ChatColor.BLUE + "Can fly: " + ChatColor.GRAY + BooleanUtils.toStringYesNo(p.getAllowFlight()));
                cs.sendMessage(ChatColor.BLUE + "Health/Hunger/Saturation: " + ChatColor.GRAY + p.getHealth() / 2 + ChatColor.BLUE + "/" + ChatColor.GRAY + p.getFoodLevel() / 2 + ChatColor.BLUE + "/" + ChatColor.GRAY + p.getSaturation() / 2);
                cs.sendMessage(ChatColor.BLUE + "Total Exp/Exp %/Level: " + ChatColor.GRAY + p.getTotalExperience() + ChatColor.BLUE + "/" + ChatColor.GRAY + df.format(p.getExp() * 100) + "%" + ChatColor.BLUE + "/" + ChatColor.GRAY + p.getLevel());
                cs.sendMessage(ChatColor.BLUE + "Item in hand: " + ChatColor.GRAY + RUtils.getItemName(p.getItemInHand()));
                cs.sendMessage(ChatColor.BLUE + "Alive for:" + ChatColor.GRAY + RUtils.formatDateDiff(new Date().getTime() - p.getTicksLived() * 50));
                Location l = p.getLocation();
                cs.sendMessage(ChatColor.BLUE + "Last position: " + "(" + ChatColor.GRAY + l.getX() + ChatColor.BLUE + ", " + ChatColor.GRAY + l.getY() + ChatColor.BLUE + ", " + ChatColor.GRAY + l.getZ() + ")");
                cs.sendMessage(ChatColor.BLUE + "Last world: " + ChatColor.GRAY + RUtils.getMVWorldName(l.getWorld()) + ChatColor.BLUE + " (" + ChatColor.GRAY + l.getWorld().getName() + ChatColor.BLUE + ")");
            } else {
                String lP = "lastposition.";
                World w = (pcm.isSet(lP + "world")) ? plugin.getServer().getWorld(pcm.getString(lP + "world")) : null;
                if (w != null) {
                    Location l = new Location(w, pcm.getDouble(lP + "x"), pcm.getDouble(lP + "y"), pcm.getDouble(lP + "z"));
                    cs.sendMessage(ChatColor.BLUE + "Last position: " + "(" + ChatColor.GRAY + l.getX() + ChatColor.BLUE + ", " + ChatColor.GRAY + l.getY() + ChatColor.BLUE + ", " + ChatColor.GRAY + l.getZ() + ")");
                    cs.sendMessage(ChatColor.BLUE + "Last world: " + ChatColor.GRAY + RUtils.getMVWorldName(l.getWorld()) + ChatColor.BLUE + " (" + ChatColor.GRAY + l.getWorld().getName() + ChatColor.BLUE + ")");
                }
            }
            cs.sendMessage(ChatColor.BLUE + "=====================");
            return true;
        }
        return false;
    }

}
