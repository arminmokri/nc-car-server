/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.ClientThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Armin
 */
public class ServerThread extends Thread {

    private ServerSocket serverSocket;
    private boolean Running;

    public ServerThread(int port, String name) throws IOException {
        super(name + "->" + "ServerThread");
        this.serverSocket = new ServerSocket(port);
        this.Running = false;
    }

    @Override
    public void run() {
        while (this.isRunning()) {
            try {
                Socket socket = this.serverSocket.accept();
                client.ClientThread clientThread = new ClientThread(socket, getName());
                clientThread.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        if (!this.isRunning()) {
            this.setRunning(true);
            super.start();
        }
    }

    public void Stop() {
        if (this.isRunning()) {
            this.setRunning(false);
            try {
                this.serverSocket.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                super.stop();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    public boolean isRunning() {
        return Running;
    }

    private void setRunning(boolean Running) {
        this.Running = Running;
    }
}
