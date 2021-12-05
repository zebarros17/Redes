import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;


public class Overlay {
    private Devices devices;
    private AddressTable table;


    // Constructor
    public Overlay() {
        this.devices = new Devices();
        this.table = new AddressTable();
    }

    public Overlay(String file) {
        this.devices = new Devices();
        this.table = new AddressTable();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
        
            Document document = builder.parse(new File( file ));
            document.getDocumentElement().normalize();

            parseDevices(document);
            AddressTable underlay = parseLinks(document);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Overlay Parsing NOT OK");
        }
    }

    
    // GETTERS
    public Devices getDevices() {
        return this.devices;
    }

    public AddressTable getTable() {
        return this.table;
    }


    // SETTERS
    public void setDevices(Devices devices) {
        this.devices = devices;
    }

    public void setTable(AddressTable table) {
        this.table = table;
    }


    // PARSING -- ALL PRIVATE
    private void parseDevices (Document document) {
        NodeList deviceNodeList = document.getElementsByTagName("device");
        for (int i = 0; i < deviceNodeList.getLength(); i++) {
            Node deviceNode = deviceNodeList.item(i);
            Element deviceElement = (Element) deviceNode;
            Device device = new Device();
            device.setId(Integer.parseInt( deviceElement.getAttribute("id") ));
            device.setName( deviceElement.getAttribute("name") );
            this.devices.putDevice(device);
        }
    }

    private AddressTable parseLinks(Document document) {
        // Extrai todos os nodos do xml
        AddressTable underlay = new AddressTable();

        NodeList linkNodeList = document.getElementsByTagName("link");
        for (int i = 0; i < linkNodeList.getLength(); i++) {
            Link node1 = new Link();
            Link node2 = new Link(); 

            Node linkNode = linkNodeList.item(i);
            Element linkElement = (Element) linkNode;
            // Preenchemos cada node com o seu respetivo id 
            node1.setDeviceID(Integer.parseInt( linkElement.getAttribute("node1")));
            node2.setDeviceID(Integer.parseInt( linkElement.getAttribute("node2")));

            // Agora vamos ver as interfaces 1 e 2 respetivamente
            NodeList iface1NodeList = linkElement.getElementsByTagName("iface1");
            Node iface1Node = iface1NodeList.item(0);
            if (iface1Node != null) {
                Element iface1Element = (Element) iface1Node;
                node1.setIpv4(iface1Element.getAttribute("ip4"));
            }
            NodeList iface2NodeList = linkElement.getElementsByTagName("iface2");
            Node iface2Node = iface2NodeList.item(0);
            if (iface2Node != null) {
                Element iface2Element = (Element) iface2Node;
                node2.setIpv4(iface2Element.getAttribute("ip4"));
            }

            Links links1 = new Links(node1.getDeviceID(), node2);
            Links links2 = new Links(node2.getDeviceID(), node1); 
            underlay.addLinks(node1.getDeviceID(), links1);
            underlay.addLinks(node2.getDeviceID(), links2);
        }
        
        return underlay;
    }

    private void underlayToOverlay(AddressTable underlay) {
         for (Links node1 : underlay.getLinksList()) { // 1 [(2, 10.0.0.2), (3, null)]
            for (Link node2 : node1.getLinksList()) {  // [(2, 10.0.0.2), (3, null)]
                // CASO AMBOS SEJAM DEVICES
                if (this.devices.contains(node1.getDeviceID(), node2.getDeviceID())) { // 1 [(2, 10.0.0.2)]
                    this.table.addLinks(node1.getDeviceID(), new Links(node1.getDeviceID(), node2));
                }
                // CASO NODE1 NÃO SEJA DEVICE -->  null [(2, 10.0.0.2)]
                if (!this.devices.contains(node1.getDeviceID()) && this.devices.contains(node1.getDeviceID())) {
                    
                }
            }
        }   
    }

    public static void main(String[] args) {
        Overlay overlay = new Overlay("underlay1.xml");
        //overlay.getDevices().print();
        //System.out.println();
        //overlay.getTable().print();
    }
}
