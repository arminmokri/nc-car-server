/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.response;

import client.ClientThread;
import client.request.Request;

/**
 *
 * @author armin
 */
public class ResponseThread extends Thread {

    private Request request;
    private ClientThread clientThread;

    public ResponseThread(Request request, ClientThread clientThread, String name) {
        super(name + "->" + "ResponseThread");
        //
        this.request = request;
        this.clientThread = clientThread;
    }

    @Override
    public void run() {
        try {
            Response response = new Response(request, clientThread);
            clientThread.Response(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        super.stop();
    }

}
