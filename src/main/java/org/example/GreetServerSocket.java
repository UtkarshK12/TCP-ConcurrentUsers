package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class GreetServerSocket {

    private ServerSocket serverSocket;

    //We create an atomic boolean because this value can be accessed even via different threads. This atomicBoolean is visible by even other threads
    // we set this to true so that another thread doesnt try to start a new server if a server is already running in one thread
    private AtomicBoolean running = new AtomicBoolean(true);

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        //we check the value of atomic boolean to see if the server is running in another thread
        while(running.get()){

            //here we basically keep running the while loop while looking for connections
            //once we have a new connection, basically we accept a new client connection
            //once a new connection is estabilished, start() invokes a new thread
            new MultiThreadedSocketHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException {

        running.set(false);
        if(serverSocket!=null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        GreetServerSocket greetServerSocket = new GreetServerSocket();
        greetServerSocket.start(10280);
    }
}