/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.response;

import global.GlobalVariable;
import client.ClientThread;
import client.Header;
import client.parameters.Parameter;
import client.parameters.Parameters;
import client.request.Request;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.IOException;
import java.util.Arrays;
import org.json.simple.parser.ParseException;

/**
 *
 * @author armin
 */
public class Response {

    //
    private Header header;
    private Parameters requestParameters;
    private Parameters responseParameters;
    //
    private ClientThread clientThread;

    public Response(Request request) {
        this(request.getHeader(), request.getRequestParameters());
    }

    public Response(Header header, Parameters requestParameters) {
        this.header = header;
        this.header.setType(Header.RESPONSE);
        this.requestParameters = requestParameters;
        this.responseParameters = new Parameters();
        setResponseParameters();
    }

    public Response(Request request, ClientThread clientThread) {
        this(request.getHeader(), request.getRequestParameters(), clientThread);
    }

    public Response(Header header, Parameters requestParameters, ClientThread clientThread) {
        this.header = header;
        this.header.setType(Header.RESPONSE);
        this.requestParameters = requestParameters;
        this.responseParameters = new Parameters();
        this.clientThread = clientThread;
        setResponseParameters();
    }

    public Response(byte[] bytes) throws ParseException {
        // header
        byte[] header = Arrays.copyOfRange(bytes, 0, 11);
        this.header = new Header(Header.REQUEST, header);
        // responseParameters
        byte[] parmeters = Arrays.copyOfRange(bytes, 11, bytes.length);
        this.responseParameters = new Parameters(parmeters);
    }

    public final void setResponseParameters() {
        String action = requestParameters.getParameterValue(Parameter.ACTION);
        switch (action) {
            case Parameter.HEARTBEAT:
                responseParameters.addParameter(Parameter.RESULT, Parameter.RESULT_1);
                break;
            case Parameter.REGISTER:
                String type = requestParameters.getParameterValue(Parameter.TYPE);
                String username = requestParameters.getParameterValue(Parameter.USERNAME);
                String password = requestParameters.getParameterValue(Parameter.PASSWORD);
                if (type.equals(Parameter.TYPE_CAR)) {
                    if (GlobalVariable.config.getCars().containsCar(username, password)) {
                        responseParameters.addParameter(Parameter.RESULT, Parameter.RESULT_1);
                    } else {
                        responseParameters.addParameter(Parameter.RESULT, Parameter.RESULT_0);
                        responseParameters.addParameter(Parameter.MESSAGE, Parameter.REGISTER_INVALID_USER_PASS);
                    }
                } else {
                    responseParameters.addParameter(Parameter.RESULT, Parameter.RESULT_0);
                    responseParameters.addParameter(Parameter.MESSAGE, Parameter.REGISTER_INVALID_TYPE);
                }
                break;
            default:
                String string = Parameter.NO_ANSWER + ", For This Request Action: " + action;
                responseParameters.addParameter(Parameter.RESULT, Parameter.RESULT_0);
                responseParameters.addParameter(Parameter.MESSAGE, string);
                System.err.println(string);
                break;
        }
    }

    public Parameters getResponseParameters() {
        return responseParameters;
    }

    public Header getHeader() {
        return header;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(100);
        byteArrayBuffer.write(header.getBytes());
        byteArrayBuffer.write(responseParameters.getJsonBytes());
        return byteArrayBuffer.toByteArray();
    }

}
