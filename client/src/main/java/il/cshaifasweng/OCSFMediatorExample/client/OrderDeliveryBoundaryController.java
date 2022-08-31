package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderDeliveryBoundaryController implements Initializable {
    @FXML private Text orderL;
    @FXML private Button userName;
    @FXML private Label addCardL;
    @FXML private TextArea txt;
    @FXML private Button applyR;
    @FXML private Button cancelR;
    @FXML private Label timeL;
    @FXML private ComboBox<String> timeChoose;
    @FXML private Label pickupDateL;
    @FXML private DatePicker datePick;
    @FXML private Label pickupTimeL;
    @FXML private ComboBox<String> hour;
    @FXML private Label seperator;
    @FXML private ComboBox<String> minutes;
    @FXML private Label addressL;
    @FXML private TextField addressTF;
    @FXML private Label receiverNameL;
    @FXML private ComboBox<String> chooseName;
    @FXML private TextField receiverNameTF;
    @FXML private Label receiverEmail;
    @FXML private ComboBox<String> chooseEmail;
    @FXML private TextField receiverEmailTF;
    @FXML private Label paymentL;
    @FXML private ComboBox<String> str;
    @FXML private Label creditL;
    @FXML private TextField creditText;
    @FXML private Label labelValid;
    @FXML private ComboBox<String> chooseMonth;
    @FXML private Label slash;
    @FXML private ComboBox<String> chooseYear;
    @FXML private Label labelCVV;
    @FXML private TextField textCVV;
    @FXML private Label cartL;
    @FXML private ListView<String> list;
    @FXML private Label priceL;
    @FXML private Label finalPrice;
    @FXML private Button removeBtn;
    @FXML private Button confirm;
    @FXML private Button returnBtn;

    private static Label placeHolderCart = new Label("Cart is empty");
    private HashMap<Flower, Integer> map = CatalogBoundaryController.getMap();
    private ArrayList<String> cartList = new ArrayList<String>();
    private ArrayList<Flower> flowersList = new ArrayList<>();

    private double globalP = 0;
    private double p = 0;
    private double refund;
    private int refundFlag = 0;
    private String localDateTime;
    private boolean updateCreditCard = false;
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

        refund = EntityHolder.getUser().getRefund();
        cancelR.setDisable(true);
        if(refund == 0) {
            applyR.setDisable(true);
        }

        timeChoose.getItems().addAll("Supply immediately", "Pick Date & Time");
        LocalDate s = LocalDate.now();
        datePick.setValue(s);
        datePick.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        hour.getItems().addAll("00", "01", "02", "03", "04", "05", "06", "07",
                                    "08", "09", "10", "11", "12", "13", "14", "15",
                                    "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.getItems().addAll("00", "15", "30", "45");

        pickupDateL.setVisible(false);
        datePick.setVisible(false);
        pickupTimeL.setVisible(false);
        hour.setVisible(false);
        seperator.setVisible(false);
        minutes.setVisible(false);
        timeChoose.getSelectionModel().selectedItemProperty().addListener((option, oldV, newV) -> {
            if(newV.equals("Pick Date & Time")) {
                pickupDateL.setVisible(true);
                datePick.setVisible(true);
                pickupTimeL.setVisible(true);
                hour.setVisible(true);
                seperator.setVisible(true);
                minutes.setVisible(true);
            }
            else {
                pickupDateL.setVisible(false);
                datePick.setVisible(false);
                pickupTimeL.setVisible(false);
                hour.setVisible(false);
                seperator.setVisible(false);
                minutes.setVisible(false);
            }
        });

        chooseName.getItems().addAll("My Name", "New Name");
        chooseEmail.getItems().setAll("My Email", "New Email");
        receiverNameTF.setVisible(false);
        receiverEmailTF.setVisible(false);
        chooseName.getSelectionModel().selectedItemProperty().addListener((option, oldV, newV) -> {
            if(newV.equals("New Name")) {
                receiverNameTF.setVisible(true);
            }
            else {
                receiverNameTF.setVisible(false);
            }
        });
        chooseEmail.getSelectionModel().selectedItemProperty().addListener((option, oldV, newV) -> {
            if(newV.equals("New Email")) {
                receiverEmailTF.setVisible(true);
            }
            else {
                receiverEmailTF.setVisible(false);
            }
        });

        str.getItems().addAll("My Credit Card", "New Credit Card");
        chooseMonth.getItems().addAll("01", "02", "03", "04", "05", "06",
                                           "07", "08", "09", "10", "11", "12");
        chooseYear.getItems().addAll("22", "23", "24", "25", "26", "27",
                                          "28", "29", "30", "31", "32", "33");
        creditL.setVisible(false);
        creditText.setVisible(false);
        labelValid.setVisible(false);
        chooseMonth.setVisible(false);
        slash.setVisible(false);
        chooseYear.setVisible(false);
        labelCVV.setVisible(false);
        textCVV.setVisible(false);
        str.getSelectionModel().selectedItemProperty().addListener((option, oldV, newV) -> {
            if(newV.equals("New Credit Card")) {
                creditL.setVisible(true);
                creditText.setVisible(true);
                labelValid.setVisible(true);
                chooseMonth.setVisible(true);
                slash.setVisible(true);
                chooseYear.setVisible(true);
                labelCVV.setVisible(true);
                textCVV.setVisible(true);
            }
            else {
                creditL.setVisible(false);
                creditText.setVisible(false);
                labelValid.setVisible(false);
                chooseMonth.setVisible(false);
                slash.setVisible(false);
                chooseYear.setVisible(false);
                labelCVV.setVisible(false);
                textCVV.setVisible(false);
            }
        });

        getCartItems();
        placeHolderCart.setStyle("-fx-text-fill: linear-gradient(#ff5400, #be1d00);-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic");
        list.setPlaceholder(placeHolderCart);

        String priceTxt;
        setRealPrice();
        priceTxt = p + " $";
        finalPrice.setText(priceTxt);

        removeBtn.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
        confirm.disableProperty().bind(timeChoose.valueProperty().isNull()
                .or(Bindings.isEmpty(addressTF.textProperty()))
                .or(chooseName.valueProperty().isNull())
                .or(chooseEmail.valueProperty().isNull())
                .or(str.valueProperty().isNull()));
    }

    public void setRealPrice() {
        p = 0;
        globalP = 0;

        for (Flower key : map.keySet()) {
            if(key.getSale() == true) {
                double dis = key.getDiscount() / 100.0;
                double finalD = 1 - dis;
                p += key.getPrice() * finalD * map.get(key);
                globalP += key.getPrice() * finalD * map.get(key);
            }
            else {
                p += key.getPrice() * map.get(key);
                globalP += key.getPrice() * map.get(key);
            }
        }

        if(EntityHolder.getUser().getAccount().equals("Yearly Chain Account")) {
            if(p > 50) {
                p *= 0.9;
                globalP *= 0.9;
            }
        }

        // delivery cost
        p += 10;
        globalP += 10;
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

    public void getCartItems() {
        for (Flower key : map.keySet()) {
            cartList.add(key.getName() + " x" + map.get(key));
            for(int i = 0; i < map.get(key); i++) {
                flowersList.add(key);
            }
        }
        cartList.add("Delivery");
        list.getItems().addAll(cartList);
    }

    @FXML
    public void applyR(ActionEvent event) {
        refundFlag = 1;
        p -= refund;
        if(p >= 0) {
            EntityHolder.getUser().setRefund(0);
        }
        else if(p < 0) {
            p = 0;
            EntityHolder.getUser().setRefund(refund - globalP);
        }
        String priceTxt;
        priceTxt = p + " $";
        finalPrice.setText(priceTxt);
        applyR.setDisable(true);
        cancelR.setDisable(false);
    }

    @FXML
    public void cancelR(ActionEvent event) {
        refundFlag = 0;
        p = globalP;
        EntityHolder.getUser().setRefund(refund);
        String priceTxt;
        priceTxt = p + " $";
        finalPrice.setText(priceTxt);
        applyR.setDisable(false);
        cancelR.setDisable(true);
    }

    @FXML
    public void removeFunc(ActionEvent event) throws IOException {
        String item = list.getSelectionModel().getSelectedItem();
        String item2;

        for (Flower key : map.keySet()) {
            if(item != null && item.startsWith(key.getName()) == true) {
                if(map.get(key) > 1) {
                    cartList.remove(key.getName());
                    item2 = key.getName() + " x" + String.valueOf(map.get(key) - 1);
                    cartList.add(item2);
                    map.put(key, map.get(key) - 1);
                    list.getItems().remove(item);
                    list.getItems().add(item2);
                    list.refresh();
                }
                else {
                    cartList.remove(key.getName());
                    list.getItems().remove(item);
                    map.remove(key);
                    list.refresh();
                }
                break;
            }
        }

        setRealPrice();
        if(refund > 0) {
            moneyAfterRemove();
        }
        String pricetxt = p + " $";
        finalPrice.setText(pricetxt);
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {}
        });

        if(list.getItems().isEmpty()) {
            App.setRoot("CatalogBoundary");
        }
    }

    public void moneyAfterRemove() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("If refund was ever applied, it has now been removed.\nShould you wish to, apply again");
        a.showAndWait();
        refundFlag = 0;
        EntityHolder.getUser().setRefund(refund);
        applyR.setDisable(false);
        cancelR.setDisable(true);
    }

    @FXML
    public void confirmOrder(ActionEvent actionEvent) throws IOException {
        boolean[] answers = new boolean[7];
        int userId = EntityHolder.getID();
        String flowers = getItems();
        String card = checkCard();
        double finalP = p;
        int status = 1;
        int orderID = App.getOrderID();
        App.setOrderID(orderID + 1);
        String formOfS = "Delivery";
        String storeN = "";

        String address = "";
        answers[0] = checkAddress(addressTF.getText());
        if(answers[0] == true) {
            address = addressTF.getText();
        }

        String receiverN = "";
        String choice1 = chooseName.getSelectionModel().getSelectedItem();
        if(choice1.equals("My Name")) {
            receiverN = EntityHolder.getUser().getUserName();
            answers[1] = true;
        }
        else {
            answers[1] = checkName(receiverNameTF.getText());
            if(answers[1] == true) {
                receiverN = receiverNameTF.getText();
            }
        }

        String receiverE = "";
        String choice2 = chooseEmail.getSelectionModel().getSelectedItem();
        if(choice2.equals("My Email")) {
            receiverE = EntityHolder.getUser().getEmail();
            answers[2] = true;
        }
        else {
            answers[2] = checkEmail(receiverEmailTF.getText());
            if(answers[2] == true) {
                receiverE = receiverEmailTF.getText();
            }
        }

        String timeC = timeChoose.getSelectionModel().getSelectedItem();
        // for immediate supply
        if(timeC.equals("Supply immediately")) {
            answers[3] = true;
            localDateTime = getDate(LocalDateTime.now().plusHours(3));
        }
        // if we choose date + time
        else if(timeC.equals("Pick Date & Time")) {
            answers[3] = checkDateTime(LocalDateTime.now().plusHours(3));
        }

        String paymentMethod = str.getSelectionModel().getSelectedItem();
        String credit = "";
        String monthAndYear = "";
        String cvv = "";
        int flag = 0;

        if(paymentMethod.equals("My Credit Card")) {
            boolean tf = checkCurrentCard();
            // if our credit card is fine
            if(tf == true) {
                answers[4] = true;
                answers[5] = true;
                answers[6] = true;
                credit = EntityHolder.getUser().getCredit();
                monthAndYear = EntityHolder.getUser().getMonthAndYear();
                cvv = EntityHolder.getUser().getCvv();
                flag = 0;
            }
            // we need to change credit card
            else {
                answers[4] = false;
                answers[5] = false;
                answers[6] = false;
                flag = 1;
            }
        }
        // if we add new credit card
        else {
            credit = creditText.getText();
            cvv = textCVV.getText();
            String vMonth = chooseMonth.getSelectionModel().getSelectedItem();
            String vYear = chooseYear.getSelectionModel().getSelectedItem();
            answers[4] = checkCreditCard(credit);
            answers[5] = checkCVV(cvv);
            answers[6] = checkMonthYear(vMonth, vYear);
            if(answers[6] == true) {
                monthAndYear = vMonth + "/" + vYear;
            }
            flag = 0;
        }

        int counter = 0;
        for(int i = 0; i < 7; i++) {
            if(!answers[i]) {
                counter++;
            }
        }

        if(counter != 0 || flag == 1) {
            errorM(answers, flag);
        }
        else {
            if(refundFlag == 1) {
                updateRefund();
            }
            if(updateCreditCard == true) {
                EntityHolder.getUser().setCredit(credit);
                EntityHolder.getUser().setCvv(cvv);
                EntityHolder.getUser().setMonthAndYear(monthAndYear);
                updateCreditCardFunction(credit, cvv, monthAndYear);
            }
            addNewOrder(userId, flowers, card, formOfS, storeN, address, receiverN, receiverE,
                    localDateTime, finalP, credit, cvv, monthAndYear, status, orderID);
        }
    }

    @FXML
    public void returnFunc(ActionEvent actionEvent) throws IOException {
        App.setRoot("CatalogBoundary");
    }

    public String getItems() {
        String list = "";
        for(Flower key : flowersList) {
            list += key.getName() + "\n";
        }
        int index = list.lastIndexOf("\n");
        list = list.substring(0, index);
        return list;
    }

    public String checkCard() {
        String card = txt.getText();
        if(card == null) {
            card = "";
        }
        return card;
    }

    public boolean checkAddress(String a) {
        if(a.trim().isEmpty()) {
            return false;
        }

        char c;
        for(int i = 0; i < a.length(); i++) {
            c = a.charAt(i);
            if (Character.isLetter(c) || c == ' ' || (c >= '0' && c <= '9')) {
                continue;
            }
            else {
                return false;
            }
        }

        return true;
    }

    public boolean checkName(String name) {
        if(name == null) {
            return false;
        }
        if(name.trim().isEmpty()) {
            return false;
        }

        int length = name.length();
        char ch = ' ';
        for(int i = 0; i < length; i++) {
            ch = name.charAt(i);
            if (Character.isLetter(ch) || ch == ' ') {
                continue;
            }
            else {
                return false;
            }
        }

        return true;
    }

    public boolean checkEmail(String Email) {
        if(Email == null) {
            return false;
        }

        char a = '@';
        String begin = "";
        String end = "";

        int index = Email.indexOf(a);
        if(index == -1) {
            return false;
        }
        else {
            begin = Email.substring(0, index);
            if(begin  == null || begin.trim().isEmpty()) {
                return false;
            }
            end = Email.substring(index + 1, Email.length());
            if(end == null) {
                return false;
            }
        }

        if(!end.equals("gmail.com")) {
            return false;
        }

        for(int i = 0; i < begin.length(); i++) {
            a = begin.charAt(i);
            if (Character.isLetter(a) || (a >= '0' && a <= '9')) {
                continue;
            }
            else {
                return false;
            }
        }

        return true;
    }

    public boolean checkDateTime(LocalDateTime now) {
        LocalDate date = datePick.getValue();
        String hours = hour.getSelectionModel().getSelectedItem();
        String m = minutes.getSelectionModel().getSelectedItem();
        if(date == null || hours == null || m == null) {
            return false;
        }

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.parse(date.toString(), formatter1);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        String chosen = today.toString() + "-" + hours + "-" + m;
        LocalDateTime l = LocalDateTime.parse(chosen, formatter2);

        String gt = getDate(now);
        LocalDateTime now1 = LocalDateTime.parse(gt, formatter2);

        if(l.isAfter(now1)) {
            localDateTime = getDate(l);
            return true;
        }
        else {
            localDateTime = getDate(now1);
            return false;
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

    public boolean checkCurrentCard() {
        String monthAndYear = EntityHolder.getUser().getMonthAndYear();
        int index = monthAndYear.indexOf('/');
        String month = monthAndYear.substring(0, index);
        String year = monthAndYear.substring(index + 1, monthAndYear.length());
        boolean answer = checkMonthYear(month, year);
        return answer;
    }

    public boolean checkCreditCard(String creditCard) {
        if(creditCard == null) {
            return false;
        }

        int length = creditCard.length();
        int c = 0;
        if(length != 16) {
            return false;
        }
        else {
            for(int i = 0; i < length; i++) {
                c = creditCard.charAt(i) - 48;
                if(c < 0 || c > 9) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean checkCVV(String cvv) {
        if(cvv == null) {
            return false;
        }

        int length = cvv.length();
        int c = 0;
        if(length != 3) {
            return false;
        }
        else {
            for(int i = 0; i < length; i++) {
                c = cvv.charAt(i) - 48;
                if(c < 0 || c > 9) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean checkMonthYear(String month, String year) {
        if(month == null || year == null) {
            return false;
        }

        String monthAndYear = "20" + year + "-" + month + "-" + "01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.parse(LocalDate.now().toString(), formatter);
        LocalDate myDate = LocalDate.parse(monthAndYear, formatter);

        if(myDate.isBefore(today)) {
            return false;
        }
        else {
            return true;
        }
    }

    public void errorM(boolean[] answers, int flag) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");

        if(answers[0] != true) {
            a.setHeaderText("Write appropriate address");
            a.showAndWait();
        }
        else if(answers[1] != true) {
            a.setHeaderText("Name only contains letters and spaces");
            a.showAndWait();
        }
        else if(answers[2] != true) {
            a.setHeaderText("Email is incorrect\nForm of (letters, digits@gmail.com)");
            a.showAndWait();
        }
        else if(answers[3] != true) {
            a.setHeaderText("Choose appropriate date and time for supplying");
            a.showAndWait();
        }
        else if(flag != 0) {
            str.setValue("New Credit Card");
            creditL.setVisible(true);
            creditText.setVisible(true);
            labelValid.setVisible(true);
            chooseMonth.setVisible(true);
            slash.setVisible(true);
            chooseYear.setVisible(true);
            labelCVV.setVisible(true);
            textCVV.setVisible(true);
            updateCreditCard = true;
            a.setHeaderText("Your credit card isn't valid anymore\nAdd a different one");
            a.showAndWait();
        }
        else if(answers[4] != true) {
            a.setHeaderText("Credit card number is incorrect (16 digits only)");
            a.showAndWait();
        }
        else if(answers[5] != true) {
            a.setHeaderText("CVV number is incorrect (3 digits only)");
            a.showAndWait();
        }
        else if(answers[6] != true) {
            a.setHeaderText("Valid is incorrect (month and year must be valid)");
            a.showAndWait();
        }
    }

    public void updateRefund() throws IOException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("Your refund has been updated");
        a.showAndWait();

        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#updateDetails");
        arr.add(EntityHolder.getUser().getUserName());
        arr.add(EntityHolder.getUser().getId());
        arr.add(EntityHolder.getUser().getEmail());
        arr.add(EntityHolder.getUser().getPhone());
        arr.add(EntityHolder.getUser().getCredit());
        arr.add(EntityHolder.getUser().getMonthAndYear());
        arr.add(EntityHolder.getUser().getCvv());
        arr.add(EntityHolder.getUser().getPassword());
        arr.add(EntityHolder.getUser().getAccount());
        arr.add(EntityHolder.getUser().getStoreOrNull());
        arr.add(EntityHolder.getUser().getRefund());
        arr.add(EntityHolder.getUser().getStatus());
        arr.add(EntityHolder.getID());

        App.getClient().sendToServer(arr);
    }

    public void updateCreditCardFunction(String credit, String cvv, String monthAndYear) throws IOException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("Your credit card has been updated");
        a.showAndWait();

        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#updateDetails");
        arr.add(EntityHolder.getUser().getUserName());
        arr.add(EntityHolder.getUser().getId());
        arr.add(EntityHolder.getUser().getEmail());
        arr.add(EntityHolder.getUser().getPhone());
        arr.add(credit);
        arr.add(monthAndYear);
        arr.add(cvv);
        arr.add(EntityHolder.getUser().getPassword());
        arr.add(EntityHolder.getUser().getAccount());
        arr.add(EntityHolder.getUser().getStoreOrNull());
        arr.add(EntityHolder.getUser().getRefund());
        arr.add(EntityHolder.getUser().getStatus());
        arr.add(EntityHolder.getID());

        App.getClient().sendToServer(arr);
    }

    public void addNewOrder(int userID, String flowers, String card, String formOfSupplying,
                            String storeName, String address, String receiverName, String receiverEmail,
                            String dateTime, double finalPrice, String credit, String cvv,
                            String monthAndYear, int status, int orderID) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#addOrder");
        arr.add(userID);
        arr.add(flowers);
        arr.add(card);
        arr.add(formOfSupplying);
        arr.add(storeName);
        arr.add(address);
        arr.add(receiverName);
        arr.add(receiverEmail);
        arr.add(dateTime);
        arr.add(finalPrice);
        arr.add(credit);
        arr.add(cvv);
        arr.add(monthAndYear);
        arr.add(status);
        arr.add(orderID);
        if(p == 0) {
            arr.add(globalP);
        }
        else if(p > 0) {
            arr.add(refund);
        }
        arr.add(EntityHolder.getUser().getUserName());
        arr.add(EntityHolder.getUser().getEmail());

        App.getClient().sendToServer(arr);
        moveOn();
    }

    public void moveOn() throws IOException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("Your order has been complete");
        a.showAndWait();
        CatalogBoundaryController c = new CatalogBoundaryController();
        c.refreshAfterDisconnect();
        App.setRoot("MyProfileBoundary");
    }
}
