
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Data
public class GuiCard {

    private Card card;
    private Image image;
    private Light.Distant light;
    private boolean selectable = true;

    GuiCard (){}

    GuiCard (Card card){
        this.card = card;
    }

    public ImageView getImageView(){
        if(image == null) {
            String prefix = card.getColor().toString().substring(0, 1).toLowerCase();
            String valueFormatted = String.format("%02d", card.getValue());
            String filename = prefix + valueFormatted + ".png";
            String path = Config.CARDS_FOLDER + "/" + Config.CLASSIC_THEME + "/" + Config.IMAGE_SIZE_FOLDER + "/";
            String fullFilename = path + filename;
            FileInputStream fileInputStream = Util.getFileInputStreamFromResources(getClass(), fullFilename);
            image = new Image(fileInputStream);
        }
        ImageView imageView = new ImageView(image);
        applyEffects(imageView);
        addActions(imageView);
//        imageView.setOnMouseEntered(image);
        return imageView;
    }

    public void setSelectable(boolean selectable){
        if(!selectable){
            disselectedEffect();
        }
        this.selectable = selectable;
    }

    private void addActions(ImageView imageView){
        imageView.setOnMouseEntered(ae -> {
            if(selectable) {
                selectEffect();
            }
        });
        imageView.setOnMouseExited(ae -> {
            disselectedEffect();
        });
    }

    private void selectEffect() {
        light.setColor(Color.web("#b4f6ff"));
    }

    private void disselectedEffect() {
        light.setColor(Color.WHITE);
    }

    private void applyEffects(ImageView imageView){
        light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.3);
        lighting.setSpecularConstant(0.8);
        lighting.setSpecularExponent(16.5);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(light);

        imageView.setEffect(lighting);
    }



}
