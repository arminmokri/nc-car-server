/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.parameters;

import java.util.ArrayList;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author armin
 */
public class Parameters extends ArrayList<Parameter> {

    public Parameters() {
    }

    public Parameters(byte[] jsonBytes) throws ParseException {
        this(new String(jsonBytes));
    }

    public Parameters(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
        for (Object object : jSONObject.entrySet()) {
            Map.Entry map = (Map.Entry) object;
            add(map.getKey().toString(), map.getValue().toString());
        }
    }

    public final void add(String key, String value) {
        add(new Parameter(key, value));
    }

    public String getValue(String key) {
        Parameter parameter = get(key);
        if (parameter != null) {
            return parameter.getValue();
        }
        return null;
    }

    public Parameter get(String key) {
        for (Parameter parameter : this) {
            if (parameter.getKey().equals(key)) {
                return parameter;
            }
        }
        return null;
    }

    public String getJsonString() {
        JSONObject jSONObject = new JSONObject();
        for (Parameter parameter : this) {
            jSONObject.put(parameter.getKey(), parameter.getValue());
        }
        return jSONObject.toJSONString();

    }

    public byte[] getJsonBytes() {
        return getJsonString().getBytes();

    }

}
