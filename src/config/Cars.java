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
public class Cars {

    private ArrayList<Car> cars;

    public Cars() {
        this.cars = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public boolean containsCar(String username, String password) {
        Car car = new Car(username, password);
        return containsCar(car);
    }

    public boolean containsCar(Car car) {
        return cars.contains(car);
    }

}
