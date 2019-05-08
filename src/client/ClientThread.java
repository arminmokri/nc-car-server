package client;

import client.request.Request;
import client.response.Response;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
                    Request heartBeat = new Request(Request.HEARTBEAT);
                    Request(heartBeat);
                    String ans = new String(heartBeat.getAnswer());
                    //System.out.println(ans);
                    if (!ans.equals(Response.HEARTBEAT_YES)) {
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
                            Response response = new Response(
                                    request_temp.getHeader(), request_temp.getRequest()
                            );
                            socketThread.dataOutputStreamWrite(response.getBytes());
                            break;
                        case Header.RESPONSE:
                            Response response_temp = new Response(data);
                            for (int i = 0; i < requests.size(); i++) {
                                Request request = requests.get(i);
                                if (response_temp.getHeader().getTicket().equals(
                                        request.getHeader().getTicket())) {
                                    requests.remove(request);
                                    request.setAnswer(response_temp.getAnswer());
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
                super.stop();
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
            socketThread.dataOutputStreamWrite(request.getBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
