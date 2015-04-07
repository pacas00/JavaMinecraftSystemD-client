/*******************************************************************************
 * Copyright (c) 2015 Peter Cashel (pacas00@petercashel.net). All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Creative Commons Attribution-NoDerivatives 4.0 International License
 * which accompanies this distribution, and is available at
 * http://creativecommons.org/licenses/by-nd/4.0/.
 *
 * Contributors:
 *     Peter Cashel - initial implementation
 *******************************************************************************/
package net.petercashel.jmsDc;

import java.util.concurrent.TimeUnit;

import net.petercashel.commonlib.threading.threadManager;
import net.petercashel.jmsDc.command.commandClient;
import net.petercashel.nettyCore.client.clientCore;
import net.petercashel.nettyCore.common.exceptions.ConnectionShuttingDown;
import net.petercashel.nettyCore.server.serverCore;

public class clientMain {

	public static boolean run = true;
	
	public static void main(String[] args) {
		//init client console and network;
		commandClient.init();
		reconnect();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//init other
		
		commandClient.ConsoleHandover();

	}

	public static void shutdown() {
		run = false;
		try {
			clientCore.shutdown();
		} catch (NullPointerException e) {
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionShuttingDown e) {
		}
		threadManager.getInstance().shutdown();
		System.exit(0);
		
	}

	public static void reconnect() {
		threadManager.getInstance().addRunnable(new Runnable() {
			@Override
			public void run() {
				try {
					clientCore.initializeConnection("127.0.0.1",14444);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					shutdown();
				}
			}
		});
		
	}

}
