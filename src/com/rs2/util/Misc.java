package com.rs2.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.Position;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.clip.Rangable;
import com.rs2.util.clip.Region;

/**
 * A collection of miscellaneous utility methods and constants.
 * 
 * @author blakeman8192
 */
public class Misc {

	private static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']' };

	private static char decodeBuf[] = new char[4096];

	private static Random random = new Random();
	
	public static int coords2Region(int x, int y){
		int rx = x >> 6;
		int ry = y >> 6;
		int region = ry + (rx * 256);
		return region;
	}
	
	public static int getActualValue(int x){
		int realValue = (int) Math.pow(2, x);
		return realValue;
	}
	
    public static String formatDuration(long seconds) {
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
	
	public static void writeSettings() throws IOException {
		Server.watchedItems.clear();
		for (Item items : Server.watchedItems2) {
			Server.watchedItems.add(items);
		}
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("./data/watched-items.dat")));
		out.writeInt(Server.watchedItems.size());
		for (Item items : Server.watchedItems) {
			out.writeInt(items.getId());
			out.writeInt(items.getCount());
		}
		out.close();
	}
	
	public static void readSettings() throws IOException {
		try{
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("./data/watched-items.dat")));
		int amt = in.readInt();
		//System.out.println(amt);
		for (int i = 0; i < amt; i++) {
			int id = in.readInt();
			int count = in.readInt();
			//System.out.println(count);
			Item itm = new Item(id, count);
			int c = 0;
			for (Item items : Server.watchedItems) {
				if(id == items.getId()){
					Server.watchedItems.set(c, itm);
					break;
				}
				c++;
			}
		}
		in.close();
		}catch(Exception ex){
			
		}
	}
	
	public static String MACtoString(byte[] mac){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
		return sb.toString();
	}
	
	public static void addWatchedItem(Item itm) {
		int c = 0;
		for (Item items : Server.watchedItems2) {
			if(itm.getId() == items.getId()){
				int a = items.getCount()+itm.getCount();
				itm.setCount(a);
				Server.watchedItems2.set(c, itm);
				break;
			}
			c++;
		}
	}
	
	public static void checkWatchedItems() {
		for (Item items : Server.watchedItems) {
			for (Item items2 : Server.watchedItems2) {
				if(items.getId() == items2.getId()){
					if(items.getCount() < items2.getCount()){
						System.out.println("DUPE ALERT!!!");
						System.out.println(items.getDefinition().getName()+ "("+items.getId()+") old: "+items.getCount()+" new: "+items2.getCount());
						System.out.println("DUPE ALERT!!!");
					}
				}
			}
		}
	}
	
	public static String formattedValue(long l){
		/*DecimalFormat myFormatter = new DecimalFormat("###,###,###,###");
		String output = myFormatter.format(l);
		return output;*/
		NumberFormat format = NumberFormat.getIntegerInstance(Locale.ENGLISH);
		return format.format(l);
	}
	
	public static String formatValue(int val){
		int val_ = Math.abs(val);
		String e = "";
		if(val_ >= 100000 && val_ < 10000000){
			e = "K";
			val = val/1000;
		}
		if(val_ >= 10000000){
			e = "M";
			val = val/1000000;
		}
		return val+""+e;
	}

	public static boolean contains(int[] array, int value) {
		for (int i : array) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

	public static String ucWords(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
				}
			}
		}
		return s;
	}

	public static String formatPlayerName(String str) {
		str = ucWords(str);
		str.replace("_", " ");
		return str;
	}

	public static int randomMinusOne(int range) {
		int number = (int) (Math.random() * range);
		return number < 0 ? 0 : number;
	}

	public static int random(int range) {
		int number = (int) (Math.random() * (range + 1));
		return number < 0 ? 0 : number;
	}
	
	public static int random_(int range) {
		int number = random.nextInt(range);
		return number;
	}
	
	public static int[] ShuffleArray(int[] array)
	{
	    int index, temp;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = array[index];
	        array[index] = array[i];
	        array[i] = temp;
	    }
	    return array;
	}

	public static String format(int num) {
		//return NumberFormat.getInstance().format(num);
		NumberFormat format = NumberFormat.getIntegerInstance(Locale.ENGLISH);
		return format.format(num);
	}

	public static boolean checkClip(Position first, Position second, boolean melee) {
		return checkClip(first.getX(), first.getY(), second.getX(), second.getY(), first.getZ(), melee);
	}
	
	public static boolean canBeSeen(Player player, Npc npc) {
		Position first = player.getPosition();
		Position second = npc.getPosition();
		if(first.withinDistance(second, 6)){
			if((player.getPosition().getX() > npc.getPosition().getX() && npc.getTravelFace() == Constants.EAST) || (player.getPosition().getX() <= npc.getPosition().getX() && npc.getTravelFace() == Constants.WEST) ||
					(player.getPosition().getY() > npc.getPosition().getY() && npc.getTravelFace() == Constants.NORTH) || (player.getPosition().getY() <= npc.getPosition().getY() && npc.getTravelFace() == Constants.SOUTH))
				return checkClip(first.getX(), first.getY(), second.getX(), second.getY(), first.getZ(), false);
		}
		return false;
	}

	public static boolean checkClip(int x, int y, int x2, int y2, int height, boolean melee) {
		if (melee) {
			return Region.canMove(x, y, x2, y2, height, 1, 1);
		} else {
			return Rangable.canMove(x, y, x2, y2, height, 1, 1);
		}
		/*int x3 = x, y3 = y;
		// Algorithm starts here
		int w = x2 - x;
		int h = y2 - y;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0) {
			dx1 = -1;
		} else if (w > 0) {
			dx1 = 1;
		}
		if (h < 0) {
			dy1 = -1;
		} else if (h > 0) {
			dy1 = 1;
		}
		if (w < 0) {
			dx2 = -1;
		} else if (w > 0) {
			dx2 = 1;
		}
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0) {
				dy2 = -1;
			} else if (h > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			x3 = x;
			y3 = y;
			numerator += shortest;
			if (x == x2 && y == y2) {
				return true;
			}
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
		return true;*/
	}

	public static final boolean goodDistance(Position pos1, Position pos2, int distance) {
		if (pos1 == null || pos2 == null) {
			return false;
		}
		return goodDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), distance) && pos1.getZ() == pos2.getZ();
	}

	public static final boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
        int deltaX = objectX - playerX;
        int deltaY = objectY - playerY;
        int trueDistance = ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
        return trueDistance <= distance;
	}
	
	public static final boolean goodDistanceNoDiagonal(int objectX, int objectY, int playerX, int playerY, int distance) {
		if(objectX != playerX && objectY != playerY)
			return false;
        int deltaX = objectX - playerX;
        int deltaY = objectY - playerY;
        int trueDistance = ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
        return trueDistance <= distance;
	}

	public static final boolean goodDistanceParallel(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if (objectX == playerX || objectY == playerY) {
					if (objectX + i == playerX && (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
						return true;
					} else if (objectX - i == playerX && (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
						return true;
					} else if (objectX == playerX && (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
						return true;
					}
				}
			}
		}
		return false;
	}
    
    public static boolean isGoodDistanceObject(int objectX, int objectY, int x, int y, int objectXSize, int objectYSize, int height) {
        Position position = goodDistanceObject(objectX, objectY, x, y, objectXSize, objectYSize, height);
        return position != null;
    }
    
    
    /*
    Returns the position of the object that you are close to
     */

    @SuppressWarnings("unused")
	private static boolean TESTED = false;

	public static Position goodDistanceObject(int objectX, int objectY, int playerX, int playerY, int objectXSize, int objectYSize, int z) {
		if (objectXSize < 1 && objectYSize < 1) {
			if (playerX == objectX && playerY == objectY) {
                return new Position(objectX, objectY, z);
            }
            return null;
		}
		if (objectXSize == 1 && objectYSize == 1) {
			if (goodDistance(playerX, playerY, objectX, objectY, 1)) {
                return new Position(objectX, objectY, z);
            }
            return null;
		}
        int maxObjX = objectX+objectXSize-1;
        int maxObjY = objectY+objectYSize-1;
        
        Position playerPos = new Position(playerX, playerY, z);
        for (int x = objectX; x <= maxObjX; x++) {
            for (int y = objectY; y <= maxObjY; y++) {
                Position pos = new Position(x, y, z);
                if (goodDistance(pos, playerPos, 1)) {
                    if (pos.isPerpendicularTo(playerPos)) {
                        return pos;
                    }
                }
            }
        }
		/*if (objectXSize > objectYSize) {
			for (int x1 = objectX; x1 <= objectX + objectXSize - 1; x1++) {
				for (int y1 = objectY; y1 <= objectY + objectYSize - 1; y1++) {
					if (goodDistance(x1, y1, playerX, playerY, 1)) {
						return new Position(x1, y1, height);
					}
				}
			}
		} else {
			for (int y1 = objectY; y1 <= objectY + objectYSize - 1; y1++) {
				for (int x1 = objectX; x1 <= objectX + objectXSize - 1; x1++) {
					if (goodDistance(x1, y1, playerX, playerY, 1)) {
						return new Position(x1, y1, height);
					}
				}
			}
		} */
		return null;
	}

	public static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for (byte element : data) {
			int num = (element & 0xFF) * n;
			value += num;
			if (n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}

	/**
	 * Returns the delta coordinates. Note that the returned Position is not an
	 * actual position, instead it's values represent the delta values between
	 * the two arguments.
	 * 
	 * @param a
	 *            the first position
	 * @param b
	 *            the second position
	 * @return the delta coordinates contained within a position
	 */
	public static Position delta(Position a, Position b) {
		return new Position(b.getX() - a.getX(), b.getY() - a.getY());
	}

	public static int getDistance(Position a, Position b) {
		int deltaX = b.getX() - a.getX();
		int deltaY = b.getY() - a.getY();
		return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}

	/**
	 * Calculates the direction between the two coordinates.
	 * 
	 * @param dx
	 *            the first coordinate
	 * @param dy
	 *            the second coordinate
	 * @return the direction
	 */
	public static int direction(int dx, int dy) {
		if (dx < 0) {
			if (dy < 0) {
				return 5;
			} else if (dy > 0) {
				return 0;
			} else {
				return 3;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				return 7;
			} else if (dy > 0) {
				return 2;
			} else {
				return 4;
			}
		} else {
			if (dy < 0) {
				return 6;
			} else if (dy > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public static int direction(int srcX, int srcY, int destX, int destY) {
		int dx = destX - srcX, dy = destY - srcY;
		// a lot of cases that have to be considered here ... is there a more
		// sophisticated (and quick!) way?
		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy) {
					return 11;
				} else if (dx > dy) {
					return 9;
				} else {
					return 10; // dx == dy
				}
			} else if (dy > 0) {
				if (-dx < dy) {
					return 15;
				} else if (-dx > dy) {
					return 13;
				} else {
					return 14; // -dx == dy
				}
			} else { // dy == 0
				return 12;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy) {
					return 7;
				} else if (dx > -dy) {
					return 5;
				} else {
					return 6; // dx == -dy
				}
			} else if (dy > 0) {
				if (dx < dy) {
					return 1;
				} else if (dx > dy) {
					return 3;
				} else {
					return 2; // dx == dy
				}
			} else { // dy == 0
				return 4;
			}
		} else { // dx == 0
			if (dy < 0) {
				return 8;
			} else if (dy > 0) {
				return 0;
			} else { // dy == 0
				return -1; // src and dest are the same
			}
		}
	}

	public static String formatNumber(double number) {
		NumberFormat format = NumberFormat.getIntegerInstance(Locale.ENGLISH);
		return format.format(number);
	}

	public static Random getRandom() {
		return random;
	}

	/**
	 * A simple logging utility that prefixes all messages with a timestamp.
	 * 
	 * @author blakeman8192
	 */
	public static class TimestampLogger extends PrintStream {

		private BufferedWriter writer;
		private DateFormat df = new SimpleDateFormat();

		/**
		 * The OutputStream to log to.
		 * 
		 * @param out
		 */
		public TimestampLogger(OutputStream out, String file) throws IOException {
			super(out);
			writer = new BufferedWriter(new FileWriter(file, true));
		}

		public TimestampLogger(OutputStream out) {
			super(out);
		}

		@Override
		public void println(String msg) {
			msg = "[" + df.format(new Date()) + "]: " + msg;
			super.println(msg);
			log(msg);
		}

		/**
		 * Logs the message to the log file.
		 * 
		 * @param msg
		 *            the message
		 */
		private void log(String msg) {
			try {
				if (writer == null) {
					return;
				}
				writer.write(msg);
				writer.newLine();
				writer.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * A simple timing utility.
	 * 
	 * @author blakeman8192
	 */
	public static class Stopwatch {

		/** The cached time. */
		private long time = System.currentTimeMillis();

		/**
		 * Resets this stopwatch.
		 */
		public void reset() {
			time = System.currentTimeMillis();
		}

		/**
		 * Returns the amount of time elapsed (in milliseconds) since this
		 * object was initialized, or since the last call to the "reset()"
		 * method.
		 * 
		 * @return the elapsed time (in milliseconds)
		 */
		public long elapsed() {
			return System.currentTimeMillis() - time;
		}
	}

	public static String intToString(int intToChange) {
		if (intToChange == 1) {
			return "first";
		} else if (intToChange == 2) {
			return "second";
		} else if (intToChange == 3) {
			return "third";
		} else if (intToChange == 4) {
			return "fourth";
		}
		return "first";
	}

	public static int getDayOfYear() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int days = 0;
		int[] daysOfTheMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			daysOfTheMonth[1] = 29;
		}
		days += c.get(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < daysOfTheMonth.length; i++) {
			if (i < month) {
				days += daysOfTheMonth[i];
			}
		}
		return days;
	}

	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public static void loadScripts(File directory) {
		try {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("jruby");
			if (!directory.exists() || !directory.isDirectory()) {
				throw new IllegalArgumentException("Missing scripts folder! " + directory.getAbsolutePath());
			}
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					loadScripts(file);
				} else {
					if (file.getName().endsWith(".rb")) {
						engine.eval(new FileReader(file));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

    public static String durationFromTicks(long ticks, boolean shortened) {
        long seconds = ticksToSeconds(ticks);
        return durationFromLong(seconds, shortened);
    }

    public static String durationFromLong(long duration, boolean shortened) {
        int seconds = (int) Math.ceil((double)duration % 60d);
        int minutes = (int) (duration/ 60) % 60;
        int hours = (int) (duration/ 60 / 60) % 24;
        int days = (int) (duration / 60 / 60 / 24);

        boolean forceShowSeconds = false;
        if (days == 0 && hours == 0 && minutes == 0 && seconds == 0)
            forceShowSeconds = true;

        return displayDuration(shortened, forceShowSeconds, days, hours, minutes, seconds);
    }

    private static String displayDuration(boolean shortened, boolean forceShowSeconds, int days, int hours, int minutes, int seconds) {
        return (days > 0 ? days + ""+(shortened ? "d" : " days")+" " : "") + (hours > 0 ? hours + ""+(shortened ? "h" : " hours")+" " : "")
                + (minutes > 0 ? minutes + ""+(shortened ? "m" : " minutes")+" " : "") + ((seconds > 0 || forceShowSeconds) ? seconds + ""+(shortened ? "s" : " seconds")+"" : "");
    }


    public static long ticksToSeconds(long ticks) {
        long seconds = (long)Math.ceil(ticks * 600d / 1000d);
        return seconds;
    }

    public static long secondsToTicks(long seconds) {
        return (long)Math.ceil(seconds * 1000d / 600d);
    }

}
