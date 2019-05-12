/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import client.ClientThread;

/**
 *
 * @author armin
 */
public class Car {

    private String username;
    private String password;
    private ClientThread clientCar;
    private ClientThread clientControl;

    public Car() {
    }

    public Car(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ClientThread getClientCar() {
        return clientCar;
    }

    public ClientThread getClientControl() {
        return clientControl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClientCar(ClientThread clientCar) {
        this.clientCar = clientCar;
    }

    public void setClientControl(ClientThread clientControl) {
        this.clientControl = clientControl;
    }

    public ClientThread getOppositeSideClient(ClientThread client) {
        ClientThread opposite_side = null;
        if (client != null && clientCar != null && client == clientCar) {
            opposite_side = clientControl;
        } else if (client != null && clientControl != null && client == clientControl) {
            opposite_side = clientCar;
        }
        return opposite_side;
    }

    public void unsetClient(ClientThread client) {
        if (client != null && clientCar != null && client == clientCar) {
            clientCar = null;
        } else if (client != null && clientControl != null && client == clientControl) {
            clientControl = null;
        }

    }

    @Override
    public boolean equals(Object obj) {
        Car car = (Car) obj;
        return username.equals(car.getUsername()) && password.equals(car.getPassword());
    }

}
