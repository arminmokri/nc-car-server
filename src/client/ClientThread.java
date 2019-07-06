/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.transfer.TransferProtocol;
import client.transfer.tcp.TcpThread;
import client.parameters.Parameter;
import client.parameters.Parameters;
import client.request.Request;
import client.response.Response;
import client.response.ResponseThread;
import config.Car;
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
    private Vector<DataInput> dataInputs;
    private Vector<Request> requests;
    private Vector<Response> responses;
    //
    private TcpThread tcpThread;
    //
    private boolean Running;
    //
    private Timer HeartBeat;
    //
    private Car car;

    public ClientThread(String ServerIP, int ServerPort, String name) throws Exception {
        super(name + "->" + "ClientThread");
        //
        dataInputs = new Vector<>();
        requests = new Vector<>();
        responses = new Vector<>();
        //
        tcpThread = new TcpThread(ServerIP, ServerPort, dataInputs, getName());
        //
        HeartBeat = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Parameters parameters = new Parameters();
                    parameters.add(Parameter.ACTION, Parameter.HEARTBEAT);
                    Request heartBeat = new Request(TransferProtocol.TCP, parameters);
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
        dataInputs = new Vector<>();
        requests = new Vector<>();
        responses = new Vector<>();
        //
        tcpThread = new TcpThread(socket, dataInputs, getName());
        //
        HeartBeat = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Parameters parameters = new Parameters();
                    parameters.add(Parameter.ACTION, Parameter.HEARTBEAT);
                    Request heartBeat = new Request(TransferProtocol.TCP, parameters);
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
                while (!dataInputs.isEmpty()) {

                    DataInput dataInput = dataInputs.remove(0);
                    //System.out.println(Arrays.toString(dataInput.getBytes()));

                    switch (dataInput.getBytes()[0]) {
                        case Header.REQUEST:
                            Request request_temp = new Request(dataInput.getTransferProtocol(), dataInput.getBytes());
                            ResponseThread responseThread = new ResponseThread(request_temp, this, getName());
                            responseThread.start();
                            break;
                        case Header.RESPONSE:
                            Response response_temp = new Response(dataInput.getBytes());
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
                            dataInput = null;
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
            tcpThread.start();
            //
            HeartBeat.start();
        }
    }

    public void Stop() {
        if (this.isRunning()) {
            this.setRunning(false);

            try {
                if (car != null) {
                    car.unsetClient(this);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                HeartBeat.stop();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                tcpThread.Stop();
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
            dataOutputWrite(request.getTransferProtocol(), request.getBytes());
            requests.add(request);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void Response(Response response) {
        try {
            dataOutputWrite(response.getTransferProtocol(), response.getBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void dataOutputWrite(TransferProtocol transferProtocol, byte[] bytes) throws IOException, Exception {
        if (transferProtocol.equals(TransferProtocol.TCP)) {
            tcpThread.dataOutputStreamWrite(bytes);
        } else if (transferProtocol.equals(TransferProtocol.UDP)) {
            throw new Exception("Not implemented yet.");
        } else {
            throw new Exception("Unknown TransferProtocol.");
        }
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

}
