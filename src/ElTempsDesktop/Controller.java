package ElTempsDesktop;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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
    private Dialog dialog;
    private Dialog mitjas;
    private ObservableList<String> data = FXCollections.observableArrayList("\t\tdia\t\t-\ttemps\t\t-\tMin/Max");
    private String city = "Barcelona";
    private int units = 0;
    private Temps temp;


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
        llista.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String fname = llista.getSelectionModel().getSelectedItem();
                String icon = "";
                int p = fname.lastIndexOf('#');
                if (p >= 0) {
                    icon = fname.substring(0,p);
                }
                String[] temps = fname.split("-");
                createDialog(icon,temps[1].replace("\t",""));
                dialog.show();
            }
        });
    }

    private void createDialog(String icon, String temps) {
        // Definició d'un diàleg usant la classe Dialog
        dialog = new Dialog();
        dialog.setTitle("Diàleg");
        dialog.setHeaderText("This is a custom dialog");
        dialog.setResizable(true);
        Label label1 = new Label("Predicció: ");
        ImageView img1 = new ImageView("icons/" + icon + ".png");
        Label label2 = null;
        switch (temps){
            case "sky is clear":
                label2 = new Label("Sol");
                break;
            case "light rain":
                label2 = new Label("Lluvia");
                break;
        }
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(img1, 1, 2);
        grid.add(label2, 2, 2);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
    }

    public void listViewClick(Event event) {
        event.getSource();
        dialog.show();
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
            temp = new Temps();
            TempUnitari tempU = null;
            for(int i = 0; i < nl.getLength(); i++){
                tempU = new TempUnitari();
                Element temps = (Element) nl.item(i);
                String[] horari = temps.getAttribute("day").split("-");
                String diaVista = horari[2]+"/"+horari[1];
                String descripcio = temps.getFirstChild().getAttributes().getNamedItem("name").getTextContent();
                String icon = temps.getFirstChild().getAttributes().getNamedItem("var").getTextContent()+"#";
                Element temperatura = (Element)temps.getChildNodes().item(4);
                String tempMin = temperatura.getAttribute("min");
                String tempMax = temperatura.getAttribute("max");
                Element humitat = (Element)temps.getChildNodes().item(6);
                String humidity = humitat.getAttribute("value");
                Element presio = (Element)temps.getChildNodes().item(5);
                String pressure = presio.getAttribute("value");
                tempU.setTempsMin(Double.parseDouble(tempMin));
                tempU.setTempsMax(Double.parseDouble(tempMax));
                tempU.setHumidity(Integer.parseInt(humidity));
                tempU.setPressure(Double.parseDouble(pressure));
                temp.add(tempU);
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
        setData(city, units);
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

    public void calculMitjaTempsMin(ActionEvent actionEvent) {
        double min = temp.mitjaTempsMin();
        String unitats = (units == 0)? "ºC" : "ºF";
        setMitjas("Temperatura Mínima:",min,unitats);
        mitjas.show();
    }

    public void calculMitjaTempsMax(ActionEvent actionEvent) {
        double max = temp.mitjaTempsMax();
        String unitats = (units == 0)? "ºC" : "ºF";
        setMitjas("Temperatura Màxima:",max,unitats);
        mitjas.show();
    }

    public void calculMitjaHumidity(ActionEvent actionEvent) {
        double humitat = temp.mitjaHumidity();
        setMitjas("Humitat:",humitat,"%");
        mitjas.show();
    }

    public void calculMitjaPressure(ActionEvent actionEvent) {
        double pressio = temp.mitjaPressure();
        setMitjas("Pressió:",pressio,"hPa");
        mitjas.show();
    }

    public void setMitjas(String tipus, double result,String unitats){
        // Definició d'un diàleg usant la classe Dialog
        mitjas = new Dialog();
        mitjas.setTitle("Diàleg");
        mitjas.setHeaderText("Càlcul de la mitja de la "+tipus);
        mitjas.setResizable(true);
        Label label1 = new Label(String.format("%.2f", result) + " " + unitats);
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        mitjas.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        mitjas.getDialogPane().getButtonTypes().add(buttonTypeOk);
    }
}
