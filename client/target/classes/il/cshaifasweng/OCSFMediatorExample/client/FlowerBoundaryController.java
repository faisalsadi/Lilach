package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FlowerBoundaryController implements Initializable {
    @FXML private ImageView flowerImg;
    @FXML private TextField txt;
    @FXML private TextField txt1;
    @FXML private TextField txt2;
    @FXML private TextField txt3;
    @FXML private TextField priceLabel;
    @FXML private TextField priceTxt;
    @FXML private TextField dollarLabel;
    @FXML private TextField descriptionTxt;
    @FXML private Button addToCartBtn;
    @FXML private Button returnBtn;

    Flower flower = new Flower();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flower = CatalogBoundaryController.getCurrentFlower();

        txt.setText(flower.getName());
        String sn = "Serial Number: " + flower.getSerialNumber();
        txt1.setText(sn);
        txt2.setText(flower.getType());
        String d = "Discount: " + flower.getDiscount() + "%";
        txt3.setText(d);

        if(flower.getSale() == true) {
            txt3.setVisible(true);
        }
        else {
            txt3.setVisible(false);
        }

        descriptionTxt.setText(flower.getDescription());
        priceTxt.setText((flower.getPrice()) + "");
        txt.setEditable(false);
        descriptionTxt.setEditable(false);

        priceTxt.setEditable(false);
        flowerImg.setImage(new Image(getClass().getResourceAsStream(flower.getImageurl())));
    }

    @FXML
    void returnBtn(ActionEvent event) throws IOException {
        try {
            App.setRoot("CatalogBoundary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addBtn(ActionEvent event) throws IOException {
        try {
            CatalogBoundaryController.addToCart(flower);
            if(flower.getSale() == true) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Message");
                a.setHeaderText("Discount has been applied");
                a.showAndWait();
            }
            App.setRoot("CatalogBoundary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}