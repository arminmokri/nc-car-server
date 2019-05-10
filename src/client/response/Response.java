/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.response;

import client.ClientThread;
import client.Header;
import client.request.Request;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author armin
 */
public class Response {

    // Request.HEARTBEAT
    public static final String HEARTBEAT_ACT = "act";
    // Request.REGISTER
    public static final String REGISTER_ACT = "act";
    // Request.TYPE
    public static final String TYPE_SERVER = "server";
    public static final String TYPE_CAR = "car";
    public static final String TYPE_CONTROL = "control";
    // NO_ANSWER
    public static final String NO_ANSWER = "no answer";
    //
    private Header header;
    private byte[] answer;
    private ClientThread clientThread;

    public Response(Header header, byte request) {
        this.header = header;
        this.header.setType(Header.RESPONSE);
        setAnswer(request);
    }

    public Response(Header header, byte request, ClientThread clientThread) {
        this.header = header;
        this.header.setType(Header.RESPONSE);
        this.clientThread = clientThread;
        setAnswer(request);
    }

    public Response(byte[] bytes) {
        this.header = new Header(Header.RESPONSE, bytes);
        this.answer = Arrays.copyOfRange(bytes, 11, bytes.length);
    }

    private void setAnswer(byte request) {
        switch (request) {
            case Request.HEARTBEAT:
                this.answer = Response.HEARTBEAT_ACT.getBytes();
                break;
            case Request.REGISTER:
                this.answer = Response.REGISTER_ACT.getBytes();
                Request type = new Request(Request.TYPE);
                clientThread.Request(type);
                if (type.getAnswerString().equals(Response.TYPE_CAR)) {
                    this.answer = Response.REGISTER_ACT.getBytes();
                }
                break;
            case Request.TYPE:
                this.answer = Response.TYPE_CAR.getBytes();
                break;
            default:
                String answer_temp = Response.NO_ANSWER + ", For This Request Code " + (int) request;
                System.err.println(answer_temp);
                this.answer = answer_temp.getBytes();
                break;
        }
    }

    public Header getHeader() {
        return header;
    }

    public byte[] getAnswer() {
        return answer;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(100);
        byteArrayBuffer.write(header.getBytes());
        byteArrayBuffer.write(getAnswer());
        return byteArrayBuffer.toByteArray();
    }

}
