package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.util.Logger;
import com.rs2.util.NameUtil;
import com.rs2.util.TextUtil;

public class PrivateMessaging {
	
	private final Player player;
	private int lastPrivateMessageId = 1;
	
	public PrivateMessaging(Player player) {
		this.player = player;
	}
	
	public void sendPMOnLogin() {
		player.getActionSender().sendPMServer(2);
		refresh(false);
	}

	public void refresh(boolean logout) {
		for (int i = 0; i < player.getFriends().length; i ++) {
			if (player.getFriends()[i] == 0) {
				continue;
			}
			player.getActionSender().sendFriendList(player.getFriends()[i], checkOnlineStatus(player.getFriends()[i]));
		}
        long name = player.getUsernameAsLong();
		//int world = logout ? 0 : checkOnlineStatus(name);
        int world = 0;
		for (Player players : World.getPlayers()) {
			if (players == null)
				continue;
			if (players.getPrivateMessaging().contains(players.getFriends(), name)) {
				if(logout || (player.getPrivateChatMode() == 2) || (player.getPrivateChatMode() == 1 && !player.getPrivateMessaging().contains(player.getFriends(), players.getUsernameAsLong())))
					world = 0;
				else
					world = 1;
				players.getActionSender().sendFriendList(name, world);
			}
		}
	}
	
	public void addToFriendsList(long name) {
		if (getCount(player.getFriends()) >= 200) {
			player.getActionSender().sendMessage("Your friends list is full.");
			return;
		}
		if (contains(player.getFriends(), name)) {
			player.getActionSender().sendMessage(""+ NameUtil.longToName(name) + " is already on your friends list.");
			return;
		}
		int slot = getFreeSlot(player.getFriends());
		player.getFriends()[slot] = name;
		//player.getActionSender().sendFriendList(name, checkOnlineStatus(name));
		refresh(false);
	}
	
	public void addToIgnoresList(long name) {
		if (getCount(player.getIgnores()) >= 100) {
			player.getActionSender().sendMessage("Your ignores list is full.");
			return;
		}
		if (contains(player.getIgnores(), name)) {
			player.getActionSender().sendMessage(""+ NameUtil.longToName(name) + " is already on your ignores list.");
			return;
		}
		int slot = getFreeSlot(player.getIgnores());
		player.getIgnores()[slot] = name;
	}
	
	public boolean isIgnoringPlayer(long name) {
		for (long p : player.getIgnores()) {
			if (p == name) {
				return true;
			}
		}
		return false;
	}

	public void sendPrivateMessage(Player from, long to, byte[] message,
			int messageSize) {
		for (Player p : World.getPlayers()) {
			if (p != null && p.getUsernameAsLong() == to && !p.getPrivateMessaging().isIgnoringPlayer(player.getUsernameAsLong())) {
				int rights = from.getStaffRights();
				if(from.isDonator() && rights == 0)
					rights += 10;
				p.getActionSender().sendPrivateMessage(from.getUsernameAsLong(), rights, message, messageSize);
				String uncompressed = TextUtil.uncompress(message, messageSize);
				if(Constants.LOG_PRIVATE_CHAT)
					Logger.logEvent(System.currentTimeMillis()+"§"+from.getUsername()+"§"+from.getPosition().getX()+"§"+from.getPosition().getY()+"§"+from.getPosition().getZ()+"§"+p.getUsername()+"§"+p.getPosition().getX()+"§"+p.getPosition().getY()+"§"+p.getPosition().getZ()+"§"+uncompressed, "private chat");
				break;
			}
		}
	}
	
	public void removeFromList(long[] person, long name) {
		for (int i = 0; i < person.length; i ++) {
			if (person[i] == name) {
				person[i] = 0;
				break;
			}
		}
		refresh(false);
	}

	private int checkOnlineStatus(long friend) {
		for (Player p : World.getPlayers()) {
			if (p != null) {
				if (p.getUsernameAsLong() == player.getUsernameAsLong())
					continue;
				if (p.getUsernameAsLong() == friend) {
					//System.out.println(player.getUsername()+" "+player.getPrivateChatMode()+" - "+p.getUsername()+" "+p.getPrivateChatMode());
					if(p.getPrivateChatMode() == 0 || (p.getPrivateChatMode() == 1 && p.getPrivateMessaging().contains(p.getFriends(), player.getUsernameAsLong())))
						return 1;
					else
						return 0;
				}
			}
		}
		return 0;
	}
	
	public boolean contains(long[] person, long name) {
		for (int i = 0; i < person.length; i++) {
			if (person[i] == name) {
				return true;
			}
		}
		return false;
	}
	
	public int getCount(long[] name) {
		int count = 0;
		for (long names : name) {
			if (names > 0) {
				count ++;
			}
		}
		return count;
	}
	
	public int getFreeSlot(long[] person) {
		for (int i = 0; i < person.length; i ++) {
			if (person[i] == 0) {
				return i;
			}
		}
		return -1;
	}

	public int getLastPrivateMessageId() {
		return lastPrivateMessageId ++;
	}

}
