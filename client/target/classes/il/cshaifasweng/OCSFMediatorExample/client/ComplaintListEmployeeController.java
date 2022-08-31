package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
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

public class ComplaintListEmployeeController implements Initializable {
    @FXML private Text complaintListL;
    @FXML private Button userName;

    @FXML private TableView<ComplaintHolder> tableView;
    @FXML private TableColumn<ComplaintHolder, String> userID;
    @FXML private TableColumn<ComplaintHolder, String> orderN;
    @FXML private TableColumn<ComplaintHolder, String> complaintDate;

    @FXML private Button returnBtn;
    @FXML private Button viewCBtn;

    private static ObservableList<ComplaintHolder> list;
    private int index;
    private String Email;

    public static void setComplaints(ObservableList<ComplaintHolder> complaints){
        ComplaintListEmployeeController.list = FXCollections.observableList(complaints);
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

        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        orderN.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        complaintDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableView.setItems(FXCollections.observableList(list)); // put values in the rows for each order

        viewCBtn.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
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
    void returnFunc(ActionEvent event) throws IOException {
        App.setRoot("CatalogEmployee");
    }

    @FXML
    public void clickItem(ActionEvent event) throws IOException {
        index = tableView.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            SingleComplaintEmployeeController.setCurrentComplaint(list.get(index));
            App.setRoot("SingleComplaintEmployee");
        }
    }

    public void removeItem() {
        list.remove(index);
    }
}
