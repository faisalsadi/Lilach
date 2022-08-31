package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.ObservableList;
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

public class MyProfileBoundaryController implements Initializable {
    @FXML private Text profileL;

    @FXML private Button userName;
    @FXML private Button personalD;
    @FXML private Button myOrdersB;
    @FXML private Button cancelBtn;
    @FXML private Button complaintBtn;
    @FXML private Button refundBtn;
    @FXML private Button returnBtn;

    private ObservableList<OrderHolder> orders;

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
    public void personalDetails(ActionEvent event) throws IOException {
        App.setRoot("PersonalDetailsBoundary");
    }

    @FXML
    public void myOrders(ActionEvent event) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#getmyorders");
        arr.add(EntityHolder.getID());
        App.getClient().sendToServer(arr);
    }

    public void nextStep() {
        try {
            moveForward();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveForward() throws IOException {
        App.setRoot("MyOrdersBoundary");
    }

    @FXML
    public void cancelOrder(ActionEvent event) throws IOException {
        App.setRoot("CancelOrdersBoundary");
    }

    @FXML
    public void fileComplaints(ActionEvent event) throws IOException {
        App.setRoot("FileComplaintBoundary");
    }

    @FXML
    public void refundHistory(ActionEvent event) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#getmyrefunds");
        arr.add(EntityHolder.getID());
        App.getClient().sendToServer(arr);
    }

    public void nextStep1() {
        try {
            moveForward1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveForward1() throws IOException {
        App.setRoot("RefundHistoryBoundary");
    }

    @FXML
    public void returnToCatalog(ActionEvent event) throws IOException {
        App.setRoot("CatalogBoundary");
    }
}
