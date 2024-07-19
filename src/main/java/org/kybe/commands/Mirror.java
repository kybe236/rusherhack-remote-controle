package org.kybe.commands;

import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import org.kybe.window.RemoteConsole;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.network.EventPacket;
import org.rusherhack.client.api.feature.command.Command;
import org.rusherhack.core.command.annotations.CommandExecutor;
import org.rusherhack.core.event.subscribe.Subscribe;

import java.awt.*;

public class Mirror extends Command {
    private boolean cancelmsg = false;
    private boolean cancelcmd = false;
    private boolean mirrormsg = false;
    private boolean mirrorcmd = false;
    private boolean test = true;
    public Mirror() {
        super("mirror", "Mirror the console output");
    }

    @CommandExecutor(subCommand = "msg")
    @CommandExecutor.Argument({"on", "cancel"})
    public void onMsg(Boolean on, Boolean cancel) {
        RemoteConsole remoteConsole = (RemoteConsole) RusherHackAPI.getWindowManager().getFeature("Remote Console").get();
        mirrormsg = on;
        cancelmsg = cancel;
    }

    @CommandExecutor(subCommand = "cmd")
    @CommandExecutor.Argument({"on", "cancel"})
    public void onCmd(Boolean on, Boolean cancel) {
        RemoteConsole remoteConsole = (RemoteConsole) RusherHackAPI.getWindowManager().getFeature("Remote Console").get();
        mirrorcmd = on;
        cancelmsg = cancel;
    }

    @Subscribe
    public void onMessage(EventPacket.Send event) {
        if (test == true) {
            test = false;
            RemoteConsole remoteConsole = (RemoteConsole) RusherHackAPI.getWindowManager().getFeature("Remote Console").get();
            remoteConsole.messageView.add("Mirror command loaded", Color.green.getRGB());
            return;
        }
        if (event.getPacket() instanceof ServerboundChatPacket packet) {
            if (mirrormsg) {
                RemoteConsole remoteConsole = (RemoteConsole) RusherHackAPI.getWindowManager().getFeature("Remote Console").get();
                remoteConsole.messageView.add(".msg " + packet.message(), java.awt.Color.green.getRGB());
                remoteConsole.config.commandSendQueue.add(".msg " + packet.message());
                event.setCancelled(cancelcmd);
            }
        } else if (event.getPacket() instanceof ServerboundChatCommandPacket packet) {
            if (mirrorcmd) {
                RemoteConsole remoteConsole = (RemoteConsole) RusherHackAPI.getWindowManager().getFeature("Remote Console").get();
                remoteConsole.messageView.add(".cmd " + packet.command(), java.awt.Color.green.getRGB());
                remoteConsole.config.commandSendQueue.add(".cmd " + packet.command());
                event.setCancelled(cancelcmd);
            }
        }
    }
}
