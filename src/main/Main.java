package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import global.GlobalVariable;
import config.Config;
import server.ServerThread;

/**
 *
 * @author armin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Config config = new Config(args);
        GlobalVariable.config = config;
        try {
            ServerThread serverThread = new ServerThread(config.getServer().getSignallingPort(), "Main");
            serverThread.start();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
