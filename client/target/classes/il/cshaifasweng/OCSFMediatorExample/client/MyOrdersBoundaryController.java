package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyOrdersBoundaryController implements Initializable {
    @FXML private Text myOrdersL;
    @FXML private Button userName;

    @FXML private TableView<OrderHolder> tableView;
    @FXML private TableColumn<OrderHolder, String> orderN;
    @FXML private TableColumn<OrderHolder, String> orderDate;
    @FXML private TableColumn<OrderHolder, String> orderP;
    @FXML private TableColumn<OrderHolder, String> orderD;
    @FXML private TableColumn<OrderHolder, String> orderS;

    @FXML private Button returnBtn;

    private static ObservableList<OrderHolder> list;

    private String Email;

    public static void setMyOrders(ObservableList<OrderHolder> orders) {
        MyOrdersBoundaryController.list = FXCollections.observableList(orders);
    }

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

        orderN.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        orderDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        orderP.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));
        orderD.setCellValueFactory(new PropertyValueFactory<>("flowers"));
        orderS.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableView.setItems(FXCollections.observableList(list)); // put values in the rows for each order
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
    public void backBtn(ActionEvent event) throws IOException {
        App.setRoot("MyProfileBoundary");
    }
}
