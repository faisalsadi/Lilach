package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginOrSignupBoundaryController implements Initializable {
    @FXML private Text welcome;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;
    @FXML private Button showCatalogBtn;

    // go to registration screen
    @FXML
    public void signupBtn(ActionEvent event) throws IOException {
        App.setRoot("RegistrationBoundary");
    }

    // go to login screen
    @FXML
    public void loginBtn(ActionEvent event) throws IOException {
        App.setRoot("LoginBoundary");
    }

    // go to catalog screen
    @FXML
    public void showCatalogBtn(ActionEvent event) throws IOException {
        EntityHolder.setTable(-1);
        App.setRoot("CatalogBoundary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
