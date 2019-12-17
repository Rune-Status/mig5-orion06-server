package com.rs2.model.content.skills;

import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 17:15 To change
 * this template use File | Settings | File Templates.
 */
public class SkillsX {
	public static boolean handleXButtons(Player player, int buttonId) {
		switch (buttonId) {
			case 2414 : // bronze x
			case 3988 : // iron x
			case 3992 : // silver x
			case 3996 : // steel x
			case 4000 : // gold x
			case 4158 : // mith x
			case 7442 : // addy x
			case 7447 : // rune x
			case 8886 :
			case 8946 :
			case 8950 :
			case 8954 :
			case 8906 :
			case 8910 :
			case 8914 :
			case 11471 :
			case 11475 :
			case 12397 :
			case 12401 :
			case 12405 :
			case 6200 :
			case 12409 :
			case 8918 :
			case 8958 :
			case 8962 :
			case 1748 :
			case 8890 :
			case 8894 :
			case 8871 :
			case 8875 :
			case 13718 :
            case 14801 :
            case 14802 :
            case 14803 :
            case 14804 :
            case 14805 :
            case 14806 :
            case 14807 :
            case 14808 :
				player.getActionSender().openXInterface(buttonId);
				return true;
		}
		return false;
	}
}
