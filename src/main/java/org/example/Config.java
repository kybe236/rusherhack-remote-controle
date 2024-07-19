package org.example;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public boolean Server = false;
    public String Ip = "";
    public List<String> commandSendQueue = new ArrayList<>();
    public boolean running = false;
    public int serverPort = 23609;

    public Config() {
    }
}
