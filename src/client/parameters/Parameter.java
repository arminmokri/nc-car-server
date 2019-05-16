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
    public static final String REGISTER_ALREADY = "already register";
    // TYPE
    public static final String TYPE = "type";
    public static final String TYPE_SERVER = "server";
    public static final String TYPE_CAR = "car";
    public static final String TYPE_CONTROL = "control";
    // AUTHENTICATION
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    // KEY
    public static final String KEY = "key";
    public static final String KEY_CODE = "code";
    public static final String KEY_MODE = "mode";
    public static final String KEY_HEARTBEAT_INTERVAL = "heartbeat_interval";
    public static final String KEY_HEARTBEAT_WAIT_TIME = "heartbeat_wait_time";
    public static final String KEY_EVENT = "event";
    // PROXY
    public static final String PROXY = "proxy";
    public static final String OPPOSITE_SIDE_PARAMETERS = "opposite_side_parameters";
    public static final String OPPOSITE_SIDE_NOT_AVAILABLE = "opposite side not available";
    // RESULT
    public static final String RESULT = "result";
    public static final String RESULT_0 = "0";
    public static final String RESULT_1 = "1";
    // MESSAGE
    public static final String MESSAGE = "message";
    // NO_ANSWER
    public static final String NO_ANSWER = "no answer";
    //
    public static final String ERROR = "error";
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
