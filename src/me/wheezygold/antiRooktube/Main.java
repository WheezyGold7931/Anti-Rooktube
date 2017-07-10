package me.wheezygold.antiRooktube;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import me.wheezygold.antiRooktube.common.JsonReader;
import me.wheezygold.antiRooktube.common.Util;
import me.wheezygold.antiRooktube.skript.CondRookFalse;
import me.wheezygold.antiRooktube.skript.CondRookTrue;
import me.wheezygold.antiRooktube.skript.ExprRookSpigot;
import me.wheezygold.metrics.Metrics;

public class Main extends JavaPlugin implements Listener {
	
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	
	public void loadConfiguration() {
		String _kickpath = "messages.kick";
		String _chatfiltermessagepath = "messages.chatfilter";
		String _chatfilterpath = "toggles.chatfilter";
		String _namepath = "values.rookname";
		getConfig().addDefault(_kickpath, "You have been removed by Anti-Rooktube!");
		getConfig().addDefault(_chatfilterpath, true);
		getConfig().addDefault(_chatfiltermessagepath, "You cannot discuss Rooktube/Rookcraft or anything related to him!");
		getConfig().addDefault(_namepath, "RookPvPz");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	@Override
	public void onEnable() {
		Util.sendCMsg("Loading Configuration...");
		loadConfiguration();
		Util.sendCMsg("Loaded Configuration...");
		Util.sendCMsg("Registering Events...");
		Bukkit.getPluginManager().registerEvents(this, this);
		Util.sendCMsg("Registered Events!");
		Util.sendCMsg("Loading Metrics...");
		Metrics metrics = new Metrics(this);
		String chatfilter = String.valueOf(getConfig().getBoolean("toggles.chatfilter"));
		metrics.addCustomChart(new Metrics.SimplePie("using_chat_filter") {
            @Override
            public String getValue() {
                return chatfilter;
            }
        });
		Util.sendCMsg("Loaded Metrics!");
		if (getServer().getPluginManager().getPlugin("Skript")!=null) {
			Util.sendCMsg("Skript has been found!");
			Util.sendCMsg("Registing Addon...");
			Skript.registerAddon(this);
			Util.sendCMsg("Registered Addon!");
			if (Skript.isAcceptRegistrations()) {
				Util.sendCMsg("Looks like Skript is looking for syntax so lets throw some shit at it...");
				Util.sendCMsg("Going to start to load the syntax...");
				Skript.registerCondition(CondRookFalse.class, "rook[ ][(craft|tube)] is a[n] (eclipse|java) (coder|developer)");
				Skript.registerCondition(CondRookTrue.class, "rook[ ][(craft|tube)] is not a[n] (eclipse|java) (coder|developer)");
				Skript.registerCondition(CondRookTrue.class, "rook[ ][(craft|tube)] (says|talks) [about] his server [is] not [being] a mineplex (copy|replica|clone|mirror)");
				Skript.registerCondition(CondRookTrue.class, "rook[ ][(craft|tube)] is a[n] anti md[(_5|-5|5)]");
				Skript.registerCondition(CondRookFalse.class, "rook[ ][(craft|tube)] is not a[n] anti md[(_5|-5|5)]");
				Skript.registerCondition(CondRookFalse.class, "rook[ ][(craft|tube)] is a[n] successful (person|server)");
				Skript.registerCondition(CondRookTrue.class, "rook[ ][(craft|tube)] is not a[n] successful (person|server)");
				Skript.registerCondition(CondRookTrue.class, "rook[ ][(craft|tube)] does bot download[s]");
				Skript.registerCondition(CondRookFalse.class, "rook[ ][(craft|tube)] does[n't] [not] bot download[s]");
				Skript.registerExpression(ExprRookSpigot.class, String.class, ExpressionType.PROPERTY, "rook[ ][(craft|tube)]['][s] spigot[ ][mc] [user]name");
				Util.sendCMsg("Loaded all of the Skript syntax!");
			} else {
				Util.sendCMsg("Skript is not looking to accept syntax/registrations. Did you restart the server?");
			}
		} else {
			Util.sendCMsg("Skript has not been found, so expect nothing to register.");
		}
		Util.sendCMsg("Anti-Rooktube(" + this.getDescription().getVersion() + ") by WheezyGold7931 has been loaded. Expect less Rook now!");
	}
	
	@Override
	public void onDisable() {
		Util.sendCMsg("Anti-Rooktube(" + this.getDescription().getVersion() + ") by WheezyGold7931 has been un-loaded. Shit, um...he is exists now.");
	}
	
	 @Override
	    public boolean onCommand(CommandSender sender,
	            Command command,
	            String label,
	            String[] args) {
	        if (command.getName().equalsIgnoreCase("anti-rooktube")) {
	        	if(sender instanceof Player) {
	        		Player p = (Player) sender;
	        		reloadConfig();
	        		Util.sendMsg(p, ChatColor.GREEN + "Reloaded Anti-Rooktube's config!");
	        		Util.sendMsg(p, ChatColor.LIGHT_PURPLE + "Grabbing Rook's current spigot name 1 sec...");
	        	} else {
	        		reloadConfig();
		        	sender.sendMessage(ChatColor.GREEN + "Reloaded Anti-Rooktube's config!");
		        	sender.sendMessage(ChatColor.LIGHT_PURPLE + "Grabbing Rook's current spigot name 1 sec...");
	        	}
	        	String spigotname = null;
				try {
					spigotname = JsonReader.grabRookSpigot(args);
				} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
	            sender.sendMessage(ChatColor.GREEN + "Rook's current spigot name is: " + ChatColor.RED + "" + ChatColor.BOLD + spigotname);
	            return true;
	        }
	        return false;
	    }
	 @EventHandler
	 public void PlayerLoginEvent(PlayerJoinEvent e) {
	        Player p = e.getPlayer();
	        if (p.getName().equalsIgnoreCase(getConfig().getString("values.rookname"))) {
	        	p.kickPlayer("\u00A7c\u00A7l" + this.getConfig().getString("messages.kick"));
	        	Util.sendCMsg("Rooktube (RookPvPz) has attempted to join the server but we kicked him for you.");
	        	//We shall not let them toggle this! Who needs him in your server?
	        	//If you want this toggleable contact me
	        }
	    }
	 @EventHandler
	 public void playerChat(AsyncPlayerChatEvent  e) {
		 if (getConfig().getBoolean("toggles.chatfilter") != false) {
			 if (e.getMessage().contains("rooktube") || e.getMessage().contains("rookcraft") || (e.getMessage().contains("rooktdm") || e.getMessage().contains("RookPvPz") || e.getMessage().contains("rook craft") || e.getMessage().contains("rook tube") || e.getMessage().contains("rook"))) {
				 e.setCancelled(true);
				 e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + this.getConfig().getString("messages.chatfilter"));
		 }
	 }
}

}