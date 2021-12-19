package connection;

import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


public class UDPRouter {
    private int      id;
    private Caminhos caminhos;

    private InetAddress serverAddress; 
    private int         serverPort;  

    private int         clientToClientPort; 
    private int         clientToServerPort;
    
    private DatagramSocket dSocketStream;
    private DatagramSocket dSocketControl;


    // Constructor
    public UDPRouter(int id) throws Exception {
        this.id       = id;
        this.caminhos = new Caminhos();

        this.clientToClientPort = 5000 + this.id;
        this.clientToServerPort = 4000 + this.id;
        this.dSocketStream  = new DatagramSocket(this.clientToClientPort);
        this.dSocketControl = new DatagramSocket(this.clientToServerPort);

        this.serverAddress = InetAddress.getByName("10.0.0.10"); 
        //this.serverAddress = InetAddress.getByName("localhost");
        this.serverPort    = 4001;
    }


    // Sends ON flag to server
    private void turnON() throws Exception {
        byte[] buffer          = ("CON:" + this.id).getBytes(StandardCharsets.UTF_8);
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);
        this.dSocketControl.send(dPacket);
    }

    // Run the client
    public void run() throws Exception {
        turnON(); // We first tell the server who we are
        new Thread( new ClientON_CR(this.id, this.caminhos, this.dSocketControl) ).start();

        System.out.println("WE ARE GETTING READY YO");
        TimeUnit.SECONDS.sleep(5);
        System.out.println("WE ARE READY YO");

        // We listen to packets to send
        byte[] buffer           = new byte[8];
        DatagramPacket dPacket  = new DatagramPacket(buffer, buffer.length);
        this.dSocketStream.receive(dPacket);      
        
        ByteBuffer wrapped = ByteBuffer.wrap(buffer);
        int destinoID = wrapped.getInt();
        System.out.println("Destino: " + destinoID);
        new Thread( new StreamSend(this.caminhos, destinoID)).start();
        
        Vou escrever sem comentario para veres
        // Agora temos server router e client
        // Para isto dar tem de ser testado no core, não dá para testar fora porque os ip4 dos nodos são os do core
        
        // Para testar:
        // -> inicias o server primeiro sempre
        // -> Depois ligas um vizinho do server
        // -> Quando aparecer nesse vizinho "WE ARE READY YO" podes ligar outro, antes dá merda
        // -> Se ligares um cliente que não tem caminho para o server aparece a mensagem "cant reach".
        //    Se isso acontecer e quiseres mandar alguma coisa para esse gajo tens de criar caminho e tens de o desligar e voltar a ligar.
        // -> Não uses uma topologia com switches.
        // -> Pode ter algum bug mas em principio não tirando o facto de teres de esperar para ligar merdas e teres de desligar
        //    e voltar a ligar se não houver caminho

        // O router é meio cliente meio servidor: 
        // -> Como um cliente a primeira merda que faz é ligar-se ao servidor para saber os caminhos
        // -> Como um servidor envia as cenas (a função da thread até é a mesma)
        
        // O cliente só espera até receber as coisas. Não sei se é suposta o caminho começar sempre do servidor ou se é 
        // para simplesmente vir do vizinho mais proximo mas nos temos a vir do servidor.

        // Agora não sei o que queres começar a fazer:
        // -> Podes tentar melhorar isto do genero, meter switches (não recomendo começar por isso)
        // -> Fazer com que ele não seja preciso desligar e voltar a ligar para encontrar caminho.
        // -> Meter a pedir ao vizinho mais proximo ligado em vez do servidor.
        // -> Começar a juntar o codigo do stor ao nosso.

        // A prioridade é o codigo do stor porque isto parece me funcional mas podes querer começar por
        // outro lado é o que preferires.

        // De novas classes só temos o StreamSend que era o Stream_S (igualzinho acho eu) porque no router e no servidor
        // faz se exatamente a mesma merda e no cliente só se recebe por isso não precisamos de mais nada.
        // Se calhar com o codigo do stor vai ser preciso alterações mas para já não sei por isso ta assim muito simples
    }

}

