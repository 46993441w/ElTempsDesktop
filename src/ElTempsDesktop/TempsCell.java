package ElTempsDesktop;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by alumne on 12/11/15.
 */
public class TempsCell extends ListCell<String> {
    static HashMap<String, Image> mapOfFileExtToSmallIcon = new HashMap<String, Image>();

    @Override
    public void updateItem(String item, boolean empty){
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText(null);
        } else if(item.equals("\t\tdia\t\t-\ttemps\t\t-\tMin/Max")){
            setText(item);
        } else {
            ImageView imageView = new ImageView();
            String icon = getFileExt(item);
            //File file = new File("src/icons/"+icon+".png");
            Image fxImage = new Image("http://openweathermap.org/img/w/"+icon+".png");
            imageView.setImage(fxImage);
            setText(item.substring(4));
            setGraphic(imageView);
        }
    }

    private static String getFileExt(String fname) {
        String ext = ".";
        int p = fname.lastIndexOf('#');
        if (p >= 0) {
            ext = fname.substring(0,p);
        }
        return ext.toLowerCase();
    }

    private static javax.swing.Icon getJSwingIconFromFileSystem(File file) {

        // Windows {
        FileSystemView view = FileSystemView.getFileSystemView();
        javax.swing.Icon icon = view.getSystemIcon(file);
        // }

        // OS X {
        //final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        //javax.swing.Icon icon = fc.getUI().getFileView(fc).getIcon(file);
        // }

        return icon;
    }

    private static Image getFileIcon(String fname) {
        final String ext = getFileExt(fname);

        Image fileIcon = mapOfFileExtToSmallIcon.get(ext);
        if (fileIcon == null) {

            javax.swing.Icon jswingIcon = null;

            File file = new File("src/icons/"+ext+".png");
            if (file.exists()) {
                jswingIcon = getJSwingIconFromFileSystem(file);
            }

            if (jswingIcon != null) {
                fileIcon = jswingIconToImage(jswingIcon);
                mapOfFileExtToSmallIcon.put(ext, fileIcon);
            }
        }

        return fileIcon;
    }

    private static Image jswingIconToImage(javax.swing.Icon jswingIcon) {
        BufferedImage bufferedImage = new BufferedImage(jswingIcon.getIconWidth(), jswingIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        jswingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
