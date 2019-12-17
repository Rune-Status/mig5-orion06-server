package com.rs2.model.content.minigames.fightcaves;

import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;

//import com.rs2.model.content.combat.HealersCombatScript;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 5/17/12 Time: 8:27 PM To change
 * this template use File | Settings | File Templates.
 */
public class WavesHandling {
    public static final int TZ_KIH = 2627;
    public static final int TZ_KEK_SPAWN = 2629;
    public static final int TZ_KEK = 2630;
    public static final int TOK_XIL = 2631;
    public static final int YT_MEJ_KOT = 2741;
    public static final int KET_ZEK = 2743;
    public static final int TZTOK_JAD = 2745;
    //public static final Npc YT_HURKO = new Npc(World.getDefinitions()[2746], 2746, new HealersCombatScript(10, 2637, 5, new Graphic(-1, 100), new Graphic(444, 100)));


    public static final int[][] waves = {
            {TZ_KIH},//1
            {TZ_KIH, TZ_KIH},//2
            {TZ_KEK},//3
            {TZ_KEK, TZ_KIH},//4
            {TZ_KEK, TZ_KIH, TZ_KIH},//5
            {TZ_KEK, TZ_KEK},//6
            {TOK_XIL},//7
            {TOK_XIL, TZ_KIH},//8
            {TOK_XIL, TZ_KIH, TZ_KIH},//9
            {TOK_XIL, TZ_KEK},//10
            {TOK_XIL, TZ_KEK, TZ_KIH},//11
            {TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},//12
            {TOK_XIL, TZ_KEK, TZ_KEK},//13
            {TOK_XIL, TOK_XIL},//14
            {YT_MEJ_KOT},//15
            {YT_MEJ_KOT, TZ_KIH},//16
            {YT_MEJ_KOT, TZ_KIH, TZ_KIH},//17
            {YT_MEJ_KOT, TZ_KEK},//18
            {YT_MEJ_KOT, TZ_KEK, TZ_KIH},//19
            {YT_MEJ_KOT, TZ_KEK, TZ_KIH, TZ_KIH},//20
            {YT_MEJ_KOT, TZ_KEK, TZ_KEK},//21
            {YT_MEJ_KOT, TOK_XIL},//22
            {YT_MEJ_KOT, TOK_XIL, TZ_KIH},//23
            {YT_MEJ_KOT, TOK_XIL, TZ_KIH, TZ_KIH},//24
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK},//25
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH},//26
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},//27
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KEK},//28
            {YT_MEJ_KOT, TOK_XIL, TOK_XIL},//29
            {YT_MEJ_KOT, YT_MEJ_KOT},//30
            {KET_ZEK},//31
            {KET_ZEK, TZ_KIH},//32
            {KET_ZEK, TZ_KIH, TZ_KIH},//33
            {KET_ZEK, TZ_KEK},//34
            {KET_ZEK, TZ_KEK, TZ_KIH},//35
            {KET_ZEK, TZ_KEK, TZ_KIH, TZ_KIH},//36
            {KET_ZEK, TZ_KEK, TZ_KEK},//37
            {KET_ZEK, TOK_XIL},//38
            {KET_ZEK, TOK_XIL, TZ_KIH},//39
            {KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH},//40
            {KET_ZEK, TOK_XIL, TZ_KEK},//41
            {KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH},//42
            {KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},//43
            {KET_ZEK, TOK_XIL, TZ_KEK, TZ_KEK},//44
            {KET_ZEK, TOK_XIL, TOK_XIL},//45
            {KET_ZEK, YT_MEJ_KOT},//46
            {KET_ZEK, YT_MEJ_KOT, TZ_KIH},//47
            {KET_ZEK, YT_MEJ_KOT, TZ_KIH, TZ_KIH},//48
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK},//49
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, TZ_KIH},//50
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, TZ_KIH, TZ_KIH},//51
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, TZ_KEK},//52
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL},//53
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KIH},//54
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KIH, TZ_KIH},//55
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK},//56
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH},//57
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},//58
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KEK},//59
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TOK_XIL},//60
            {KET_ZEK, YT_MEJ_KOT, YT_MEJ_KOT},//61
            {KET_ZEK, KET_ZEK},//62
            {TZTOK_JAD}//63
            };

    public static void spawnWave(Player player, int wave){
    	int currentWave = (wave+1);
    	player.getActionSender().sendMessage("Wave "+currentWave+"/"+waves.length);
    	player.clearFightCaveMonsters();
    	for(int i : waves[wave]){
    		Npc npc = new Npc(i);
    		player.addFightCaveMonster(npc);
    		NpcLoader.spawnNpcAlwaysFollow(player, npc, MinigameAreas.randomPosition(FightCaveAreas.getRandomSpawningArea(player.getPosition())), false, null);
    		if(currentWave == waves.length){
    			player.getDialogue().setLastNpcTalk(2617);
    			player.getDialogue().sendNpcChat("Look out, here comes TzTok-Jad!",Dialogues.CONTENT);
    			player.getDialogue().endDialogue();
    		}
    		//System.out.println("spawned " + npc.getPosition());
    	}
    	player.getActionSender().sendMessage("Enemies to kill: "+player.getFightCaveMonsters().size());
    }


    public static void spawnFinalWave(Player player){
        //NpcLoader.spawnNpc(player, TZTOK_JAD, true, false);
        //NpcLoader.spawnNpc(TZTOK_JAD, YT_HURKO, TZTOK_JAD.getPosition(), false);
    }
}
