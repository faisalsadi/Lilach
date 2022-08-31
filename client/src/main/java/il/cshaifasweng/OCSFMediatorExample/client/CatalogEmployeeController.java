package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CatalogEmployeeController implements Initializable {
    @FXML private Text employeesL;

    @FXML private Button userName;
    @FXML private Button addBtn;
    @FXML private Button removeBtn;
    @FXML private Button updateBtn;
    @FXML private Button orderListBtn;
    @FXML private Button complaintListBtn;

    @FXML private Label catalogL;
    @FXML private ListView<String> myListView;
    private String Email;

    private static ArrayList<Flower> flowers = new ArrayList<Flower>();
    private static Label placeHolderList = new Label("No flowers in catalog");
    private ArrayList<String> listStr = new ArrayList<String>();

    public static void setFlowers(ArrayList<Flower> flowers) {
        if(CatalogEmployeeController.flowers.isEmpty()) {}
        else {
            CatalogEmployeeController.flowers.clear();
        }
        CatalogEmployeeController.flowers = flowers;
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

        placeHolderList.setStyle("-fx-text-fill: linear-gradient(#ff5400, #be1d00);-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic");
        myListView.setPlaceholder(placeHolderList);

        removeBtn.disableProperty().bind(myListView.getSelectionModel().selectedItemProperty().isNull());
        updateBtn.disableProperty().bind(myListView.getSelectionModel().selectedItemProperty().isNull());
        getCatalogItems();
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

    public void getCatalogItems() {
        for(int i = 0; i < flowers.size(); i++ ) {
            listStr.add(flowers.get(i).getName());
        }
        myListView.getItems().addAll(listStr);
    }

    @FXML
    public void addFlower(ActionEvent event) throws IOException {
        App.setRoot("AddFlowerEmployee");
    }

    @FXML
    public void removeFlower(ActionEvent event) throws IOException {
        int index = myListView.getSelectionModel().getSelectedIndex();

        if(index > -1) {
            ArrayList<Object> msg = new ArrayList<>();
            msg.add("#deleteflower");
            msg.add(index);

            App.getClient().sendToServer(msg);
            firstSettings();
            message();
        }
    }

    public void firstSettings() throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#getcatalog");
        App.getClient().sendToServer(arr);
    }

    public void message() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("Flower has been removed from user's catalog");
        a.showAndWait();
    }

    @FXML
    public void updateFlower(ActionEvent event) throws IOException {
        int index = myListView.getSelectionModel().getSelectedIndex();

        if(index > -1) {
            ArrayList<Object> arr = new ArrayList<>();
            arr.add("#beforeUpdate");
            arr.add(index);
            App.getClient().sendToServer(arr);
        }
    }

    public void nextStep(int i) {
        if(i == 1) {
            try {
                moveToUpdate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(i == 2) {
            try {
                moveToOrderList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(i == 3) {
            try {
                moveToComplaintList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveToUpdate() throws IOException {
        App.setRoot("UpdateFlowerEmployee");
    }

    @FXML
    public void orderList(ActionEvent event) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#orderListE");

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("Which list would you like?");
        String[] b = {"Haifa", "Tel Aviv", "New York", "Eilat", "London", "Delivery"};
        ButtonType b1 = new ButtonType(b[0]);
        ButtonType b2 = new ButtonType(b[1]);
        ButtonType b3 = new ButtonType(b[2]);
        ButtonType b4 = new ButtonType(b[3]);
        ButtonType b5 = new ButtonType(b[4]);
        ButtonType b6 = new ButtonType(b[5]);
        a.getButtonTypes().setAll(b1, b2, b3, b4, b5, b6);
        Optional<ButtonType> result = a.showAndWait();

        if(result.get() == b1) {
            arr.add("Haifa");
            arr.add(1);
            App.getClient().sendToServer(arr);
        }
        else if(result.get() == b2) {
            arr.add("Tel Aviv");
            arr.add(2);
            App.getClient().sendToServer(arr);
        }
        else if(result.get() == b3) {
            arr.add("New York");
            arr.add(3);
            App.getClient().sendToServer(arr);
        }
        else if(result.get() == b4) {
            arr.add("Eilat");
            arr.add(4);
            App.getClient().sendToServer(arr);
        }
        else if(result.get() == b5) {
            arr.add("London");
            arr.add(5);
            App.getClient().sendToServer(arr);
        }
        else if(result.get() == b6) {
            arr.add("");
            arr.add(6);
            App.getClient().sendToServer(arr);
        }
    }

    public void moveToOrderList() throws IOException {
        App.setRoot("OrderListEmployee");
    }

    @FXML
    public void complaintList(ActionEvent event) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#ComplaintListE");
        App.getClient().sendToServer(arr);
    }

    public void moveToComplaintList() throws IOException {
        App.setRoot("ComplaintListEmployee");
    }
}
