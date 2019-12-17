package com.rs2.model.players;

import com.rs2.util.sql.SQLEngine;
import com.rs2.util.sql.SQLWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HighscoresManager {

    public static boolean debug;
    
    private static HighscoresManager singleton;
       
    public static void load() { 
        singleton = new HighscoresManager();
    }

	/**
	 * The main method that is called upon logout
     * @param player the player
     */
	public void savePlayer(final Player player) {
		System.out.println("Saving hiscores for " + player.getUsername() + " - UID: " + player.getUniqueId());
		SQLEngine.getGameDatabase().execute(new SQLWorker(SQLEngine.UPDATE_HIGH_SCORES) {
            @Override
            public ResultSet executeSQL(Connection c, PreparedStatement st) {
            	try {
                st.setInt(1, player.getUniqueId());
                st.setInt(2, player.getSkill().getTotalLevel());
                st.setDouble(3, (double)player.getSkill().getTotalXp());
                st.setDouble(4, player.getSkill().getExp()[0]);
                st.setDouble(5, player.getSkill().getExp()[1]);
                st.setDouble(6, player.getSkill().getExp()[2]);
                st.setDouble(7, player.getSkill().getExp()[3]);
                st.setDouble(8, player.getSkill().getExp()[4]);
                st.setDouble(9, player.getSkill().getExp()[5]);
                st.setDouble(10, player.getSkill().getExp()[6]);
                st.setDouble(11, player.getSkill().getExp()[7]);
                st.setDouble(12, player.getSkill().getExp()[8]);
                st.setDouble(13, player.getSkill().getExp()[9]);
                st.setDouble(14, player.getSkill().getExp()[10]);
                st.setDouble(15, player.getSkill().getExp()[11]);
                st.setDouble(16, player.getSkill().getExp()[12]);
                st.setDouble(17, player.getSkill().getExp()[13]);
                st.setDouble(18, player.getSkill().getExp()[14]);
                st.setDouble(19, player.getSkill().getExp()[15]);
                st.setDouble(20, player.getSkill().getExp()[16]);
                st.setDouble(21, player.getSkill().getExp()[17]);
                st.setDouble(22, player.getSkill().getExp()[18]);
                st.setDouble(23, player.getSkill().getExp()[19]);
                st.setDouble(24, player.getSkill().getExp()[20]);
                st.executeUpdate(); 
            	} catch(Exception e) {
            		e.printStackTrace();
            	}
                return null;
            }
        }, null);
    }

    public static HighscoresManager getSingleton() {
        return singleton;
    }
}
