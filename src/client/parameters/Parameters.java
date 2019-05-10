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
public class Parameters {
    
    private ArrayList<Parameter> parameters;
    
    public Parameters() {
        parameters = new ArrayList<>();
    }
    
    public Parameters(byte[] jsonBytes) throws ParseException {
        this(new String(jsonBytes));
    }
    
    public Parameters(String jsonString) throws ParseException {
        parameters = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject jSONObject = (JSONObject) parser.parse(jsonString);
        for (Object object : jSONObject.entrySet()) {
            Map.Entry map = (Map.Entry) object;
            addParameter(map.getKey().toString(), map.getValue().toString());
        }
    }
    
    public final void addParameter(String key, String value) {
        this.addParameter(new Parameter(key, value));
        
    }
    
    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
        
    }
    
    public String getParameterValue(String key) {
        Parameter parameter = this.getParameter(key);
        if (parameter != null) {
            return parameter.getValue();
        }
        return null;
    }
    
    public Parameter getParameter(String key) {
        for (Parameter parameter : parameters) {
            if (parameter.getKey().equals(key)) {
                return parameter;
            }
        }
        return null;
    }
    
    public String getJsonString() {
        JSONObject jSONObject = new JSONObject();
        for (Parameter parameter : parameters) {
            jSONObject.put(parameter.getKey(), parameter.getValue());
        }
        return jSONObject.toJSONString();
        
    }
    
    public byte[] getJsonBytes() {
        return getJsonString().getBytes();
        
    }
    
}
