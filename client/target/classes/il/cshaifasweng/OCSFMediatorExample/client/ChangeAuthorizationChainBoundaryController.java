package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.StoreManager;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
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

public class ChangeAuthorizationChainBoundaryController implements Initializable {
    @FXML private Text title;
    @FXML private Button userName;
    @FXML private Button returnBtn;
    @FXML private Label userL;
    @FXML private TableView<User> tableUser;
    @FXML private TableColumn<User, String> name1;
    @FXML private TableColumn<User, String> status1;
    @FXML private Button changeU;
    @FXML private Label employeeL;
    @FXML private TableView<Employee> tableEmployee;
    @FXML private TableColumn<Employee, String> name2;
    @FXML private TableColumn<Employee, String> status2;
    @FXML private Button changeEmployee;
    @FXML private Label managerL;
    @FXML private TableView<StoreManager> tableManager;
    @FXML private TableColumn<StoreManager, String> name3;
    @FXML private TableColumn<StoreManager, String> status3;
    @FXML private Button changeManager;

    private static ObservableList<User> list1;
    private static ObservableList<Employee> list2;
    private static ObservableList<StoreManager> list3;

    private String Email;

    public static void testAll(ArrayList<User> list1, ArrayList<Employee> list2, ArrayList<StoreManager> list3) {
        ChangeAuthorizationChainBoundaryController.list1 = FXCollections.observableArrayList(list1);
        ChangeAuthorizationChainBoundaryController.list2 = FXCollections.observableArrayList(list2);
        ChangeAuthorizationChainBoundaryController.list3 = FXCollections.observableArrayList(list3);
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

        name1.setCellValueFactory(new PropertyValueFactory<>("userName"));
        status1.setCellValueFactory(new PropertyValueFactory<>("statusForTable"));
        name2.setCellValueFactory(new PropertyValueFactory<>("userName"));
        status2.setCellValueFactory(new PropertyValueFactory<>("statusForTable"));
        name3.setCellValueFactory(new PropertyValueFactory<>("userName"));
        status3.setCellValueFactory(new PropertyValueFactory<>("statusForTable"));

        tableUser.setItems(FXCollections.observableList(list1));
        tableEmployee.setItems(FXCollections.observableList(list2));
        tableManager.setItems(FXCollections.observableList(list3));

        changeU.disableProperty().bind(tableUser.getSelectionModel().selectedItemProperty().isNull());
        changeEmployee.disableProperty().bind(tableEmployee.getSelectionModel().selectedItemProperty().isNull());
        changeManager.disableProperty().bind(tableManager.getSelectionModel().selectedItemProperty().isNull());
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
    public void returnBtn(ActionEvent event) throws IOException {
        App.setRoot("ManageChain");
    }

    @FXML
    public void changeU(ActionEvent event) throws IOException {
        int index = tableUser.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            ArrayList<Object> msg = new ArrayList<>();
            msg.add("#changeU");
            msg.add(index);
            if(list1.get(index).getStatus() == 1) {
                msg.add(2);
            }
            else {
                msg.add(1);
            }
            App.getClient().sendToServer(msg);
            App.setRoot("ManageChain");
        }
    }

    @FXML
    public void changeE(ActionEvent event) throws IOException {
        int index = tableEmployee.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            ArrayList<Object> msg = new ArrayList<>();
            msg.add("#changeE");
            msg.add(index);
            if(list2.get(index).getStatus() == 1) {
                msg.add(2);
            }
            else {
                msg.add(1);
            }
            App.getClient().sendToServer(msg);
            App.setRoot("ManageChain");
        }
    }

    @FXML
    public void changeM(ActionEvent event) throws IOException {
        int index = tableManager.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            ArrayList<Object> msg = new ArrayList<>();
            msg.add("#changeM");
            msg.add(index);
            if(list3.get(index).getStatus() == 1) {
                msg.add(2);
            }
            else {
                msg.add(1);
            }
            App.getClient().sendToServer(msg);
            App.setRoot("ManageChain");
        }
    }
}
