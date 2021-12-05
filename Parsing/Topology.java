import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;


public class Topology {
    private Devices devices;
    private AddressTable underlay;
    private AddressTable overlay;


    // --- CONSTRUCTORS ---
    public Topology() {
        this.devices = new Devices();
        this.overlay = new AddressTable();
    }

    public Topology(String file) {
        this.devices  = new Devices();
        this.overlay  = new AddressTable();
        this.underlay = new AddressTable();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
        
            Document document = builder.parse(new File( file ));
            document.getDocumentElement().normalize();

            underlayDevices(document);
            underlayLinks(document);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Overlay Parsing NOT OK");
        }
    }



    // --- GETTERS ---
    public Devices getDevices() {
        return this.devices;
    }

    public AddressTable getOverlay() {
        return this.overlay;
    }

    public AddressTable getUnderlay() {
        return this.underlay;
    }


    // --- SETTERS ---
    public void setDevices(Devices devices) {
        this.devices = devices;
    }

    public void setOverlay(AddressTable overlay) {
        this.overlay = overlay;
    }

    public void setUnderlay(AddressTable underlay) {
        this.underlay = underlay;
    }


    // --- PARSING (ALL PRIVATE) ---
    private void underlayDevices (Document document) {
        NodeList deviceNodeList = document.getElementsByTagName("device");
        for (int i = 0; i < deviceNodeList.getLength(); i++) {
            Node deviceNode = deviceNodeList.item(i);
            Element deviceElement = (Element) deviceNode;
            Device device = new Device();
            device.setId(Integer.parseInt( deviceElement.getAttribute("id") ));
            device.setName( deviceElement.getAttribute("name") );
            this.devices.addDevice(device);
        }
    }

    private void underlayLinks(Document document) {
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
            this.underlay.addLinks(node1.getID(), links1);
            this.underlay.addLinks(node2.getID(), links2);
        }
    }




    public static void main(String[] args) {
        Topology topologia = new Topology("underlay1.xml");
        topologia.getDevices().print();
        System.out.println("----------------------------");
        topologia.getUnderlay().print();
        //System.out.println();
        //overlay.getTable().print();
    }

}
