package me.paraweb.orderbuilder.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import me.paraweb.orderbuilder.Orders.Order;

/**
 * Created by vault13 on 02.07.17.
 *
 * Класс для упаравления соединением с сервером, тут я сам
 *
 * Вызывать эту бурду по кнопкам, если можно))
 *
 */

public class Connection extends Thread{

    private String command;
    private String ipAdress;
    private int port;
    private Socket socket;
    OutputStream output;
    InputStream input;
    ObjectOutputStream objOut;
    Order order;

    public Connection(Order order) {
        super("connection");
        this.ipAdress = "127.0.0.1";
        this.port = 3000;
        this.order = order;
        //Метод старт плез!!!
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void run() {


        try {

            this.socket = new Socket(this.ipAdress, this.port);
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Место для выова методов

        switch (this.command){
            case "save":
                PackObject();
                break;
            case "get":
                SetTemplate();
                break;
        }

        try {

            this.input.close();
            this.output.close();
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SetTemplate(){

        //Задать реализацию с Викой

    }

    private void PackObject(){

        try {

            this.objOut = new ObjectOutputStream(this.output);
            this.objOut.writeObject(this.order);
            this.objOut.flush();

        } catch (IOException  e){
            e.printStackTrace();
        }

    }
}