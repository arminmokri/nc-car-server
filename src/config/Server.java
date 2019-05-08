/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author armin
 */
public class Server {

    private int Port;
    private String HostAddress;

    public Server() {
    }

    public Server(int Port, String HostAddress) {
        this.Port = Port;
        this.HostAddress = HostAddress;
    }

    public int getPort() {
        return Port;
    }

    public String getHostAddress() {
        return HostAddress;
    }

    public void setHostAddress(String HostAddress) {
        this.HostAddress = HostAddress;
    }

    public void setPort(int Port) {
        this.Port = Port;
    }

}
