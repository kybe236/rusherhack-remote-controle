package org.example;

import org.example.window.RemoteConsole;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;


/**
 * Remote Control Plugin
 *
 * @author kybe236
 */
public class RemoteControle extends Plugin {
	
	@Override
	public void onLoad() {
        final RemoteConsole remoteConsole;
        RusherHackAPI.getWindowManager().registerFeature(new RemoteConsole());
	}

	@Override
	public void onUnload() {

	}
}