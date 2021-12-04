package Parsing;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class Underlay {
    List<Entity> entities;
    List<Link> links;


    // Constructors
    public Underlay ( List<Entity> entities, List<Link> links) {
        this.entities = new ArrayList<>(entities);
        this.links = new ArrayList<>(links);
    }

    public Underlay ( String file ) throws Exception {
        this.entities = new ArrayList<>();
        this.links = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document document = builder.parse(new File( file ));
        document.getDocumentElement().normalize();
    
        parseNetworks(document);
        parseDevices(document);
        parseLinks(document);
            
        // Print de teste podem tirar se quiserem
        for (int i = 0; i < entities.size(); i++) {
            System.out.println("ENTITY");
            System.out.println(this.entities.get(i).getId());
            System.out.println(this.entities.get(i).getName());
            System.out.println(this.entities.get(i).getPositionX());
            System.out.println(this.entities.get(i).getPositionY());
            System.out.println();
        }

        for (int i = 0; i < links.size(); i++) {
            System.out.println("LINK");
            System.out.println(this.links.get(i).getNode1());
            System.out.println(this.links.get(i).getNode2());
            System.out.println(this.links.get(i).getIp());
            System.out.println();
        }
    }


    // PARSERS (ONLY USE HERE)
    private void parseNetworks(Document document) {
        NodeList networks = document.getElementsByTagName("network");
        for (int i = 0; i < networks.getLength(); i++) {
            Node networkNode = networks.item(i);
            Element networkElement = (Element) networkNode;
            Network network = new Network();
            network.setId(Integer.parseInt( networkElement.getAttribute("id") ));
            network.setName(networkElement.getAttribute("name"));

            NodeList positions = networkElement.getElementsByTagName("position");
            Node position = positions.item(0);
            Element postionElement = (Element) position;
            network.setPositionX(Integer.parseInt( postionElement.getAttribute("x") ));
            network.setPositionY(Integer.parseInt( postionElement.getAttribute("y") ));

            this.entities.add(network);                
        }
    }

    private void parseDevices(Document document) {
        NodeList devices = document.getElementsByTagName("device");
        for (int i = 0; i < devices.getLength(); i++) {
            Node deviceNode = devices.item(i);
            Element deviceElement = (Element) deviceNode;
            Device device = new Device();
            device.setId(Integer.parseInt( deviceElement.getAttribute("id") ));
            device.setName(deviceElement.getAttribute("name"));

            NodeList positions = deviceElement.getElementsByTagName("position");
            Node position = positions.item(0);
            Element postionElement = (Element) position;
            device.setPositionX(Integer.parseInt( postionElement.getAttribute("x") ));
            device.setPositionY(Integer.parseInt( postionElement.getAttribute("y") ));

            this.entities.add(device);                
        }
    }

    private void parseLinks(Document document) {
        NodeList links = document.getElementsByTagName("link");
        for (int i = 0; i < links.getLength(); i++) {
            Node linkNode = links.item(i);
            Element linkElement = (Element) linkNode;
            Link link = new Link();
            link.setNode1(Integer.parseInt( linkElement.getAttribute("node1")));
            link.setNode2(Integer.parseInt( linkElement.getAttribute("node2")));

            NodeList iface2s = linkElement.getElementsByTagName("iface2");
            Node iface2 = iface2s.item(0);
            Element iface2Element = (Element) iface2;
            link.setIp(iface2Element.getAttribute("ip4"));

            this.links.add(link);                
        }
    }

    public static void main(String[] args) throws Exception{
        Underlay underlay = new Underlay("Parsing/overlay1.xml");
    }
}
