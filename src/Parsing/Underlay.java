package Parsing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.Map;

public class Underlay {
    private Map<Integer, Device> entities;
    private Map<Integer, List<Link>> links;

    // Constructors
    public Underlay ( Map<Integer, Device> entities, Map<Integer, List<Link>> links) {
        this.entities = new HashMap<>(entities);
        this.links = new HashMap<>(links);
    }

    public Underlay ( String file ) throws Exception {
        this.entities = new HashMap<>();
        this.links = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document document = builder.parse(new File( file ));
        document.getDocumentElement().normalize();
    
        parseNetworks(document);
        parseDevices(document);
        parseLinks(document);

    }

    public Map<Integer, List<Link>> getLinks() {
        return links;
    }

    public Map<Integer, Device> getEntities() {
        return entities;
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

            this.entities.put(device.getId(), device);
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

            // Node 1
            if(!this.links.containsKey(link.getNode1())){
                List<Link> auxLink = new ArrayList<>();
                auxLink.add(link);
                this.links.put(link.getNode1(), auxLink);
            }
            else{
                List<Link> auxLink = new ArrayList<>(this.links.get(link.getNode1()));
                auxLink.add(link);
                this.links.put(link.getNode1(), auxLink);
            }
            // Node 2
            if(!this.links.containsKey(link.getNode2())){
                List<Link> auxLink = new ArrayList<>();
                auxLink.add(link);
                this.links.put(link.getNode2(), auxLink);
            }
            else{
                List<Link> auxLink = new ArrayList<>(this.links.get(link.getNode2()));
                auxLink.add(link);
                this.links.put(link.getNode2(), auxLink);
            }

        }
    }

    @Override
    public String toString() {
        return "Underlay{" +
                "entities=" + entities + "\n" +
                ", links=" + links +
                '}';
    }

    public static void main(String[] args) throws Exception{
        Underlay underlay = new Underlay("src/Parsing/underlay1.xml");
        System.out.println(underlay);
    }
}
