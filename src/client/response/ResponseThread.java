/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.response;

import client.ClientThread;
import client.request.Request;
import java.io.IOException;

/**
 *
 * @author armin
 */
public class ResponseThread extends Thread {

    private Request request;
    private ClientThread clientThread;

    public ResponseThread(Request request, ClientThread clientThread) {
        this.request = request;
        this.clientThread = clientThread;
    }

    @Override
    public void run() {
        try {
            Response response = new Response(request, clientThread);
            clientThread.dataOutputStreamWrite(response.getBytes());
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        super.stop();
    }

}
