package de.hftstuttgart.vs.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    public static void main(String[] args) {

        Socket vSocket = null;
        boolean gameRunning = true;

        try {
            vSocket = new Socket("localhost", 50737);
            OutputStream vOutput = vSocket.getOutputStream();
            PrintStream vOutputWriter = new PrintStream(vOutput);

            InputStream vInput = vSocket.getInputStream();
            BufferedReader vInputReader = new BufferedReader(new InputStreamReader(vInput));

            Scanner scan = new Scanner(System.in);
            System.out.println("Enter Coordinate Format: x,y | Values Range (1-3)");
            while(gameRunning){
                System.out.print("Enter Value: ");
                String move = scan.nextLine();
                vOutputWriter.println(move);

                while (!vInputReader.ready()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (vInputReader.ready()) {
                    String vResponse = vInputReader.readLine();
                    if(vResponse.equals("END")){
                        gameRunning =false;
                    }
                    System.out.println(vResponse);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(vSocket!=null){
                try {
                    vSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
