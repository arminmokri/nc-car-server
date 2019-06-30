/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.transfer;

/**
 *
 * @author armin
 */
public class TransferProtocol {

    //
    public static final String STRING_TCP = "tcp";
    public static final String STRING_UDP = "udp";
    //
    public static final TransferProtocol TCP = new TransferProtocol(TransferProtocol.STRING_TCP);
    public static final TransferProtocol UDP = new TransferProtocol(TransferProtocol.STRING_UDP);
    //
    private String protocol;

    public TransferProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    @Override
    public boolean equals(Object obj) {
        TransferProtocol transferProtocol = (TransferProtocol) obj;
        return this.getProtocol().equals(transferProtocol.getProtocol());
    }

}
