package org.kybe.CommandHandler;

import net.minecraft.client.Minecraft;
import org.kybe.window.RemoteConsole;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class Client {

    public static void handle(String command, RemoteConsole remoteConsole) {
        boolean running = remoteConsole.config.running;
        if (command.equalsIgnoreCase(".start")) {
            if (!running) {
                remoteConsole.config.running = true;
                enableClientSocket(remoteConsole);
                remoteConsole.messageView.add("Client started", java.awt.Color.green.getRGB());
            } else {
                remoteConsole.messageView.add("Client is already running", java.awt.Color.red.getRGB());
            }
        } else if (command.equalsIgnoreCase(".stop")) {
            if (running) {
                remoteConsole.config.running = false;
                remoteConsole.messageView.add("Client stopped", java.awt.Color.green.getRGB());
            } else {
                remoteConsole.messageView.add("Client is not running", java.awt.Color.red.getRGB());
            }
        } else {
            remoteConsole.messageView.add("Invalid command", java.awt.Color.red.getRGB());
        }
    }

    public static void enableClientSocket(RemoteConsole remoteConsole) {
        new Thread(new ClientThread(remoteConsole)).start();
    }

    public static String handleCommand(String response, RemoteConsole remoteConsole) throws UnknownHostException {
        if (response.toLowerCase().startsWith(".msg")) {
            String[] split = response.split(" ");
            StringBuilder msg = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                msg.append(split[i]).append(" ");
            }
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.connection.sendChat(msg.toString().trim());
            remoteConsole.messageView.add("Message sent: " + msg, Color.green.getRGB());
            return "";
        }
        if (response.toLowerCase().startsWith(".cmd")) {
            String[] split = response.split(" ");
            StringBuilder cmd = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                cmd.append(split[i]).append(" ");
            }
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.connection.sendCommand(cmd.toString().trim());
            remoteConsole.messageView.add("Command sent: " + cmd, Color.green.getRGB());

            return "";
        }

        if (response.equalsIgnoreCase(".info")) {
            remoteConsole.messageView.add("sending info", Color.green.getRGB());
            int hours = (int) (System.currentTimeMillis() / 3600000) % 24;
            int minutes = (int) (System.currentTimeMillis() / 60000) % 60;
            int seconds = (int) (System.currentTimeMillis() / 1000) % 60;
            int milliseconds = (int) System.currentTimeMillis() % 1000;
            return "IP: " + InetAddress.getLocalHost().getHostAddress() + " Time: " + hours + ":" + minutes + ":" + seconds + " ms: " + milliseconds;
        }

        if (response.toLowerCase().startsWith(".mirror")) {
            //All after the first one
            String[] messages = response.split(" ");
            StringBuilder message = new StringBuilder();

            for (int i = 2; i < messages.length; i++) {
                message.append(messages[i]).append(" ");
            }

            assert Minecraft.getInstance().player != null;
            if (messages[1].equalsIgnoreCase("msg")) {
                Minecraft.getInstance().player.connection.sendChat(message.toString().trim());
            } else if (messages[1].equalsIgnoreCase("cmd")) {
                Minecraft.getInstance().player.connection.sendCommand(message.toString().trim());
            } else {
                remoteConsole.messageView.add("Invalid command", Color.red.getRGB());
            }
            return "";
        }

        remoteConsole.messageView.add("Invalid command", Color.red.getRGB());
        return "";
    }
}

class ClientThread implements Runnable {

    private final RemoteConsole remoteConsole;
    private PrintWriter out;

    public ClientThread(RemoteConsole remoteConsole) {
        this.remoteConsole = remoteConsole;
    }

    @Override
    public void run() {
        try {
            Socket client = new Socket(remoteConsole.config.Ip, remoteConsole.config.serverPort);
            while (remoteConsole.config.running) {
                out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String command = in.readLine();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if (command.equalsIgnoreCase(".info")) {
                                out.println(Client.handleCommand(command, remoteConsole));
                            } else {
                                Client.handleCommand(command, remoteConsole);
                            }
                        } catch (UnknownHostException e) {
                            remoteConsole.config.running = true;
                            remoteConsole.messageView.add("Error: " + e.getMessage(), Color.red.getRGB());
                        }
                    }
                }, 0);
            }
            out.close();
        } catch (IOException e) {
            remoteConsole.messageView.add("Error: " + e.getMessage(), Color.red.getRGB());
        }
    }
}

