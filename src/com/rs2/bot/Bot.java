package com.rs2.bot;

import java.nio.channels.SelectionKey;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;

public class Bot extends Player{

	public Bot(SelectionKey key, String username, String password) {
		super(null);
        isBot = true;
        setUsername(NameUtil.uppercaseFirstLetter(username));
        setUsernameAsLong(NameUtil.nameToLong(getUsername().toLowerCase()));
        setPw(password);
        setPassword(password);
        setHost("127.0.0.1");
		loginTime = System.currentTimeMillis();
		regionEnterTime = loginTime;
		setMagicId(Constants.MAGIC_ID);
		setClientVersion(Constants.CLIENT_VERSION);
        setLoginStage(LoginStages.AWAITING_LOGIN_COMPLETE);
        try {
			if(beginLogin()){
	        	test();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createNewBot(String username, String password) {
		Bot bot = new Bot(null, username, password);
	}
	
	public void setStartingDetails(){
    	setPosition(new Position(Constants.RESPAWN_X, Constants.RESPAWN_Y, Constants.RESPAWN_Z));
		randomiseLook();
		skipTutorial();
		completeQuests();
		getActionSender().removeInterfaces();
	}
	
	public void removeFromGame(){
		getActionSender().sendLogout();
		disconnect();
	}
	
	public void test(){
		final Bot bot = this;
		//bot.walkTo(3241, 3201, true);
		final Tick timer1 = new Tick(10) {
            @Override
            public void execute() {
            	
            	
            	String names[] = {"faggot", "bitch","cunt","fuckhead","dick mongler","knob shiner","cock spinner","lil bitch","shit pker","dyslexic 4 year old"};
            	bot.getUpdateFlags().setForceChatMessage("Austin is a "+names[Misc.randomMinusOne(names.length)]+".");
            	bot.getMovementHandler().setRunToggled(true);
            	bot.walkTo(bot.getPosition().getX()+Misc.random_(20)-10, bot.getPosition().getY()+Misc.random_(20)-10, true);
            }
		};
        World.getTickManager().submit(timer1);
	}
	
}
