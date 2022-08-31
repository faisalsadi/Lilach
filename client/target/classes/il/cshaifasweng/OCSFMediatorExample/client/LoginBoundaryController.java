package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginBoundaryController implements Initializable {
    @FXML private Text signin;
    @FXML private Label labelE;
    @FXML private Label labelP;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private Button loginBtn;
    @FXML private Button backBtn;
    @FXML private Label labelC;
    @FXML private ComboBox<String> options;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        options.getItems().addAll("User", "Employee", "Store Manager", "Chain Manager");
        loginBtn.disableProperty().bind(
               Bindings.isEmpty(email.textProperty())
                       .or(Bindings.isEmpty(password.textProperty()))
                       .or(options.valueProperty().isNull()));
    }

    @FXML
    public void onClick(ActionEvent event) throws IOException {
        // user put username and password that are not only spaces or null
        String un = email.getText();
        String up = password.getText();
        String type = options.getSelectionModel().getSelectedItem();

        if(un.trim().isEmpty() || up.trim().isEmpty()) {
            showMessage();
        }
        else {
            // write to database
            ArrayList<Object> arr = new ArrayList<>();
            arr.add("#loginUser");
            arr.add(type);
            arr.add(un);
            arr.add(up);
            App.getClient().sendToServer(arr);
        }
    }

    @FXML
    public void backButton(ActionEvent event) throws IOException {
        App.setRoot("LoginOrSignupBoundary");
    }

    public void showMessage() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("One or more details are wrong.\nPlease fill again");
        a.showAndWait();
    }

    public void nextStep(int i) {
        if(i == 2) {
            try {
                moveForwardUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(i == 3) {
            try {
                moveForwardEmployee();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(i == 4) {
            try {
                moveForwardStoreM();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(i == 5) {
            try {
                moveForwardChainM();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveForwardUser() throws IOException {
        App.setRoot("CatalogBoundary");
    }

    public void moveForwardEmployee() throws IOException {
        App.setRoot("CatalogEmployee");
    }

    public void moveForwardStoreM() throws IOException {
        App.setRoot("ManageStore");
    }

    public void moveForwardChainM() throws IOException {
        App.setRoot("ManageChain");
    }
}
