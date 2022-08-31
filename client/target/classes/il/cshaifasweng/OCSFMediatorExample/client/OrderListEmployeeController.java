package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class OrderListEmployeeController implements Initializable {
    @FXML private Text OrderListL;
    @FXML private Button userName;

    @FXML private TableView<OrderHolder> tableView;
    @FXML private TableColumn<OrderHolder, String> orderN;
    @FXML private TableColumn<OrderHolder, String> orderReceiver;
    @FXML private TableColumn<OrderHolder, String> orderDescription;
    @FXML private TableColumn<OrderHolder, String> orderDate;
    @FXML private TableColumn<OrderHolder, String> orderAddress;

    @FXML private Button returnBtn;
    @FXML private Button suppliedBtn;

    private static ObservableList<OrderHolder> list;

    private String Email;

    public static void setMyOrders(ObservableList<OrderHolder> orders) {
        OrderListEmployeeController.list = FXCollections.observableList(orders);
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
        orderReceiver.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
        orderDescription.setCellValueFactory(new PropertyValueFactory<>("flowers"));
        orderDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        orderAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableView.setItems(FXCollections.observableList(list)); // put values in the rows for each order

        suppliedBtn.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
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
        App.setRoot("CatalogEmployee");
    }

    @FXML
    public void supplyOrder(ActionEvent event) throws IOException {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            ArrayList<Object> msg = new ArrayList<>();
            msg.add("#supplyOrder");
            int orderNumber = Integer.parseInt(list.get(index).getOrderID());
            msg.add(orderNumber);
            App.getClient().sendToServer(msg);

            if(list.get(index).getReceiverName().equals(list.get(index).getName())) {
                message();
            }
            else {
                sendEmail(list.get(index).getEmail(), list.get(index).getOrderID());
            }

            tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
        }
    }

    public void message() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("Order has been supplied");
        a.showAndWait();
    }

    public void sendEmail(String email, String orderID) {
        final String username = "lilachservice@yahoo.com";
        final String password = "ogpmilcfzbcomkqx";
        String fromEmail = "lilachservice@yahoo.com";
        String toEmail = email;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(fromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            String subject = "About your order, number " + orderID;
            msg.setSubject(subject);
            msg.setText("Your order has been supplied");
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
