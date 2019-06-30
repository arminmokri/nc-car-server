/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.transfer.TransferProtocol;

/**
 *
 * @author armin
 */
public class DataInput {

    private TransferProtocol transferProtocol;
    private byte[] bytes;

    public DataInput(TransferProtocol transferProtocol, byte[] bytes) {
        this.transferProtocol = transferProtocol;
        this.bytes = bytes;
    }

    public TransferProtocol getTransferProtocol() {
        return transferProtocol;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
