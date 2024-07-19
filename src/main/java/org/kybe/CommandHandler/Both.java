package org.kybe.CommandHandler;

import org.kybe.Config;

public class Both {
    public static void handle(String command, org.kybe.window.RemoteConsole remoteConsole) {
        Config config = remoteConsole.config;

        if (command.equalsIgnoreCase(".server true") || command.equalsIgnoreCase(".server 1")) {
            config.Server = true;
            remoteConsole.messageView.add("Server mode enabled", java.awt.Color.green.getRGB());
        } else if (command.equalsIgnoreCase(".server false") || command.equalsIgnoreCase(".server 0")) {
            config.Server = false;
            remoteConsole.messageView.add("Server mode disabled", java.awt.Color.red.getRGB());
        } else if (command.toLowerCase().startsWith(".ip")) {
            String[] parts = command.split(" ");
            if (parts.length > 1) {
                config.Ip = parts[1];
                remoteConsole.messageView.add("Server IP set to: " + parts[1], java.awt.Color.green.getRGB());
            } else {
                remoteConsole.messageView.add("Invalid IP command format", java.awt.Color.red.getRGB());
            }
        } else if (command.toLowerCase().startsWith(".port")) {
            String[] parts = command.split(" ");
            if (parts.length > 1) {
                try {
                    config.serverPort = Integer.parseInt(parts[1]);
                    remoteConsole.messageView.add("Server port set to: " + parts[1], java.awt.Color.green.getRGB());
                } catch (NumberFormatException e) {
                    remoteConsole.messageView.add("Invalid port format", java.awt.Color.red.getRGB());
                }
            } else {
                remoteConsole.messageView.add("Invalid port command format", java.awt.Color.red.getRGB());
            }
        } else if (config.Server) {
            Server.handle(command, remoteConsole);
        } else {
            Client.handle(command, remoteConsole);
        }
    }
}
