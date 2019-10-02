import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {


    @FXML
    Button btSubmit;

    @FXML
    public void initialize() {
        btSubmit.setOnAction(event -> {
            System.out.println("Button clicked");
        });
    }

}
