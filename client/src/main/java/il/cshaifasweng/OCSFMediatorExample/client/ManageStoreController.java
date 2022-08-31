package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageStoreController implements Initializable {
    @FXML private Text storeML;
    @FXML private Button userName;
    @FXML private Button incomeBtn;
    @FXML private Button ordersBtn;
    @FXML private Button complaintBtn;

    private String Email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(EntityHolder.getTable() == -1) {
            userName.setText("Register / Login");
        }
        else {
            if(EntityHolder.getTable() == 0) {
                userName.setText(EntityHolder.getUser().getUserName());
                Email = EntityHolder.getUser().getEmail();
            }
            else if(EntityHolder.getTable() == 1) {
                userName.setText(EntityHolder.getEmployee().getUserName());
                Email = EntityHolder.getEmployee().getEmail();
            }
            else if(EntityHolder.getTable() == 2) {
                userName.setText(EntityHolder.getStoreM().getUserName());
                Email = EntityHolder.getStoreM().getEmail();
            }
            else if(EntityHolder.getTable() == 3) {
                userName.setText(EntityHolder.getChainM().getName());
                Email = EntityHolder.getChainM().getEmail();
            }
        }
    }

    @FXML
    public void getDetails(ActionEvent event) throws IOException {
        if(userName.getText().equals("Register / Login")) {
            App.setRoot("LoginOrSignupBoundary");
        }
        else {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Message");
            a.setHeaderText("Do you wish to disconnect?");
            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK) {
                ArrayList<Object> arr = new ArrayList<>();
                arr.add("#disconnecting");
                arr.add(Email);
                App.getClient().sendToServer(arr);

                App.setRoot("LoginOrSignupBoundary");
                EntityHolder.setTable(-1);
                EntityHolder.setUser(null);
                EntityHolder.setEmployee(null);
                EntityHolder.setStoreM(null);
                EntityHolder.setChainM(null);
                EntityHolder.setID(-1);
                CatalogBoundaryController c = new CatalogBoundaryController();
                c.refreshAfterDisconnect();
            }
        }
    }

    @FXML
    public void compFunc(ActionEvent event) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#complaintsStore");
        arr.add(EntityHolder.getStoreM().getStoreName());
        App.getClient().sendToServer(arr);
        App.setRoot("ComplaintsReportsBoundary");
    }

    @FXML
    public void incomeFunc(ActionEvent event) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#incomeStore");
        arr.add(EntityHolder.getStoreM().getStoreName());
        App.getClient().sendToServer(arr);
        App.setRoot("IncomeReportsBoundary");
    }

    @FXML
    public void ordersFunc(ActionEvent event) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#orderStore");
        arr.add(EntityHolder.getStoreM().getStoreName());
        App.getClient().sendToServer(arr);
        App.setRoot("OrdersReportsBoundary");
    }
}
