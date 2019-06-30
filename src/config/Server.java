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

    private int signallingPort;
    private int mediaPortFrom;
    private int mediaPortTo;
    private String HostAddress;

    public Server() {
    }

    public Server(int Port, String HostAddress) {
        this.signallingPort = Port;
        this.HostAddress = HostAddress;
    }

    public int getSignallingPort() {
        return signallingPort;
    }

    public int getMediaPortFrom() {
        return mediaPortFrom;
    }

    public int getMediaPortTo() {
        return mediaPortTo;
    }

    public String getHostAddress() {
        return HostAddress;
    }

    public void setHostAddress(String HostAddress) {
        this.HostAddress = HostAddress;
    }

    public void setSignallingPort(int Port) {
        this.signallingPort = Port;
    }

    public void setMediaPortFrom(int mediaPortFrom) {
        this.mediaPortFrom = mediaPortFrom;
    }

    public void setMediaPortTo(int mediaPortTo) {
        this.mediaPortTo = mediaPortTo;
    }

}
