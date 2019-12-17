package com.rs2.model.content;

import java.util.ArrayList;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;
import com.rs2.util.Time;

public class DailyTask {

	int skill;
	int reqLvl;
	int finishAmount;
	int taskTarget;
	
	DailyTask(int skill, int reqLvl, int finishAmount, int taskTarget){
		this.skill = skill;
		this.reqLvl = reqLvl;
		this.finishAmount = finishAmount;
		this.taskTarget = taskTarget;
	}
	
	static DailyTask tasklist[] = {
		new DailyTask(Skill.FISHING, 1, 100, 317),//shrimps
		new DailyTask(Skill.FISHING, 20, 200, 335),//trout
		new DailyTask(Skill.FISHING, 40, 200, 377),//lobsters
		new DailyTask(Skill.MINING, 1, 100, 436),//copper
		new DailyTask(Skill.MINING, 1, 300, 1436),//rune essence
		new DailyTask(Skill.MINING, 30, 500, 453),//coal
		new DailyTask(Skill.SMITHING, 1, 100, 2349),//bronze bar
		new DailyTask(Skill.SMITHING, 15, 100, 2351),//iron bar
		new DailyTask(Skill.SMITHING, 40, 200, 2357),//gold bar
		new DailyTask(Skill.WOODCUTTING, 1, 50, 1511),//tree
		new DailyTask(Skill.WOODCUTTING, 15, 100, 1521),//oak
		new DailyTask(Skill.WOODCUTTING, 30, 200, 1519),//willow
		
		new DailyTask(Skill.FISHING, 76, 100, 383),//sharks
		new DailyTask(Skill.MINING, 55, 100, 447),//mithril
		new DailyTask(Skill.SMITHING, 50, 100, 2359),//mithril bar
		new DailyTask(Skill.WOODCUTTING, 45, 400, 1517),//maple
		
		new DailyTask(Skill.HERBLORE, 5, 30, 175),//anti-poison
		new DailyTask(Skill.HERBLORE, 22, 20, 127),//restore potion
		new DailyTask(Skill.HERBLORE, 26, 20, 3010),//energy potion
		new DailyTask(Skill.HERBLORE, 38, 10, 139),//prayer potion
		
		new DailyTask(Skill.ATTACK, 1, 100, 81),//cow
		new DailyTask(Skill.ATTACK, 25, 100, 117),//hill giant
		new DailyTask(Skill.ATTACK, 60, 50, 941),//green dragon
		new DailyTask(Skill.ATTACK, 80, 100, 55),//blue dragon
		new DailyTask(Skill.ATTACK, 80, 100, 53),//red dragon
		new DailyTask(Skill.ATTACK, 80, 50, 54),//black dragon
		new DailyTask(Skill.ATTACK, 35, 100, 112),//moss giant
		new DailyTask(Skill.ATTACK, 45, 100, 111),//ice giant
		new DailyTask(Skill.ATTACK, 70, 100, 110),//fire giant
		
		new DailyTask(Skill.HERBLORE, 60, 10, 187),//weapon poison
		new DailyTask(Skill.WOODCUTTING, 75, 400, 1513),//magic
		
		new DailyTask(Skill.ATTACK, 90, 50, 1590),//bronze dragon
		new DailyTask(Skill.ATTACK, 100, 50, 1591),//iron dragon
		new DailyTask(Skill.ATTACK, 110, 50, 1592),//steel dragon

	};
	
	public int getTaskTarget(){
		return taskTarget;
	}
	
	public String getTaskTargetAsString(){
		if(skill != Skill.ATTACK)
			return new Item(taskTarget).getDefinition().getName();
		else
			return new Npc(taskTarget).getDefinition().getName();
	}
	
	public int getReqAmount(){
		return finishAmount;
	}
	
	public void getReward(final Player player){
		if(skill != Skill.ATTACK){
			Item a = new Item(taskTarget, 1);
			a = new Item(a.getDefinition().getNotedId(), finishAmount/3);
			if(a.getId() == 1437 && player.getSkill().getPlayerLevel(Skill.MINING) >= 30){
				player.getInventory().addItemOrDrop(new Item(7937, a.getCount()));
			} else
				player.getInventory().addItemOrDrop(a);
		} else {
			Item[] b = null;
			Item[] always;
			for(int i = 0; i < (finishAmount/3); i++){
				always = NpcDropController.getDrops(player, taskTarget, true);
				if(b == null)
					b = always;
				else{
					for(int items = 0; items < b.length; items++){
						for(int Nitems = 0; Nitems < always.length; Nitems++){
							if(b[items].getId() == always[Nitems].getId()){
								b[items].setCount(b[items].getCount() + always[Nitems].getCount());
								continue;
							}
						}
					}
				}
			}
			for(int i = 0; i < b.length; i++){
				Item ab = b[i];
				ab = new Item(ab.getDefinition().getNotedId(), ab.getCount());
				player.getInventory().addItemOrDrop(ab);
			}
		}
	}
	
	public int getSkill(){
		return skill;
	}
	
	public int getReqLvl(){
		return reqLvl;
	}
	
	public static int getNewTask(final Player player){
		ArrayList<Integer> listOfSuitableTasks = new ArrayList<Integer>();
		int i = 0;
		for(DailyTask task : tasklist){
			int pLvl = 0;
			if(task.getSkill() != Skill.ATTACK)
				pLvl = player.getSkill().getPlayerLevel(task.getSkill());
			else
				pLvl = player.getCombatLevel();
			if(pLvl >= task.getReqLvl())
				listOfSuitableTasks.add(i);
			i++;
		}
		int i2 = Misc.random_(listOfSuitableTasks.size());
		return listOfSuitableTasks.get(i2);
	}
	
	public static DailyTask getDailyTask(final Player player){
		long lastTime = player.dailyTaskTime;
		long loginTime = player.loginTime;
		if(Time.daysBetween(lastTime, loginTime) > 0 || lastTime == 0){
			int taskId = getNewTask(player);
			player.dailyTaskId = taskId;
			player.dailyTaskAmount = 0;
			player.dailyTaskTime = loginTime;
			return tasklist[player.dailyTaskId];
		}else{
			return tasklist[player.dailyTaskId];
		}
	}
	
	public static String getDailyTaskAsString(final Player player){
		DailyTask task = player.getDailyTask();
		String action = "";
		if(player.dailyTaskAmount > task.getReqAmount())
			return "Todays task has been completed.";
		if(task.getSkill() == Skill.MINING)
			action = "Mine";
		else if(task.getSkill() == Skill.FISHING)
			action = "Catch";
		else if(task.getSkill() == Skill.SMITHING || task.getSkill() == Skill.HERBLORE)
			action = "Make";
		else if(task.getSkill() == Skill.WOODCUTTING)
			action = "Chop";
		else if(task.getSkill() == Skill.ATTACK)
			action = "Kill";
		return "Daily task: "+action+" "+player.dailyTaskAmount+"/"+task.getReqAmount()+" "+task.getTaskTargetAsString();
	}
	
	public void completed(final Player player){
		player.getActionSender().sendMessage("Daily task completed! Talk to Skill Master to claim your reward.");
	}
	
}
