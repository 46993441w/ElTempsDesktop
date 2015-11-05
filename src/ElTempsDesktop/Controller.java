package ElTempsDesktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Controller {

    public ListView<String> llista;
    private ObservableList<String> data = FXCollections.observableArrayList();

    public void initialize(){
        setData();
        llista.setItems(data);
    }

    public void setData() {
        data.removeAll();
        File inputFile = new File("forecast.xml");
        //File outFile = new File("carrererBCN2.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputFile);
            //És important normalitzar
            doc.getDocumentElement().normalize();

            NodeList nl = doc.getElementsByTagName("time");

            for(int i = 0; i < nl.getLength(); i++){
                Element temps = (Element) nl.item(i);
                String[] horari = temps.getAttribute("day").split("-");
                String diaVista = horari[2]+"/"+horari[1];
                String descripcio = temps.getFirstChild().getAttributes().getNamedItem("name").getTextContent();
                Element temperatura = (Element)temps.getChildNodes().item(4);
                String tempMin = temperatura.getAttribute("min");
                String tempMax = temperatura.getAttribute("max");
                data.add(diaVista + " - " + descripcio + " - " + tempMin + "/" + tempMax);
                //System.out.println("Els carrers són: "+temps.getElementsByTagName("NOM_OFICIAL").item(0).getTextContent());

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.data = data;
    }
}
