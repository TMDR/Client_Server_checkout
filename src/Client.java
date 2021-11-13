import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        int choice = 1;Scanner sc;
        do{
            System.out.println("You can choose between TCP and UDP but we advise you to choose TCP as it provides 3 way handshake and every paccket is ensured to be delivered. Press 1 for TCP and 2 for UDP");
            sc = new Scanner(System.in);
            choice = sc.nextInt();
        }while(choice < 1 || choice > 2);
        sc.close();
        switch(choice){
            case 1:
                Socket s = new Socket();
                try{
                    s = new Socket(args[0],Integer.parseInt(args[1]));
                    s.setSoTimeout(Integer.parseInt(args[3]));
                    while(true){
                        s.getInputStream().read();
                        s.getOutputStream().write("t".getBytes());
                        System.out.println(s.getInetAddress().toString()+" is connected");
                        Thread.sleep(Integer.parseInt(args[2]));
                    }
                }
                catch(SocketTimeoutException ex){
                    System.out.println("Network is down");
                    s.close();
                }
                catch(SocketException e){
                    System.out.println("Network is down");
                    s.close();
                }
                break;
            case 2:
                DatagramSocket ds = new DatagramSocket();
                ds.setSoTimeout(Integer.parseInt(args[3]));
                try{
                    while(true){
                        byte b[] = new byte[1];
                        b[0] = (byte)'t';
                        DatagramPacket dp = new DatagramPacket(b, b.length,InetAddress.getByName(args[0]),Integer.parseInt(args[1]));
                        ds.send(dp);
                        dp = new DatagramPacket(dp.getData(), dp.getData().length);
                        ds.receive(dp);
                        InetAddress address = dp.getAddress();
                        System.out.println("Server with address : "+address.toString()+" is UP and connected to me right now");
                        Thread.sleep(Integer.parseInt(args[2]));
                    }
                }
                catch(SocketException e){
                    System.out.println("Network is down");
                    ds.close();
                }
                break;
        }
    }
}
