package de.hftstuttgart.vs.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private final ServerSocket mServerSocket;
    private Thread test;

    public SocketServer(int aPort) throws IOException {
        mServerSocket = new ServerSocket(aPort);
    }

    public static void main(String[] args) throws IOException {
        SocketServer vServer = new SocketServer(50737);
        vServer.listen();
    }

    private void listen() {

        while (true) {
            Socket vSocket;
            try {
                vSocket = mServerSocket.accept();

                test = new Thread(new HandleUser(vSocket));
                test.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doIO(Socket vSocket) throws IOException {
        InputStream vInput = vSocket.getInputStream();
        BufferedReader vInputReader = new BufferedReader(new InputStreamReader(vInput));
        OutputStream vOutput = vSocket.getOutputStream();
        PrintStream vOutputWriter = new PrintStream(vOutput);
        TikTakToe game = new TikTakToe();
       while(game.isRunning()){

           while (!vInputReader.ready()) {
               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }

           while (vInputReader.ready()) {
               String vRequest = vInputReader.readLine();
               String[] coords = vRequest.split(",");
               int response = game.enterVal(Integer.parseInt(coords[0])-1,Integer.parseInt(coords[1])-1);
               checkOutcome(vOutputWriter, game, response);

               if(game.running) {
                   vOutputWriter.println("AI Move: ");
                   int responseAI = game.aiMove();
                   checkOutcome(vOutputWriter, game, responseAI);
               }
           }
       }

    }

    private void checkOutcome(PrintStream vOutputWriter, TikTakToe game, int response) {
        vOutputWriter.println(game.getBoardString());
        switch (response){
            case -1:
                break;
            case 0:
                vOutputWriter.println("It is a DRAW!");
                vOutputWriter.println("END");
                return;
            case 1:
                vOutputWriter.println("AI WON!");
                vOutputWriter.println("END");
                return;
            case 2:
                vOutputWriter.println("YOU WON!");
                vOutputWriter.println("END");
                return;

        }

    }

    public class HandleUser implements Runnable {
        Socket aSocket;

        public HandleUser(Socket socket){
            aSocket = socket;
        }

        @Override
        public void run() {
            try {
                doIO(aSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(aSocket != null){
                    try {
                        aSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
