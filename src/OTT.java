import connection.UDPServer;
import connection.UDPClient;
import connection.UDPRouter;

public class OTT {
    public static void main(String[] argvs) throws Exception{
        if(argvs[0].equals("c")) {
            System.out.println("CLIENT RUNNING");
            UDPClient client = new UDPClient(Integer.parseInt(argvs[1]));
            client.run();
        }
        else if(argvs[0].equals("r")) {
            System.out.println("ROUTER RUNNING");
            UDPRouter router = new UDPRouter(Integer.parseInt(argvs[1]));
            router.run();
        }
        else if(argvs[0].equals("s")) {
            System.out.println("SERVER RUNNING");
            UDPServer server = new UDPServer("underlay3.xml", Integer.parseInt(argvs[1]));
            server.run();
        }
        else System.out.println("INVALID. TERMINATING");
    }
}
