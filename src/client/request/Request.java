/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.request;

import client.Header;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.IOException;

/**
 *
 * @author armin
 */
public class Request {

    // 
    public static final byte HEARTBEAT = 0x01;
    public static final byte REGISTER = 0x02;
    public static final byte TYPE = 0x03;
    //
    private Header header;
    private byte request;
    private byte[] answer;
    private long ResponseTimeout;

    public Request() {
        this.header = new Header(Header.REQUEST);
        this.ResponseTimeout = 5000;
    }

    public Request(byte request) {
        this.header = new Header(Header.REQUEST);
        this.request = request;
        this.ResponseTimeout = 5000;
    }

    public Request(byte request, int ResponseTimeoutSec) {
        this.header = new Header(Header.REQUEST);
        this.request = request;
        this.ResponseTimeout = ResponseTimeoutSec * 1000;
    }

    public Request(byte[] bytes) {
        this.header = new Header(Header.REQUEST, bytes);
        this.request = bytes[11];
    }

    public long getResponseTimeout() {
        return ResponseTimeout;
    }

    public byte[] getAnswer() {
        try {
            long now = System.currentTimeMillis();
            while (answer == null && (System.currentTimeMillis() <= now + ResponseTimeout)) {
                Thread.sleep(25);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return answer;
    }

    public String getAnswerString() {
        return new String(getAnswer());
    }

    public void setAnswer(byte[] answer) {
        this.answer = answer;
    }

    public Header getHeader() {
        return header;
    }

    public byte getRequest() {
        return request;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(12);
        byteArrayBuffer.write(header.getBytes());
        byteArrayBuffer.write(request);
        return byteArrayBuffer.toByteArray();
    }

}
