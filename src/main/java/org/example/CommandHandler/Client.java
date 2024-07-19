package org.example.CommandHandler;

import org.example.window.RemoteConsole;

import java.awt.*;
import java.io.*;
import java.net.*;

public class Client {

    private static DatagramSocket socket;

    public static void handle(String command, RemoteConsole remoteConsole) {
        boolean running = remoteConsole.config.running;
        if (command.equalsIgnoreCase("start")) {
            if (!running) {
                remoteConsole.config.running = true;
                enableClientSocket(remoteConsole);
                remoteConsole.messageView.add("Client started", java.awt.Color.green.getRGB());
            } else {
                remoteConsole.messageView.add("Client is already running", java.awt.Color.red.getRGB());
            }
        } else if (command.equalsIgnoreCase("stop")) {
            if (running) {
                remoteConsole.config.running = false;
                remoteConsole.messageView.add("Client stopped", java.awt.Color.green.getRGB());
            } else {
                remoteConsole.messageView.add("Client is not running", java.awt.Color.red.getRGB());
            }
        } else if (command.toLowerCase().startsWith("ip")) {
            String[] split = command.split(" ");
            if (split.length > 1) {
                remoteConsole.config.Ip = split[1];
                remoteConsole.messageView.add("IP set to: " + split[1], java.awt.Color.green.getRGB());
            } else {
                remoteConsole.messageView.add("Invalid IP", java.awt.Color.red.getRGB());
            }
        }
    }

    public static void enableClientSocket(RemoteConsole remoteConsole) {
        new Thread(new ClientThread(remoteConsole)).start();
    }

    public static String handleCommand(String response, RemoteConsole remoteConsole) {
        remoteConsole.messageView.add("Command received: " + response, java.awt.Color.green.getRGB());

        return response;
    }
}

class ClientThread implements Runnable {

    private final RemoteConsole remoteConsole;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    public ClientThread(RemoteConsole remoteConsole) {
        this.remoteConsole = remoteConsole;
    }

    @Override
    public void run() {
        try {
            Socket client = new Socket(remoteConsole.config.Ip, remoteConsole.config.serverPort);
            while (remoteConsole.config.running) {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String command = in.readLine();
                out.print(Client.handleCommand(command, remoteConsole));
            }
        } catch (IOException e) {
            remoteConsole.messageView.add("Error: " + e.getMessage(), Color.red.getRGB());
        }
    }
}

