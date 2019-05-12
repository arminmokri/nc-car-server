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
public class ProxyParameters extends Parameters {

    public ProxyParameters() {
    }

    @Override
    public String getJsonString() {
        Parameters parameters = new Parameters();
        parameters.add(Parameter.ACTION, Parameter.PROXY);
        parameters.add(Parameter.OPPOSITE_SIDE_PARAMETERS, super.getJsonString());
        return parameters.getJsonString();
    }

    @Override
    public byte[] getJsonBytes() {
        return getJsonString().getBytes();
    }

}
