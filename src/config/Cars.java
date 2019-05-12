/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.util.ArrayList;

/**
 *
 * @author armin
 */
public class Cars extends ArrayList<Car> {

    public Cars() {
    }

    public boolean authication(String username, String password) {
        Car car = new Car(username, password);
        return contains(car);
    }

    public Car get(String username, String password) {
        Car car = new Car(username, password);
        int index = indexOf(car);
        if (index >= 0) {
            return get(index);
        } else {
            return null;
        }
    }
}
