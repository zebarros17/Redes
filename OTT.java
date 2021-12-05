import java.util.Scanner;

public class OTT {

    public static void main(String argvs[]) throws Exception{
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 
        System.out.println("S: INIT SERVER");
        System.out.println("C: INIT CLIENT");

        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();
        
        if(action.equals("S")) { 
            System.out.println("SERVER RUNNING");
            UDPServer server = new UDPServer();
            server.run();
        }
        if(action.equals("C")) {
            System.out.println("CLIENT RUNNING");
            UDPClient client = new UDPClient();
            client.run();
        }    
        else {
            System.out.println("INVALID. TERMINATING");
        }
        scanner.close();
    }
}

