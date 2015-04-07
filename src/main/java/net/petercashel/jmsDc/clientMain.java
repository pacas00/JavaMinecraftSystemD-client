/*******************************************************************************
 *    Copyright 2015 Peter Cashel (pacas00@petercashel.net)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
