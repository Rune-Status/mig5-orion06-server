package com.rs2.model.content.questing;

import java.awt.Color;

import com.rs2.model.players.Player;

public abstract class Quest extends SpecialEvent{

	private int rewardQP;
	
	public Quest(int id) {
		super(id);
	}
	
	public QuestDefinition getDefinition() {
		return QuestDefinition.forId(this.getId());
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		String text[] = {"Quest not added yet!"};
			return text;
	}
	
	public int getRewardQP(){
		return rewardQP;
	}
	
	public void setRewardQP(int qp){
		this.rewardQP = qp;
	}
	
	public void setQuestStage(final Player player, final int stage){
		if(player.questStage[this.getId()] == 1)
			return;
		player.questStage[this.getId()] = stage;
	}
	
	public void addQuestStage(final Player player, final int stage){
		if(player.questStage[this.getId()] == 1)
			return;
		player.questStage[this.getId()] += stage;
	}

	public void questCompleted_2(final Player player) {
		player.getActionSender().recolorComponent(this.getDefinition().getButton(), Color.GREEN);
		player.questStage[this.getId()] = 1;
		player.getActionSender().sendMessage("Congratulations! Quest Complete!");
		player.getQuestHandler().refreshQuestPoints();
	}
	
	public void questCompleted_defaultStuff(final Player player) {
		player.getActionSender().moveInterfaceComponent(12145, 0, 0);
		player.getActionSender().sendString("You have completed "+this.getDefinition().getName()+"!", 12144);
		player.getActionSender().sendQuickSong(238, 320);
		player.getActionSender().sendString(""+player.getQuestPoints(), 12147);
	}
	
	public void questCompleted(final Player player) {
	}
	
	public void questStarted(final Player player){
		player.getActionSender().recolorComponent(this.getDefinition().getButton(), Color.YELLOW);
		player.questStage[this.getId()] = 2;
	}
}
