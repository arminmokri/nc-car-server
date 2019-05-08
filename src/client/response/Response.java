/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.response;

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

    //
    public static final String HEARTBEAT_YES = "YES";
    public static final String NO_ANSWER = "NO ANSWER";
    //
    private Header header;
    private byte[] answer;

    public Response(Header header, byte request) {
        this.header = header;
        this.header.setType(Header.RESPONSE);
        setAnswer(request);
    }

    public Response(byte[] bytes) {
        this.header = new Header(Header.RESPONSE, bytes);
        this.answer = Arrays.copyOfRange(bytes, 11, bytes.length);
    }

    private void setAnswer(byte request) {
        if (request == Request.HEARTBEAT) {
            this.answer = Response.HEARTBEAT_YES.getBytes();
        } else {
            String answer = Response.NO_ANSWER + ", For This Request Code " + (int) request;
            this.answer = answer.getBytes();
            System.err.println(answer);
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
