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
import config.Car;
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
        try {
            String action = requestParameters.getValue(Parameter.ACTION);
            switch (action) {
                case Parameter.HEARTBEAT: {
                    responseParameters.add(Parameter.RESULT, Parameter.RESULT_1);
                    break;
                }
                case Parameter.REGISTER: {
                    String type = requestParameters.getValue(Parameter.TYPE);
                    String username = requestParameters.getValue(Parameter.USERNAME);
                    String password = requestParameters.getValue(Parameter.PASSWORD);
                    Car car = GlobalVariable.config.getCars().get(username, password);
                    if (type.equals(Parameter.TYPE_CAR)) {
                        if (car != null) {
                            if (car.getClientCar() == null) {
                                car.setClientCar(clientThread);
                                clientThread.setCar(car);
                                responseParameters.add(Parameter.RESULT, Parameter.RESULT_1);
                            } else {
                                responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
                                responseParameters.add(Parameter.MESSAGE, Parameter.REGISTER_ALREADY);
                            }
                        } else {
                            responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
                            responseParameters.add(Parameter.MESSAGE, Parameter.REGISTER_INVALID_USER_PASS);
                        }
                    } else if (type.equals(Parameter.TYPE_CONTROL)) {
                        if (car != null) {
                            if (car.getClientControl() == null) {
                                car.setClientControl(clientThread);
                                clientThread.setCar(car);
                                responseParameters.add(Parameter.RESULT, Parameter.RESULT_1);
                            } else {
                                responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
                                responseParameters.add(Parameter.MESSAGE, Parameter.REGISTER_ALREADY);
                            }
                        } else {
                            responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
                            responseParameters.add(Parameter.MESSAGE, Parameter.REGISTER_INVALID_USER_PASS);
                        }
                    } else {
                        responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
                        responseParameters.add(Parameter.MESSAGE, Parameter.REGISTER_INVALID_TYPE);
                    }
                    break;
                }
                case Parameter.PROXY: {
                    ClientThread clientThread_opposite_side = clientThread.getCar().getOppositeSideClient(clientThread);
                    if (clientThread_opposite_side != null) {
                        String opposite_side_parameters_string = requestParameters.getValue(Parameter.OPPOSITE_SIDE_PARAMETERS);
                        Parameters opposite_side_parameters = new Parameters(opposite_side_parameters_string);
                        Request opposite_side_request = new Request(opposite_side_parameters);
                        clientThread_opposite_side.Request(opposite_side_request);
                        responseParameters = opposite_side_request.getResponseParameters();
                    } else {
                        responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
                        responseParameters.add(Parameter.MESSAGE, Parameter.OPPOSITE_SIDE_NOT_AVAILABLE);
                    }
                    break;
                }
                default: {
                    String string = Parameter.NO_ANSWER + ", For This Request Action: " + action;
                    responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
                    responseParameters.add(Parameter.MESSAGE, string);
                    System.err.println(string);
                    break;
                }
            }
        } catch (Exception exception) {
            String string = Parameter.ERROR + ", " + exception.getMessage();
            responseParameters.add(Parameter.RESULT, Parameter.RESULT_0);
            responseParameters.add(Parameter.MESSAGE, string);
            System.err.println(string);
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
