package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegistrationBoundaryController implements Initializable {
    @FXML private Text Registration;

    @FXML private Label LableName;
    @FXML private Label labelID;
    @FXML private Label labelEmail;
    @FXML private Label labelPhone;
    @FXML private Label labelCredit;
    @FXML private Label labelValid;
    @FXML private Label labelCVV;
    @FXML private Label labelPassword;

    @FXML private TextField textName;
    @FXML private TextField textID;
    @FXML private TextField textEmail;
    @FXML private TextField textPhone;
    @FXML private TextField textCredit;
    @FXML private ComboBox<String> chooseMonth;
    @FXML private Label slash;
    @FXML private ComboBox<String> chooseYear;
    @FXML private TextField textCVV;
    @FXML private TextField textPassword;

    @FXML private Button backBtn;
    @FXML private Button signBtn;

    @FXML private Label labelSubscription;
    @FXML private ComboBox<String> chooseMembership;
    @FXML private Label labelStore;
    @FXML private ComboBox<String> chooseStore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelStore.setVisible(false);
        chooseStore.setVisible(false);
        signBtn.disableProperty().bind(
                Bindings.isEmpty(textName.textProperty())
                        .or(Bindings.isEmpty(textID.textProperty()))
                        .or(Bindings.isEmpty(textEmail.textProperty()))
                        .or(Bindings.isEmpty(textPhone.textProperty()))
                        .or(Bindings.isEmpty(textCredit.textProperty()))
                        .or(Bindings.isEmpty(textCVV.textProperty()))
                        .or(Bindings.isEmpty(textPassword.textProperty()))
                        .or(chooseMonth.valueProperty().isNull())
                        .or(chooseYear.valueProperty().isNull())
                        .or(chooseMembership.valueProperty().isNull()));

        chooseMonth.getItems().addAll("01", "02", "03", "04", "05", "06",
                                           "07", "08", "09", "10", "11", "12");
        chooseYear.getItems().addAll("22", "23", "24", "25", "26", "27",
                                          "28", "29", "30", "31", "32", "33");
        chooseMembership.getItems().addAll("Store Account", "Chain Account", "Yearly Chain Account");
        chooseStore.getItems().addAll("Haifa", "Tel Aviv", "New York", "Eilat", "London");

        chooseMembership.getSelectionModel().selectedItemProperty().addListener((option, oldV, newV) -> {
            if(newV.equals("Store Account")) {
                labelStore.setVisible(true);
                chooseStore.setVisible(true);
            }
            else {
                labelStore.setVisible(false);
                chooseStore.setVisible(false);
            }
            if(newV.equals("Yearly Chain Account")) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Message");
                a.setHeaderText("A 100$ would be charged on your credit card due to yearly chain account!");
                a.showAndWait();
            }
        });
    }

    @FXML
    public void backButton(ActionEvent event) throws IOException {
        App.setRoot("LoginOrSignupBoundary");
    }

    @FXML
    public void signButton(ActionEvent event) throws IOException {
        String firstName = textName.getText();
        String ID = textID.getText();
        String email = textEmail.getText();
        String phoneN = textPhone.getText();
        String creditCard = textCredit.getText();
        String cvv = textCVV.getText();
        String password = textPassword.getText();
        String vMonth = chooseMonth.getSelectionModel().getSelectedItem();
        String vYear = chooseYear.getSelectionModel().getSelectedItem();
        String account = chooseMembership.getSelectionModel().getSelectedItem();
        String store = chooseStore.getSelectionModel().getSelectedItem();

        int flag = 0;
        if(account.equals("Store Account")) {
            if(store == null) {
                flag = 1;
            }
        }
        else {
            store = null;
            flag = 0;
        }

        // 0 = name, 1 = ID, 2 = email, 3 = phone, 4 = credit, 5 = cvv, 6 = password, 7 = month and year
        boolean[] answers = new boolean[8];
        answers[0] = checkName(firstName);
        answers[1] = checkID(ID);
        answers[2] = checkEmail(email);
        answers[3] = checkPhoneNumber(phoneN);
        answers[4] = checkCreditCard(creditCard);
        answers[5] = checkCVV(cvv);
        answers[6] = checkPassword(password);
        answers[7] = checkMonthYear(vMonth, vYear);

        int counter = 0;
        for(int i = 0; i < 8; i++) {
            if(!answers[i]) {
                counter++;
            }
        }
        if(counter != 0 || flag == 1) {
            errorM(answers, flag);
        }
        else {
            // adding data to database
            addNewUser(firstName, ID, email, phoneN, creditCard,
                        vMonth, vYear, cvv, password, account, store);
        }
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

    public boolean checkID(String ID) {
        if(ID == null) {
            return false;
        }

        int length = ID.length();
        int c = 0;
        if(length != 9) {
            return false;
        }
        else {
            for(int i = 0; i < length; i++) {
                c = ID.charAt(i) - 48;
                if(c < 0 || c > 9) {
                    return false;
                }
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

    public boolean checkPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            return false;
        }

        int length = phoneNumber.length();
        int c = 0;
        if(length != 10) {
            return false;
        }
        else {
            for(int i = 0; i < length; i++) {
                c = phoneNumber.charAt(i) - 48;
                if(c < 0 || c > 9) {
                    return false;
                }
            }
        }

        return true;
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

    public boolean checkPassword(String password) {
        if(password == null) {
            return false;
        }

        int length = password.length();
        char a = ' ';
        if(length < 8) {
            return false;
        }
        else {
            for(int i = 0; i < length; i++) {
                a = password.charAt(i);
                if (Character.isLetter(a) || (a >= '0' && a <= '9')) {
                    continue;
                }
                else {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean checkMonthYear(String month, String year) {
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

    public void addNewUser(String name, String id, String email, String phone, String credit, String month,
                           String year, String cvv, String password, String account, String storeOrNull) throws IOException {
        String monthAndYear = month + "/" + year;
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#register");
        arr.add(name);
        arr.add(id);
        arr.add(email);
        arr.add(phone);
        arr.add(credit);
        arr.add(monthAndYear);
        arr.add(cvv);
        arr.add(password);
        arr.add(account);
        if(storeOrNull == null) {
            arr.add("");
        }
        else {
            arr.add(storeOrNull);
        }
        double refund = 0;
        int status = 1;
        arr.add(refund);
        arr.add(status);

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
        App.setRoot("CatalogBoundary");
    }

    public void errorM(boolean[] answers, int flag) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");

        // 0 = name, 1 = ID, 2 = email, 3 = phone, 4 = credit, 5 = cvv, 6 = password, 7 = month and year
        if(answers[0] != true) {
            a.setHeaderText("Name is incorrect (only letters and space)");
            a.showAndWait();
        }
        else if(answers[1] != true) {
            a.setHeaderText("ID is incorrect (9 digits only)");
            a.showAndWait();
        }
        else if(answers[2] != true) {
            a.setHeaderText("Email is incorrect\nForm of (letters, digits@gmail.com)");
            a.showAndWait();
        }
        else if(answers[3] != true) {
            a.setHeaderText("Phone number is incorrect (10 digits only)");
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
            a.setHeaderText("Password is incorrect (at least 8 letters and digits)");
            a.showAndWait();
        }
        else if(answers[7] != true) {
            a.setHeaderText("Valid is incorrect (month and year must be valid)");
            a.showAndWait();
        }
        else if(flag != 0) {
            a.setHeaderText("Store is missing. Choose one or change account");
            a.showAndWait();
        }
    }
}
