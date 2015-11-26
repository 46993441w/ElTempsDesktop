package ElTempsDesktop;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by alumne on 12/11/15.
 */
public class TempsCell extends ListCell<String> {
    @Override
    public void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText(null);
        } else if(item.equals("\t\tdia\t\t-\ttemps\t\t-\tMin/Max")){
            setText(item);
        } else {
            // inicialitzo el imageView
            ImageView imageView = new ImageView();
            //guardo el nom del icon
            String icon = getFileExt(item);
            //File file = new File("src/icons/"+icon+".png");
            // agafo la imatge de internet
            Image fxImage = new Image("http://openweathermap.org/img/w/"+icon+".png");
            // col·loco la imatge al imageView
            imageView.setImage(fxImage);
            setText(item.substring(4)); // es posa el text al listView
            setGraphic(imageView); // es posa la imatge al listView
        }
    }

    /**
     * Mètode diu quin es el icona que s'ha de mostrar
     * @param fname
     * @return
     */
    private static String getFileExt(String fname) {
        String ext = "";
        int p = fname.lastIndexOf('#');
        if (p >= 0) {
            ext = fname.substring(0,p);
        }
        return ext;
    }
}
