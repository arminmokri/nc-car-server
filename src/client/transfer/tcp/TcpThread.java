/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.transfer.tcp;

import client.DataInput;
import client.transfer.TransferProtocol;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author armin
 */
public class TcpThread extends Thread {

    // packet header, footer
    private final byte[] packetHeader = {(byte) 0x00, (byte) 0x00, (byte) 0x00};
    private final byte[] packetFooter = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    //
    private final InetAddress ServerInetAddress;
    private final int ServerPort;
    private final int bytesSize;
    //
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    //
    private Vector<DataInput> dataInputs;
    //
    private boolean Running;

    public TcpThread(String ServerIP, int ServerPort, Vector<DataInput> dataInputs, String name) throws Exception {
        this(ServerIP, ServerPort, 8 * 1024, dataInputs, name);
    }

    public TcpThread(String ServerIP, int ServerPort, int bytesSize, Vector<DataInput> dataInputs, String name) throws Exception {
        super(name + "->" + "TcpThread");
        //
        this.ServerInetAddress = InetAddress.getByName(ServerIP);
        this.ServerPort = ServerPort;
        this.bytesSize = bytesSize;
        this.dataInputs = dataInputs;
        //
        this.socket = new Socket(this.ServerInetAddress, this.ServerPort);
        //
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());

    }

    public TcpThread(Socket socket, Vector<DataInput> dataInputs, String name) throws Exception {
        this(socket, 8 * 1024, dataInputs, name);
    }

    public TcpThread(Socket socket, int bytesSize, Vector<DataInput> dataInputs, String name) throws Exception {
        super(name + "->" + "TcpThread");
        //
        this.ServerInetAddress = socket.getInetAddress();
        this.ServerPort = socket.getPort();
        this.bytesSize = bytesSize;
        this.dataInputs = dataInputs;
        //
        this.socket = socket;
        //
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());

    }

    @Override
    public void run() {
        while (this.isRunning()) {
            try {
                int count;
                byte[] bytes = new byte[bytesSize];
                if ((count = dataInputStream.read(bytes)) > 0) {
                    byte[] remaining = Arrays.copyOfRange(bytes, 0, count);
                    while (remaining.length > 0) {
                        //
                        int index_header = Bytes.indexOf(remaining, packetHeader);
                        int index_footer = Bytes.indexOf(remaining, packetFooter);
                        byte[] packet = Arrays.copyOfRange(
                                remaining,
                                index_header + packetHeader.length,
                                index_footer
                        );
                        DataInput dataInput = new DataInput(TransferProtocol.TCP, packet);
                        dataInputs.add(dataInput);
                        // get remaining of bytes
                        remaining = Arrays.copyOfRange(
                                remaining,
                                index_footer + packetFooter.length,
                                remaining.length
                        );
                    }
                }

            } catch (EOFException exception) {
                exception.printStackTrace();
                Stop();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void start() {
        if (!this.isRunning()) {
            this.setRunning(true);
            super.start();
        }
    }

    public synchronized void Stop() {
        if (this.isRunning()) {
            this.setRunning(false);

            try {
                dataInputStream.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                dataOutputStream.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                socket.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                super.stop();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    protected boolean isRunning() {
        return Running;
    }

    protected void setRunning(boolean Running) {
        this.Running = Running;
    }

    public synchronized void dataOutputStreamWrite(byte[] bytes) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        byteArrayOutputStream.write(packetHeader);
        byteArrayOutputStream.write(bytes);
        byteArrayOutputStream.write(packetFooter);
        dataOutputStream.write(byteArrayOutputStream.toByteArray());
    }
}
