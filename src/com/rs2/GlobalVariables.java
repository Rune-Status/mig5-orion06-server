package com.rs2;

import com.rs2.model.tick.TickTimer;

public class GlobalVariables {

    public static boolean ACCEPT_CONNECTIONS = true;

	public static int serverGlobalTimer = 0;
	public static TickTimer serverUpdateTimer = null;

	// public static long leverTimer[] = new long[LeverHandler.lever.length];

	public static int getServerGlobalTimer() {
		return serverGlobalTimer;
	}
	public static void setServerGlobalTimer(int serverGlobalTimer) {
		GlobalVariables.serverGlobalTimer = serverGlobalTimer;
	}
	public static TickTimer getServerUpdateTimer() {
		return serverUpdateTimer;
	}
	public static void setServerUpdateTimer(TickTimer serverUpdateTimer) {
		GlobalVariables.serverUpdateTimer = serverUpdateTimer;
	}

}
