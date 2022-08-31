package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.beans.binding.Bindings;
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

public class AddFlowerEmployeeController implements Initializable {
    @FXML private Text addFlowerL;
    @FXML private Button userName;
    @FXML private Label LableName;
    @FXML private TextField textName;
    @FXML private Label labelDescription;
    @FXML private TextField textDescription;
    @FXML private Label labelType;
    @FXML private ComboBox<String> chooseType;
    @FXML private Label labelImage;
    @FXML private ComboBox<String> chooseImage;
    @FXML private Label labelColor;
    @FXML private ComboBox<String> chooseColor;
    @FXML private Label labelPrice;
    @FXML private TextField textPrice;
    @FXML private Label labelSale;
    @FXML private ComboBox<String> ChooseSale;
    @FXML private Label labelDiscount;
    @FXML private TextField textDiscount;
    @FXML private Button backBtn;
    @FXML private Button signBtn;

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

        chooseType.getItems().addAll("Flower Pot", "Bridal Bouquet", "Flower Arrangement");
        chooseImage.getItems().addAll("Azalea.jpg", "beamingblush.jpg", "CallaLily.jpg", "DaffodilDoubleMix.jpg",
                                          "delphiniumcheerblue.jpg", "dreamypink.jpg", "freesiaFlowers.jpg",
                                          "homemadetreat.jpg", "ignited.jpg", "inmarcible.jpg", "IrisBouquet.jpg",
                                          "romanticstarryblue.jpg", "roseat.jpg", "smileyroses.jpg",
                                          "velvet.jpg", "wildcherry.jpg", "yellow.jpg", "DesertBluebells.jpg");
        chooseColor.getItems().addAll("pink", "red", "yellow", "blue");
        ChooseSale.getItems().addAll("True", "False");

        labelDiscount.setVisible(false);
        textDiscount.setVisible(false);
        ChooseSale.getSelectionModel().selectedItemProperty().addListener((option, oldV, newV) -> {
            if(newV.equals("True")) {
                labelDiscount.setVisible(true);
                textDiscount.setVisible(true);
            }
            else {
                labelDiscount.setVisible(false);
                textDiscount.setVisible(false);
            }
        });

        signBtn.disableProperty().bind(
                Bindings.isEmpty(textName.textProperty())
                        .or(Bindings.isEmpty(textDescription.textProperty()))
                        .or(chooseType.valueProperty().isNull())
                        .or(chooseImage.valueProperty().isNull())
                        .or(chooseColor.valueProperty().isNull())
                        .or(Bindings.isEmpty(textPrice.textProperty()))
                        .or(ChooseSale.valueProperty().isNull()));
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
    public void backButton(ActionEvent event) throws IOException {
        App.setRoot("CatalogEmployee");
    }

    @FXML
    public void signButton(ActionEvent event) throws IOException {
        String name = textName.getText();
        String description = textDescription.getText();
        String type = chooseType.getSelectionModel().getSelectedItem();
        String image = "Images/" + chooseImage.getSelectionModel().getSelectedItem();
        String color = chooseColor.getSelectionModel().getSelectedItem();
        String price = textPrice.getText();
        String chooseSale = ChooseSale.getSelectionModel().getSelectedItem();
        double discount = 0;
        boolean sale = false;

        // 0 = name, 1 = description, 2 = price, 3 = discount
        boolean[] answers = new boolean[4];
        answers[0] = checkName(name);
        answers[1] = checkDescription(description);
        answers[2] = checkPrice(price, 0);

        if(chooseSale.equals("True")) {
            sale = true;
            answers[3] = checkPrice(textDiscount.getText(), 1);
            if(answers[3] == true) {
                discount = Double.parseDouble(textDiscount.getText());
            }
        }
        else {
            answers[3] = true;
            sale = false;
            discount = 0;
        }

        int counter = 0;
        for(int i = 0; i < 4; i++) {
            if(!answers[i]) {
                counter++;
            }
        }

        if(counter != 0) {
            errorM(answers);
        }
        else {
            // adding data to database
            addNewFlower(name, description, type, image, color,
                        Double.parseDouble(price), sale, discount);
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

    public boolean checkDescription(String description) {
        if(description == null) {
            return false;
        }
        if(description.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean checkPrice(String price, int status) {
        if(price == null) {
            return false;
        }
        if(price.trim().isEmpty()) {
            return false;
        }

        char a = '.';
        int index = price.indexOf(a);
        // price at the shape of "150"
        if(index == -1) {
            for(int i = 0; i < price.length(); i++) {
                a = price.charAt(i);
                if (a >= '0' && a <= '9') {
                    continue;
                }
                else {
                    return false;
                }
            }
        }
        // price at the shape of "150.5"
        else {
            String begin = price.substring(0, index);
            if(begin  == null || begin.trim().isEmpty()) {
                return false;
            }
            for(int i = 0; i < begin.length(); i++) {
                a = begin.charAt(i);
                if (a >= '0' && a <= '9') {
                    continue;
                }
                else {
                    return false;
                }
            }
            String end = price.substring(index + 1, price.length());
            if(end == null) {
                return false;
            }
            for(int i = 0; i < end.length(); i++) {
                a = end.charAt(i);
                if (a >= '0' && a <= '9') {
                    continue;
                }
                else {
                    return false;
                }
            }
        }

        if(status == 1) {
            double temp = Double.parseDouble(price);
            if(temp >= 100) {
                return false;
            }
        }

        return true;
    }

    public void errorM(boolean[] answers) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");

        // 0 = name, 1 = description, 2 = price, 3 = discount
        if(answers[0] != true) {
            a.setHeaderText("Name is incorrect (only letters and space)");
            a.showAndWait();
        }
        else if(answers[1] != true) {
            a.setHeaderText("Description is missing");
            a.showAndWait();
        }
        else if(answers[2] != true) {
            a.setHeaderText("Price is incorrect\nForm of (digits and maybe decimal point)");
            a.showAndWait();
        }
        else if(answers[3] != true) {
            a.setHeaderText("Discount is incorrect\nForm of (digits and maybe decimal point) less than 100");
            a.showAndWait();
        }
    }

    public void addNewFlower(String name, String description, String type, String image,
                             String color, double price, boolean sale, double discount) throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#addFlower");
        arr.add(name);
        arr.add(description);
        arr.add(type);
        arr.add(image);
        arr.add(color);
        arr.add(price);
        arr.add(sale);
        arr.add(discount);

        App.getClient().sendToServer(arr);
        firstSettings();
        moveForward();
    }

    public void firstSettings() throws IOException {
        ArrayList<Object> arr = new ArrayList<>();
        arr.add("#getcatalog");
        App.getClient().sendToServer(arr);
    }

    public void moveForward() throws IOException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Message");
        a.setHeaderText("Flower has been added");
        a.showAndWait();
        App.setRoot("CatalogEmployee");
    }
}
