package me.wheezygold.antiRooktube.common;

import org.bukkit.entity.Player;

public class Util {
	
	public static void sendMsg(Player p, String m) {
		p.sendMessage(m);
	}
	
	public static void sendCMsg(String m) {
		System.out.println("[Anti-Rooktube] " + m);
	}

}
