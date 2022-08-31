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

public class SingleComplaintEmployeeController implements Initializable {
    @FXML private Text complaintL;
    @FXML private Button userName;
    @FXML private Label orderNL;
    @FXML private TextField orderNumberTF;
    @FXML private Label userIDL;
    @FXML private TextField userIDTF;
    @FXML private Label messageL;
    @FXML private TextArea complaintTF;
    @FXML private Label chooseRl;
    @FXML private ComboBox<String> chooseRefund;
    @FXML private Button cancelBtn;
    @FXML private Button sendBtn;

    private static ComplaintHolder currentComplaint;
    private String Email;

    public static void setCurrentComplaint(ComplaintHolder complaint) {
        SingleComplaintEmployeeController.currentComplaint = complaint;
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

        orderNumberTF.setText(currentComplaint.getOrderID());
        userIDTF.setText(currentComplaint.getUserID());
        complaintTF.setText(currentComplaint.getContent());

        chooseRefund.getItems().addAll("0%", "50%", "100%");

        orderNumberTF.setEditable(false);
        userIDTF.setEditable(false);
        complaintTF.setEditable(false);

        checkDate();

        sendBtn.disableProperty().bind(chooseRefund.valueProperty().isNull());
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
    public void returnFunc(ActionEvent event) throws IOException {
        App.setRoot("ComplaintListEmployee");
    }

    @FXML
    public void sendFunc(ActionEvent event) throws IOException {
        double refundP = 0;
        String chosen = chooseRefund.getSelectionModel().getSelectedItem();
        // "0%", "50%", "100%"
        if(chosen.equals("0%")) {
            refundP = 0 * currentComplaint.getPrice();
        }
        else if(chosen.equals("50%")) {
            refundP = 0.50 * currentComplaint.getPrice();
        }
        else if(chosen.equals("100%")) {
            refundP = 1 * currentComplaint.getPrice();
        }

        int uID = Integer.parseInt(currentComplaint.getUserID());
        int oID = Integer.parseInt(currentComplaint.getOrderID());
        int cID = currentComplaint.getComplaintID();

        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#closeComplaint");
        arr.add(uID);
        arr.add(oID);
        arr.add(cID);
        arr.add(refundP);
        App.getClient().sendToServer(arr);
        moveOn();
    }

    public void moveOn() throws IOException {
        ComplaintListEmployeeController c = new ComplaintListEmployeeController();
        c.removeItem();
        App.setRoot("ComplaintListEmployee");
    }

    public void checkDate() {
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

        LocalDateTime complaintD = LocalDateTime.parse(currentComplaint.getDate(), formatter2);
        LocalDateTime plus24 = complaintD.plusDays(1);
        String now = getDate(LocalDateTime.now());
        LocalDateTime now1 = LocalDateTime.parse(now, formatter2);

        if(now1.isAfter(plus24)) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Message");
            a.setHeaderText("You are late in handling complaint\nRefund is 100$");
            a.showAndWait();
            chooseRefund.setValue("100%");
        }
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
}
