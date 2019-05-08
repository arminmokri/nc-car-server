/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author armin
 */
public class SocketThread extends Thread {

    //
    private final int bytesSize;
    //
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    //
    private Vector<byte[]> bytesDataInputStream;
    //
    private boolean Running;

    public SocketThread(Socket socket, String name) throws Exception {
        this(socket, 8 * 1024, name);
    }

    public SocketThread(Socket socket, int bytesSize, String name) throws Exception {
        super(name + "->" + "SocketThread");
        //
        this.bytesSize = bytesSize;
        //
        this.bytesDataInputStream = new Vector<>();
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
                    bytesDataInputStream.add(Arrays.copyOfRange(bytes, 0, count));
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

    protected synchronized void dataOutputStreamWrite(byte[] bytes) throws IOException {
        dataOutputStream.write(bytes);
    }

    /*
    protected synchronized void dataOutputStreamWrite(ByteArrayBuffer byteArrayBuffer) throws IOException {
        this.dataOutputStreamWrite(byteArrayBuffer.toByteArray());
    }
     */
    protected Vector<byte[]> getBytesDataInputStream() {
        return bytesDataInputStream;
    }

}
