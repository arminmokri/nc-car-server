/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author armin
 */
public class Ticket {

    private final byte[] value;

    public Ticket() {
        value = makeTicket();
    }

    public Ticket(byte[] value) {
        this.value = value;
    }

    private byte[] makeTicket() {
        byte[] value = new byte[10];
        for (int i = 0; i < value.length;) {
            char random = (char) ((('Z' - '0') + 1) * Math.random() + '0');
            if ((random >= '0' && random <= '9') || (random >= 'A' && random <= 'Z')) {
                value[i] = (byte) random;
                i++;
            }
        }
        return value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new String(value);
    }

    @Override
    public boolean equals(Object obj) {
        Ticket ticket = (Ticket) obj;
        return this.toString().equals(ticket.toString());
    }

}
