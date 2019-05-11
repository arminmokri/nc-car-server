/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.parameters.Parameter;
import client.parameters.Parameters;
import client.request.Request;
import client.response.Response;
import client.response.ResponseThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.Timer;

/**
 *
 * @author Armin
 */
public class ClientThread extends Thread {

    //
    private SocketThread socketThread;
    //
    private Vector<Request> requests;
    private Vector<Response> responses;
    //
    private boolean Running;
    //
    private Timer HeartBeat;

    public ClientThread(String ServerIP, int ServerPort, String name) throws Exception {
        super(name + "->" + "ClientThread");
        //
        socketThread = new SocketThread(ServerIP, ServerPort, getName());
        //
        requests = new Vector<>();
        responses = new Vector<>();
        //
        HeartBeat = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Parameters parameters = new Parameters();
                    parameters.add(Parameter.ACTION, Parameter.HEARTBEAT);
                    Request heartBeat = new Request(parameters);
                    Request(heartBeat);
                    String result = heartBeat.getResponseParameters().getValue(Parameter.RESULT);
                    //System.out.println(ans);
                    if (!result.equals(Parameter.RESULT_1)) {
                        Stop();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Stop();
                }
            }
        });
    }

    //
    public ClientThread(Socket socket, String name) throws Exception {
        super(name + "->" + "ClientThread");
        //
        socketThread = new SocketThread(socket, getName());
        //
        requests = new Vector<>();
        responses = new Vector<>();
        //
        HeartBeat = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Parameters parameters = new Parameters();
                    parameters.add(Parameter.ACTION, Parameter.HEARTBEAT);
                    Request heartBeat = new Request(parameters);
                    Request(heartBeat);
                    String result = heartBeat.getResponseParameters().getValue(Parameter.RESULT);
                    //System.out.println(ans);
                    if (!result.equals(Parameter.RESULT_1)) {
                        Stop();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Stop();
                }
            }
        });
    }

    @Override
    public void run() {

        while (this.isRunning()) {
            try {
                while (!socketThread.getBytesDataInputStream().isEmpty()) {

                    byte[] data = socketThread.getBytesDataInputStream().remove(0);
                    //System.out.println(Arrays.toString(data));

                    switch (data[0]) {
                        case Header.REQUEST:
                            Request request_temp = new Request(data);
                            ResponseThread responseThread = new ResponseThread(request_temp, this);
                            responseThread.start();
                            break;
                        case Header.RESPONSE:
                            Response response_temp = new Response(data);
                            for (int i = 0; i < requests.size(); i++) {
                                Request request = requests.get(i);
                                if (response_temp.getHeader().getTicket().equals(
                                        request.getHeader().getTicket())) {
                                    requests.remove(request);
                                    request.setResponseParameters(response_temp.getResponseParameters());
                                    break;
                                }
                            }
                            break;
                        default:
                            data = null;
                            break;
                    }

                }
                Thread.sleep(25);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    @Override
    public synchronized void start() {
        if (!this.isRunning()) {
            this.setRunning(true);
            //
            super.start();
            //
            socketThread.start();
            //
            HeartBeat.start();
        }
    }

    public void Stop() {
        if (this.isRunning()) {
            this.setRunning(false);

            try {
                HeartBeat.stop();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                socketThread.Stop();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                this.stop();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return Running;
    }

    private void setRunning(boolean Running) {
        this.Running = Running;
    }

    public void Request(Request request) {
        try {
            requests.add(request);
            dataOutputStreamWrite(request.getBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void dataOutputStreamWrite(byte[] bytes) throws IOException {
        socketThread.dataOutputStreamWrite(bytes);
    }
}
