package com.rs2.util;

import com.rs2.GlobalVariables;
import com.rs2.Server;

/**
 *
 */
public class ShutdownWorldProcess implements Runnable {

    private final long sleepTime;

    public ShutdownWorldProcess(int seconds) {
        sleepTime = seconds*1000;
    }
    
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
        Server.serverStatus = 3;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        GlobalVariables.ACCEPT_CONNECTIONS = false;
        System.exit(0);
        //Server.runner2.stop();
        //Server.runner.terminate();
        //Server.serverStatus = 3;
        Server.stop = true;
        /*Server.runner.stop();
        Server.runner2.stop();*/
    }
}
