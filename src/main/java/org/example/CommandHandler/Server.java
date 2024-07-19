package org.example.CommandHandler;

import org.example.window.RemoteConsole;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server {

    public static void handle(String command, RemoteConsole remoteConsole) {
        if (command.equalsIgnoreCase("start")) {
            remoteConsole.config.running = true;
            enableServerSocket(remoteConsole);
            remoteConsole.messageView.add("Server started", Color.green.getRGB());
        } else if (command.equalsIgnoreCase("stop")) {
            remoteConsole.config.running = false;
            remoteConsole.messageView.add("Server stopped initialized", Color.green.getRGB());
        } else {
            remoteConsole.config.commandSendQueue.add(command);
        }
    }

    public static void enableServerSocket(RemoteConsole remoteConsole) {
        new Thread(new ServerThread(remoteConsole)).start();
        remoteConsole.messageView.add("Server socket enabled", Color.green.getRGB());
    }
}

class ServerThread implements Runnable {
    private final RemoteConsole remoteConsole;
    private final ArrayList<ConnectionHandler> connections = new ArrayList<>();

    public ServerThread(RemoteConsole remoteConsole) {
        remoteConsole.messageView.add("Server class created", Color.green.getRGB());
        this.remoteConsole = remoteConsole;
    }

    public void sendCommands() {
        for (ConnectionHandler connection : connections) {
            while (!remoteConsole.config.commandSendQueue.isEmpty()) {
                remoteConsole.messageView.add("Sending command..", Color.green.getRGB());
                connection.sendCommand(remoteConsole.config.commandSendQueue.remove(0));
                remoteConsole.messageView.add("Command sent done", Color.green.getRGB());
            }
        }
    }

    @Override
    public void run() {
        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendCommands();
                }
            }, 0, 100);
            ServerSocket serverSocket = new ServerSocket(remoteConsole.config.serverPort);
            remoteConsole.messageView.add("Server is running "+ remoteConsole.config.running, Color.green.getRGB());
            while (remoteConsole.config.running) {
                Socket client = serverSocket.accept();
                remoteConsole.messageView.add("Client connected " + client.getInetAddress().toString(), Color.green.getRGB());
                ConnectionHandler connectionHandler = new ConnectionHandler(client, remoteConsole);
                new Thread(connectionHandler).start();
                connections.add(connectionHandler);
            }
            remoteConsole.messageView.add("Server stopping...", Color.green.getRGB());
            for (ConnectionHandler connection : connections) {
                connection.out.close();
                connection.in.close();
                connection.socket.close();
            }
            serverSocket.close();
            remoteConsole.messageView.add("Server stopped", Color.green.getRGB());
        } catch (Exception e) {
            remoteConsole.messageView.add("Error: " + e.getMessage(), Color.red.getRGB());
        }
    }

    static class ConnectionHandler implements Runnable {
        private final Socket socket;
        private final RemoteConsole remoteConsole;
        private BufferedReader in;
        private PrintWriter out;

        public ConnectionHandler(Socket socket, RemoteConsole remoteConsole) {
            this.socket = socket;
            this.remoteConsole = remoteConsole;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
                remoteConsole.messageView.add("Error: " + e.getMessage(), Color.red.getRGB());
            }
        }

        public void sendCommand(String command) {
            remoteConsole.messageView.add("Command sending: " + command, Color.white.getRGB());
            try {
                out.println(command);
            } catch (Exception e) {
                remoteConsole.messageView.add("Error: " + e.getMessage(), Color.red.getRGB());
            }
            remoteConsole.messageView.add("Command sent: " + command, Color.white.getRGB());
        }

        public void receiveResponse() {
            try {
                String response = in.readLine();
                remoteConsole.messageView.add("Response received: " + response, Color.white.getRGB());
            } catch (Exception e) {
                remoteConsole.messageView.add("Error: " + e.getMessage(), Color.red.getRGB());
            }
        }
    }
}

