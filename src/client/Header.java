/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author armin
 */
public class Header {

    // header value
    public static final byte REQUEST = 0x01;
    public static final byte RESPONSE = 0x02;
    //
    private byte type;
    private Ticket ticket;

    public Header(byte type) {
        this.type = type;
        this.ticket = new Ticket();

    }

    public Header(byte type, byte[] bytes) {
        this.type = type;
        this.ticket = new Ticket(Arrays.copyOfRange(bytes, 1, 11));
    }

    public Ticket getTicket() {
        return ticket;
    }

    public static String getAnswer(String response) {
        return response.substring(11);
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(11);
        byteArrayBuffer.write(type);
        byteArrayBuffer.write(ticket.getValue());
        return byteArrayBuffer.toByteArray();
    }
}
