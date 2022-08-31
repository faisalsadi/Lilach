package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class CatalogBoundaryController implements Initializable {
    @FXML private Text catalogL;

    @FXML private Button userName;
    @FXML private Button profileBtn;

    @FXML private Label cartLabel;
    @FXML private ListView<String> myCart;
    @FXML private Label priceLabel;
    @FXML private Button removeBtn;
    @FXML private Button clearBtn;
    @FXML private Button orderBtn;

    @FXML private ListView<String> myListView;

    @FXML private Button refreshBtn;

    @FXML private Label filterLabel;
    @FXML private VBox vb;
    @FXML private Label priceText;
    @FXML private Slider priceRange;
    @FXML private Label colorLabel;
    @FXML private CheckBox all;
    @FXML private CheckBox red;
    @FXML private CheckBox pink;
    @FXML private CheckBox blue;
    @FXML private CheckBox yellow;
    @FXML private Label typeLabel;
    @FXML private CheckBox all1;
    @FXML private CheckBox flowerA;
    @FXML private CheckBox bridalB;
    @FXML private CheckBox potOfFlowers;

    @FXML private Button searchBtn;
    @FXML private Button resetBtn;

    private static HashMap<String, Integer> colorsMap = new HashMap<>();
    private static HashMap<String, Integer> typeMap = new HashMap<>();
    private static String priceTxt;

    private static Label placeHolderCart = new Label("Add flowers to your cart");
    private static Label placeHolderList = new Label("No flowers in catalog");
    private int priceLimit = 1000;
    private double p;

    private static ArrayList<Flower> flowers = new ArrayList<Flower>();
    private static HashMap<Flower,Integer> cartMap = new HashMap<>();
    private static Flower currentFlower;

    private ArrayList<String> listStr = new ArrayList<String>();
    private ArrayList<String> cartNames = new ArrayList<String>();
    private String Email;

    public static void setFlowers(ArrayList<Flower> newFlowers) {
        if(CatalogBoundaryController.flowers.isEmpty()) {}
        else {
            CatalogBoundaryController.flowers.clear();
        }

        for(int i = 0; i < newFlowers.size(); i++) {
            if(newFlowers.get(i).getStatus() == 1) {
                CatalogBoundaryController.flowers.add(newFlowers.get(i));
            }
        }
    }

    public static Flower getCurrentFlower() {
        return currentFlower;
    }

    public static HashMap<Flower,Integer> getMap() {
        return cartMap;
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

        priceRange.setMin(0);
        priceRange.setMax(350);
        priceRange.setValue(350);
        priceRange.setBlockIncrement(10);
        priceRange.setShowTickMarks(false);
        priceRange.setShowTickLabels(true);

        all.setSelected(true);
        colorsMap.put("all", 1);
        colorsMap.put("red", 0);
        colorsMap.put("pink", 0);
        colorsMap.put("blue", 0);
        colorsMap.put("yellow", 0);

        all1.setSelected(true);
        typeMap.put("all", 1);
        typeMap.put("Flower Pot", 0);
        typeMap.put("Bridal Bouquet", 0);
        typeMap.put("Flower Arrangement", 0);

        placeHolderCart.setStyle("-fx-text-fill: linear-gradient(#ff5400, #be1d00);-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic");
        placeHolderList.setStyle("-fx-text-fill: linear-gradient(#ff5400, #be1d00);-fx-font-size: 14px;-fx-font-weight: bold;-fx-font-style: italic");
        myListView.setPlaceholder(placeHolderList);
        myCart.setPlaceholder(placeHolderCart);

        vb = new VBox();
        vb.setPadding(new Insets(20));
        vb.setSpacing(10);

        getCatalogItems();
        getCartItems();
        setRealPrice();
        priceTxt = "final price: " + p + " $";
        priceLabel.setText(priceTxt);

        priceRange.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                priceLimit = priceRange.valueProperty().intValue();
            }
        });

        removeBtn.disableProperty().bind(myCart.getSelectionModel().selectedItemProperty().isNull());
        clearBtn.disableProperty().bind(Bindings.isEmpty(myCart.getItems()));
        orderBtn.disableProperty().bind(Bindings.isEmpty(myCart.getItems()));
        if(userName.getText().equals("Register / Login")) {
            profileBtn.setDisable(true);
        }
    }

    public void setRealPrice() {
        p = 0;

        for (Flower key : cartMap.keySet()) {
            if(key.getSale() == true) {
                double dis = key.getDiscount() / 100.0;
                double finalD = 1 - dis;
                p += key.getPrice() * finalD * cartMap.get(key);
            }
            else {
                p += key.getPrice() * cartMap.get(key);
            }
        }
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
                refreshAfterDisconnect();
            }
        }
    }

    @FXML
    public void myProfile(ActionEvent event) throws IOException {
        App.setRoot("MyProfileBoundary");
    }

    public void getCartItems() {
        for (Flower key : cartMap.keySet()) {
            cartNames.add(key.getName() + " x" + cartMap.get(key));
        }
        myCart.getItems().addAll(cartNames);
    }

    public static void addToCart(Flower flower) {
        int counter;
        if(cartMap.get(flower) == null) {
            cartMap.put(flower, 1);
        }
        else {
            counter = cartMap.get(flower) + 1;
            cartMap.put(flower, counter);
        }
    }

    public void getCatalogItems() {
        for(int i = 0; i < flowers.size(); i++) {
            if(flowers.get(i).getPrice() <= priceLimit &&
               ((colorsMap.get(flowers.get(i).getColor()) == 1) || colorsMap.get("all") == 1) &&
               ((typeMap.get(flowers.get(i).getType()) == 1) || typeMap.get("all") == 1)) {
                listStr.add(flowers.get(i).getName());
            }
        }

        myListView.getItems().addAll(listStr);
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String currentId = myListView.getSelectionModel().getSelectedItem();
                for(int i = 0; i < flowers.size(); i++) {
                    if(flowers.get(i).getName().equals(currentId)) {
                        currentFlower = flowers.get(i);
                        break;
                    }
                }

                try {
                    App.setRoot("FlowerBoundary");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void clear(ActionEvent event) throws IOException {
        if(myCart.getItems().size() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Cart");
            alert.setHeaderText("Are you sure you want \nTo remove all items from your cart?");
            ButtonType confirmBtn = new ButtonType("Remove All");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirmBtn, cancelBtn);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirmBtn) {
                cartMap.clear();
                cartNames.clear();
                myCart.getItems().clear();
                priceTxt = "final price: 0 $";
                priceLabel.setText(priceTxt);
            }
        }
    }

    @FXML
    public void updateFilter(ActionEvent event) throws IOException {
        listStr.clear();
        myListView.getItems().clear();

        if(all.isSelected()) {
            colorsMap.put("all", 1);
        }
        else {
            colorsMap.put("all", 0);
        }
        if(red.isSelected()) {
            colorsMap.put("red", 1);
        }
        else {
            colorsMap.put("red", 0);
        }
        if(pink.isSelected()) {
            colorsMap.put("pink", 1);
        }
        else {
            colorsMap.put("pink", 0);
        }
        if(blue.isSelected()) {
            colorsMap.put("blue", 1);
        }
        else {
            colorsMap.put("blue", 0);
        }
        if(yellow.isSelected()) {
            colorsMap.put("yellow", 1);
        }
        else {
            colorsMap.put("yellow", 0);
        }

        if(all1.isSelected()) {
            typeMap.put("all", 1);
        }
        else {
            typeMap.put("all", 0);
        }
        if(flowerA.isSelected()) {
            typeMap.put("Flower Arrangement", 1);
        }
        else {
            typeMap.put("Flower Arrangement", 0);
        }
        if(bridalB.isSelected()) {
            typeMap.put("Bridal Bouquet", 1);
        }
        else {
            typeMap.put("Bridal Bouquet", 0);
        }
        if(potOfFlowers.isSelected()) {
            typeMap.put("Flower Pot", 1);
        }
        else {
            typeMap.put("Flower Pot", 0);
        }

        getCatalogItems();
    }

    @FXML
    public void resetFilter(ActionEvent event) throws IOException {
        priceLimit = 350;
        priceRange.setValue(350);
        listStr.clear();
        myListView.getItems().clear();

        all.setSelected(true);
        colorsMap.put("all", 1);
        colorsMap.put("red", 0);
        colorsMap.put("pink", 0);
        colorsMap.put("yellow", 0);
        colorsMap.put("blue", 0);

        all1.setSelected(true);
        typeMap.put("all", 1);
        typeMap.put("Flower Pot", 0);
        typeMap.put("Bridal Bouquet", 0);
        typeMap.put("Flower Arrangement", 0);

        red.setSelected(false);
        pink.setSelected(false);
        blue.setSelected(false);
        yellow.setSelected(false);

        flowerA.setSelected(false);
        bridalB.setSelected(false);
        potOfFlowers.setSelected(false);

        getCatalogItems();
    }

    @FXML
    public void goToOrder(ActionEvent event) throws IOException {
        if(userName.getText().equals("Register / Login")) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Message");
            a.setHeaderText("You must be logged in to your account");
            a.showAndWait();
        }
        else if(EntityHolder.getUser().getStatus() == 2) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Message");
            a.setHeaderText("Your account is blocked!");
            a.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            if(EntityHolder.getUser().getAccount().equals("Yearly Chain Account")) {
                if(p > 50) {
                    double finalP = p * 0.9;
                    String m = "Yearly Chain Account gets additional 10% discount\nYour new price is " + finalP;
                    alert.setTitle("Message");
                    alert.setHeaderText(m);
                    alert.showAndWait();
                }
            }

            alert.setTitle("Question");
            alert.setHeaderText("Choose delivery or pick up your order yourself\n(Delivery costs 10$)");
            ButtonType deliveryBtn = new ButtonType("Delivery");
            ButtonType pickUpBtn = new ButtonType("Pick Up");
            alert.getButtonTypes().setAll(deliveryBtn, pickUpBtn);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == deliveryBtn) {
                App.setRoot("OrderDeliveryBoundary");
            }
            else if(result.get() == pickUpBtn) {
                App.setRoot("OrderPickUpBoundary");
            }
        }
    }

    @FXML
    public void removeFunc(ActionEvent event) throws IOException {
        String item = myCart.getSelectionModel().getSelectedItem();
        String item2;

        for (Flower key : cartMap.keySet()) {
            if(item != null && item.startsWith(key.getName()) == true) {
                if(cartMap.get(key) > 1) {
                    cartNames.remove(key.getName());
                    item2 = key.getName() + " x" + String.valueOf(cartMap.get(key) - 1);
                    cartNames.add(item2);
                    cartMap.put(key, cartMap.get(key) - 1);
                    myCart.getItems().remove(item);
                    myCart.getItems().add(item2);
                    myCart.refresh();
                }
                else {
                    cartNames.remove(key.getName());
                    myCart.getItems().remove(item);
                    cartMap.remove(key);
                    myCart.refresh();
                }
                break;
            }
        }

        setRealPrice();
        priceTxt = "final price: " + p + " $";
        priceLabel.setText(priceTxt);
        myCart.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {}
        });
    }

    public void refreshAfterDisconnect() {
        cartMap.clear();
    }
}