package Parsing;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;


public class AddressTable {
    private HashMap<Integer, Links> table;

    // --- CONSTRUCTOR ---
    public AddressTable() {
        this.table = new HashMap<Integer, Links>();
    }

    public AddressTable(AddressTable addressTable) {
        this.table = addressTable.getTable();
    }

    public AddressTable(String file) {
        this.table = new HashMap<Integer, Links>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
        
            Document document = builder.parse(new File( file ));
            document.getDocumentElement().normalize();

            NodeList linkNodeList = document.getElementsByTagName("link");
            for (int i = 0; i < linkNodeList.getLength(); i++) {
                Link node1 = new Link();
                Link node2 = new Link(); 
    
                Node linkNode = linkNodeList.item(i);
                Element linkElement = (Element) linkNode;
                // Preenchemos cada node com o seu respetivo id 
                node1.setID(Integer.parseInt( linkElement.getAttribute("node1")));
                node2.setID(Integer.parseInt( linkElement.getAttribute("node2")));
    
                // Agora vamos ver as interfaces 1 e 2 respetivamente
                NodeList iface1NodeList = linkElement.getElementsByTagName("iface1");
                Node iface1Node = iface1NodeList.item(0);
                if (iface1Node != null) {
                    Element iface1Element = (Element) iface1Node;
                    node1.setIP4(iface1Element.getAttribute("ip4"));
                }
                NodeList iface2NodeList = linkElement.getElementsByTagName("iface2");
                Node iface2Node = iface2NodeList.item(0);
                if (iface2Node != null) {
                    Element iface2Element = (Element) iface2Node;
                    node2.setIP4(iface2Element.getAttribute("ip4"));
                }
    
                Links links1 = new Links(node1.getID(), node2);
                Links links2 = new Links(node2.getID(), node1); 
                
                if (this.table.containsKey(node1.getID())) {
                    this.table.get(node1.getID()).addLinks(links1);
                }
                else {
                    this.table.put(node1.getID(), links1);
                }

                if (this.table.containsKey(node2.getID())) {
                    this.table.get(node2.getID()).addLinks(links2);
                }
                else {
                    this.table.put(node2.getID(), links2);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Overlay Parsing NOT OK");
        }
    }


    // --- GETTERS ---
    public HashMap<Integer, Links> getTable() {
        return this.table;
    }

    public ArrayList<Links> getAllLinks() {
        return new ArrayList<Links>(this.table.values());
    }

    public Links getLinks(int id) {
        return this.table.get(id);
    }

    public Set<Integer> getKeys() {
        return this.table.keySet();
    }


    // --- SETTERS ---
    public void setTable(HashMap<Integer, Links> links) {
        this.table = links;
    }

    public void addLinks(int id, Links links) {
        if (this.table.containsKey(id)) {
            this.table.get(id).addLinks(links);
        }
        else {
            this.table.put(id, links);
        }
    }


    // --- OTHER ---
    public void print() {
        System.out.println("--- TABLE ----");
        System.out.println();
        for(Links links : this.table.values()) {
            links.print();
        }
    }

    // calcula o proximo candidato
    private int min(HashMap<Integer, Integer> fringe, HashMap<Integer, Integer> dist, int size){
        int distMC = Integer.MAX_VALUE;
        int valor = -1;

        for(int i = 1; i <= size; i++) {
            if (fringe.get(i) != null && distMC > fringe.get(i) + dist.get(i)) {
                valor = i;
                distMC = fringe.get(i) + dist.get(i);
            }
        }
        return valor;
    }

    public HashMap<Integer, Integer> caminhoMaisCurto(int nodo){

        // Numero de Devices incluido networks
        //int size = underlay.getKeys().size();
        int size = this.table.size();

        // Criar os arrays necessários
        HashMap<Integer, Integer> fringe = new HashMap<>();
        HashMap<Integer, String> status = new HashMap<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer, Integer> parent = new HashMap<>();

        // Inicializar status como não visitado
        for(int i = 1; i <= size; i++) status.put(i, "UNSEEN");

        // total de vizitados inicializado
        int edgeCount = 0;
        int nodoAtual = nodo;

        // Colocar o nodo atual na arvore e marcar o seu pai e a distancia como -1 e 0 respetivamente
        status.put(nodoAtual, "INTREE");
        parent.put(nodoAtual, -1);
        dist.put(nodoAtual, 0);

        // Enquanto todos os nodos não forem nodo atual
        while (edgeCount < size -1) {
            for (Link l: this.table.get(nodoAtual).getLinks()) { // verificamos os links do nodo atual
                int wxy = 1;                                     // marcamos a distancia entre nodos como 1 porque contabilizamos o nr de saltos
                int y = l.getID();                               // id do nodo correspondente

                if (Objects.equals(status.get(l.getID()), "UNSEEN")) { // Caso o nodo ainda não esteja vizitado
                    status.put(y, "FRINGE");                           // Metemos o nodo na orla
                    parent.put(y, nodoAtual);                          // O pai do nodo é o nodo atual
                    fringe.put(y, wxy);                                // na orla adicionamos a distancia entre o nodo atual e o nodo y

                    dist.put(y, dist.get(nodoAtual) + wxy); // distancia entre o nodo inicial e o nodo y
                } 
                else if (Objects.equals(status.get(y), "FRINGE") && dist.get(nodoAtual) + wxy < dist.get(y)) { // caso esteja na orla e obtenha um caminho mais curto atualiza a orla
                    parent.put(y, nodoAtual);
                    fringe.put(y, wxy);
                    dist.put(y, dist.get(nodoAtual) + wxy);
                }
            }

            if (fringe.size() == 0)
                return null;

            nodoAtual = min(fringe, dist, size); // calcula proximo nodo a consultar
            fringe.remove(nodoAtual);            // remove o proximo nodo da orla
            edgeCount++;                         // incrementa os visitados
            System.out.println("PARENT\n" + parent);    
        }

        return parent;
    }

    public static void main(String[] args) {
        AddressTable table = new AddressTable("underlay1.xml");
        table.print();
    }
}