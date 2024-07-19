package org.kybe.commands;

import org.kybe.window.RemoteConsole;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.command.Command;
import org.rusherhack.core.command.annotations.CommandExecutor;

public class RemoteSend extends Command {
    public RemoteSend() {
        super("remotesend", "Send a command to the remote console");
    }

    @CommandExecutor(subCommand = "msg")
    @CommandExecutor.Argument({"message"})
    public void onMsg(String message) {
        RemoteConsole remoteConsole = (RemoteConsole) RusherHackAPI.getWindowManager().getFeature("Remote Console").get();
        remoteConsole.messageView.add(".msg " + message, java.awt.Color.green.getRGB());
        remoteConsole.config.commandSendQueue.add(".msg " + message);
    }

    @CommandExecutor(subCommand = "cmd")
    @CommandExecutor.Argument({"command"})
    public void onCmd(String command) {
        RemoteConsole remoteConsole = (RemoteConsole) RusherHackAPI.getWindowManager().getFeature("Remote Console").get();
        remoteConsole.messageView.add(".cmd " + command, java.awt.Color.green.getRGB());
        remoteConsole.config.commandSendQueue.add(".cmd " + command);
    }

}
