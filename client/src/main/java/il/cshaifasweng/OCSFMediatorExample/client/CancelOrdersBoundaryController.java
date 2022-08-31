package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CancelOrdersBoundaryController implements Initializable {
    @FXML private Text cancellationL;
    @FXML private Button userName;
    @FXML private Label orderL;
    @FXML private TextField orderNumberTF;
    @FXML private Label sorryL;
    @FXML private TextArea message;
    @FXML private Button backBtn;
    @FXML private Button applyBtn;

    private double refundWeHaveNow = EntityHolder.getUser().getRefund();
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

        message.setEditable(false);
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
    void backOnClick(ActionEvent event) throws IOException {
        App.setRoot("MyProfileBoundary");
    }

    @FXML
    void applyOnClick(ActionEvent event) throws IOException {
        String orderNumber = orderNumberTF.getText();
        String today = "";

        if(orderNumber.trim().isEmpty()) {
            showMessage(1);
        }
        else if(!isValidOrderNum(orderNumber)) {
            showMessage(2);
        }
        else {
            today = getDate(LocalDateTime.now());
            // write to database
            ArrayList<Object> arr = new ArrayList<>();
            arr.add("#cancelorder");
            // user id
            arr.add(EntityHolder.getID());
            // order number
            int newON = Integer.parseInt(orderNumber);
            arr.add(newON);
            // the date of the cancellation
            arr.add(today);
            // status
            arr.add(3);
            arr.add(refundWeHaveNow);
            App.getClient().sendToServer(arr);
        }
    }

    public void showMessage(int i) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");

        if(i == 1) {
            a.setHeaderText("Fields cannot be empty or only with spaces");
            a.showAndWait();
        }
        else if(i == 2) {
            a.setHeaderText("Order number must only include numbers");
            a.showAndWait();
        }
    }

    // check that order number only has numbers
    private boolean isValidOrderNum(String fieldA) {
        if(fieldA == null) {
            return false;
        }

        for(int i = 0; i < fieldA.length(); i++) {
            char temp = fieldA.charAt(i);
            if(!(temp >= '0' && temp <= '9')) {
                return false;
            }
        }
        return true;
    }

    public String getDate(LocalDateTime now) {
        int[] getData = new int[5];
        getData[0] = now.getYear();
        getData[1] = now.getMonthValue();
        getData[2] = now.getDayOfMonth();
        getData[3] = now.getHour();
        getData[4] = now.getMinute();

        String[] newData = new String[5];
        for(int i = 0; i < 5; i++) {
            if(getData[i] < 10) {
                newData[i] = "0" + Integer.toString(getData[i]);
            }
            else {
                newData[i] = Integer.toString(getData[i]);
            }
        }

        String t = newData[0] + "-" + newData[1] + "-" + newData[2] + "-" + newData[3] + "-" + newData[4];
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime now1 = LocalDateTime.parse(t, formatter2);
        String now2 = now1.toString().replace("T", "-");
        now2 = now2.replace(":", "-");

        return now2;
    }

    public void nextStep() {
        try {
            moveOn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveOn() throws IOException {
        App.setRoot("MyProfileBoundary");
    }
}
