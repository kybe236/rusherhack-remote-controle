package org.kybe;

import org.kybe.commands.Mirror;
import org.kybe.commands.RemoteSend;
import org.kybe.window.RemoteConsole;
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
		this.getLogger().info("Remote Control Plugin loaded");
        final RemoteConsole remoteConsole;
        RusherHackAPI.getWindowManager().registerFeature(new RemoteConsole());
		RusherHackAPI.getCommandManager().registerFeature(new Mirror());
		RusherHackAPI.getCommandManager().registerFeature(new RemoteSend());
	}

	@Override
	public void onUnload() {
		this.getLogger().info("Remote Control Plugin unloaded");
	}
}