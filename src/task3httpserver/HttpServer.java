package task3httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 18.02.13
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public class HttpServer {
    public static void main(String [] args) throws IOException {
        if ( args.length > 1){
            int port = Integer.parseInt(args[0]);
            ServerSocket serverSocket = null;
            try{
                serverSocket = new ServerSocket(port);
            }catch (IOException e){
                System.out.println("Порт " + port + " заблокирован.");
                System.exit(-1);
            }

            while (true){
                Socket clientSocket = serverSocket.accept();
                System.err.println("Клиент принят");
                new Thread(new SocketProcessor(clientSocket, args [1])).start();
            }
        }else {
            System.out.println("Аргументы программы не указаны");
        }

    }
}
