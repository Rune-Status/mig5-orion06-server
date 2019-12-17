package com.rs2.util;

import java.io.IOException;


public class ShutDownHook extends Thread {

	@Override
	public void run() {
		PlayerSave.saveAllPlayers();
		try {
			Misc.writeSettings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Saved all players.");
	}
}