
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.Data;

import java.io.FileInputStream;

public class GuiCard {

    private Card card;
    private Image image;
    private Light.Distant light;
    private boolean selectable;
    private Lighting lighting;
    private ImageView imageView;
    private boolean greyedOut = false;

    GuiCard() {
    }

    GuiCard(Card card) {
        this.card = card;
    }

    public ImageView getImageView(boolean enableUserInteraction) {
        if (image == null) {
            String filename;
            if (card == null) {
                filename = "back.png";
            } else {
                String prefix = card.getColor().toString().substring(0, 1).toLowerCase();
                String valueFormatted = String.format("%02d", card.getValue());
                filename = prefix + valueFormatted + ".png";
            }
            String path = Config.CARDS_FOLDER + "/" + Config.CLASSIC_THEME + "/" + Config.IMAGE_SIZE_FOLDER + "/";
            String fullFilename = path + filename;
            FileInputStream fileInputStream = Util.getFileInputStreamFromResources(getClass(), fullFilename);
            image = new Image(fileInputStream);
        }
        imageView = new ImageView(image);
        prepareEffects();
        if (enableUserInteraction) {
            addMouseOverEffectToImageView();
        }
        return imageView;
    }

    public void setGreyedOut(boolean greyedOut) {
        this.greyedOut = greyedOut;
        if (this.greyedOut) {
            greyedOutEffect();
//            removeMouseOverActionFromImageView();
        } else {
            removeGreyedOutEffect();
        }
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        if (!selectable && image != null) {
            disselectedEffect();
        }
        this.selectable = selectable;
    }

    private void addMouseOverEffectToImageView() {
        imageView.setOnMouseEntered(ae -> {
            if (selectable && !greyedOut) {
                selectEffect();
            }
        });
        imageView.setOnMouseExited(ae -> {
            disselectedEffect();
        });
    }

    private void removeMouseOverActionFromImageView() {
        imageView.setOnMouseEntered(null);
        imageView.setOnMouseExited(null);
    }

    private void greyedOutEffect() {
        imageView.setEffect(lighting);
        light.setColor(Color.GRAY);
    }

    private void removeGreyedOutEffect() {
        if (imageView != null) {
            imageView.setEffect(null);
        }
    }

    private void selectEffect() {
        imageView.setEffect(lighting);
        light.setColor(Color.LIGHTBLUE);
    }

    private void disselectedEffect() {
        if (greyedOut) {
            greyedOutEffect();
        } else {
            imageView.setEffect(null);
        }
    }

    //TODO: Can be static
    private void prepareEffects() {
        light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        lighting = new Lighting();
        lighting.setDiffuseConstant(1.3);
        lighting.setSpecularConstant(0.8);
        lighting.setSpecularExponent(16.5);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(light);
    }


}
