package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiThreadedSocketHandler extends Thread{

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    //Be sure to send the parameters in consturctor
    public MultiThreadedSocketHandler(Socket connection){
        this.clientSocket=connection;
    }

    //Multithread classes implement the Thread interface
    //these implement a function run which runs the main program of the class. Nothing special here.
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(),true);

            String inputLine;
            while ((inputLine=in.readLine())!=null){
                if(inputLine.equalsIgnoreCase("Hello")){
                    out.println("Hey");
                }
                else {
                    out.println("Cant recognize");
                }
            }

            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
