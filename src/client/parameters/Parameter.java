/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.parameters;

/**
 *
 * @author armin
 */
public class Parameter {

    // ACTION
    public static final String ACTION = "action";
    // HEARTBEAT
    public static final String HEARTBEAT = "heartbeat";
    // REGISTER
    public static final String REGISTER = "register";
    public static final String REGISTER_INVALID_TYPE = "invalid type";
    public static final String REGISTER_INVALID_USER_PASS = "invalid username or password";
    // TYPE
    public static final String TYPE = "type";
    public static final String TYPE_SERVER = "server";
    public static final String TYPE_CAR = "car";
    public static final String TYPE_CONTROL = "control";
    // AUTHENTICATION
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    // RESULT
    public static final String RESULT = "result";
    public static final String RESULT_0 = "0";
    public static final String RESULT_1 = "1";
    // MESSAGE
    public static final String MESSAGE = "message";
    // NO_ANSWER
    public static final String NO_ANSWER = "no answer";
    //
    private String key;
    private String value;

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

}
