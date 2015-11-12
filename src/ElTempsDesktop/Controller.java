package ElTempsDesktop;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Controller {

    public ListView<String> llista;
    public CheckMenuItem mts;
    public CheckMenuItem ipr;
    public CheckMenuItem mdd;
    public CheckMenuItem bcn;
    private ObservableList<String> data = FXCollections.observableArrayList("\t\tdia\t\t-\ttemps\t\t-\tMin/Max");
    private String city = "Barcelona";
    private int units = 0;

    /**
     * Mètode que s'inicia automaticament al començament
     */
    public void initialize() {
        setData(city, units);
        llista.setItems(data);
        llista.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> llista) {
                return new TempsCell();
            }
        });
    }

    /**
     * Mètode que canvia la llista que es mostra per pantalla
     */
    public void setData(String city,int units) {
        data.remove(1, data.size());
        File inputFile = new File(city+units+".xml");
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
                String icon = temps.getFirstChild().getAttributes().getNamedItem("var").getTextContent()+"#";
                Element temperatura = (Element)temps.getChildNodes().item(4);
                String tempMin = temperatura.getAttribute("min");
                String tempMax = temperatura.getAttribute("max");
                // afegir el String al observableList per a que el mostri el listView
                data.add(icon + diaVista + "\t-\t" + descripcio + "\t-\t" + tempMin + "/" + tempMax);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mètode que tanca el programa
     * @param actionEvent
     */
    public void sortir(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Mètode que canvia la ciutat i mostra les seves dades
     * @param actionEvent
     */
    public void changeCity(ActionEvent actionEvent) {
        CheckMenuItem men = (CheckMenuItem) actionEvent.getSource();
        switch (men.getId()){
            case "bcn":
                city = "Barcelona";
                mdd.setSelected(false);
                break;
            case "mdd":
                city = "Madrid";
                bcn.setSelected(false);
                break;
        }
        setData(city,units);
    }

    /**
     * Mètode que canvia les unitats i mostra les seves dades
     * @param actionEvent
     */
    public void changeUnits(ActionEvent actionEvent) {
        CheckMenuItem men = (CheckMenuItem) actionEvent.getSource();
        switch (men.getId()){
            case "mts":
                units = 0;
                ipr.setSelected(false);
                break;
            case "ipr":
                units = 1;
                mts.setSelected(false);
                break;
        }
        setData(city,units);
    }
}
