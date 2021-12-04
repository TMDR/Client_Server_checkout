import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws Exception {
        int choice = 1;Scanner sc;
        do{
            System.out.println("You can choose between TCP and UDP but we advise you to choose TCP as it provides 3 way handshake and every packet is ensured to be delivered. Press 1 for TCP and 2 for UDP");
            sc = new Scanner(System.in);
            choice = sc.nextInt();
        }while(choice < 1 || choice > 2);
        sc.close();

        switch(choice){
            case 1:
                ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));//this waits for client connections
                Socket s = new Socket();
                try{
                    ss.setSoTimeout(Integer.parseInt(args[2]));
                    s = ss.accept();//we wait for only one client
                    s.setSoTimeout(Integer.parseInt(args[2]));
                    while(true){
                        s.getOutputStream().write("t".getBytes());
                        s.getInputStream().read();
                        System.out.println(s.getInetAddress().toString()+" is connected");
                        Thread.sleep(Integer.parseInt(args[1]));
                    }
                }
                catch(SocketTimeoutException ex){
                    System.out.println("Network is down");
                    s.close();
                    ss.close();
                }
                catch(SocketException e){
                    if(e.getMessage().equals("Connection reset"))
                        System.out.println("Client is down");
                    else
                        System.out.println("Network is down");
                    s.close();
                    ss.close();
                }
                break;
            case 2:
                DatagramSocket ds = new DatagramSocket(Integer.parseInt(args[0]));
                ds.setSoTimeout(Integer.parseInt(args[2]));
                try{
                    while(true){
                        byte b[] = new byte[1];
                        DatagramPacket dp = new DatagramPacket(b, b.length);
                        ds.receive(dp);
                        InetAddress address = dp.getAddress();
                        int port = dp.getPort();
                        System.out.println("client with address : "+address.toString()+" is UP and connected to me right now");
                        dp = new DatagramPacket(dp.getData(), dp.getData().length, address, port);
                        ds.send(dp);
                        Thread.sleep(Integer.parseInt(args[1]));
                    }
                }
                catch(SocketException | SocketTimeoutException e){
                    System.out.println("Network is down");
                    ds.close();
                }
                break;
        }
    }
}
