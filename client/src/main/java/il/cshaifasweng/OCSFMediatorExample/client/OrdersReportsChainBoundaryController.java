package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrdersReportsChainBoundaryController implements Initializable {
    @FXML private Text incomeTxt;
    @FXML private Button userName;
    @FXML private Button returnBtn;
    @FXML private Label fromL;
    @FXML private DatePicker startDate1;
    @FXML private DatePicker startDate2;
    @FXML private Label toL;
    @FXML private DatePicker endDate1;
    @FXML private DatePicker endDate2;
    @FXML private Label priceLabel;
    @FXML private Label yearIncome1;
    @FXML private Label priceLabel2;
    @FXML private Label yearFlower1;
    @FXML private Label ordersLabel2;
    @FXML private Label yearIncome2;
    @FXML private Label flowersLabel2;
    @FXML private Label yearFlower2;
    @FXML private Label chooseL;
    @FXML private ComboBox<String> storeCB;
    @FXML private Button generateBtn;
    @FXML private Button compareBtn;
    @FXML private PieChart ordersChart1;
    @FXML private PieChart ordersChart2;

    private static ArrayList<Order> orderslst = new ArrayList<>();
    private static ArrayList<Flower> flowerlst = new ArrayList<>();
    private int numOrders1 = 0;
    private int numOrders2 = 0;
    private String currentStore = "All";
    private String Email;

    private HashMap<String,Integer> flowerMap1 = new HashMap<>();
    private HashMap<String,Integer> flowerMap2 = new HashMap<>();

    private String[] stores = {"All", "Haifa", "Tel Aviv", "New York", "Eilat", "London"};

    public static void setData(ArrayList<Order> orders, ArrayList<Flower> flowers) {
        OrdersReportsChainBoundaryController.orderslst = orders;
        OrdersReportsChainBoundaryController.flowerlst = flowers;
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

        String beforeS = getDate(LocalDateTime.now());
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDate s = LocalDate.parse(beforeS, formatter2);
        endDate1.setValue(s);
        startDate1.setValue(s.minusDays(6));
        endDate2.setValue(s);
        startDate2.setValue(s.minusDays(6));

        storeCB.setItems(FXCollections.observableArrayList(stores));
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                currentStore = storeCB.getSelectionModel().getSelectedItem();
                try {
                    initchart();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        };

        storeCB.getSelectionModel().selectFirst();
        storeCB.setOnAction(event);

        startDate2.setVisible(false);
        endDate2.setVisible(false);
        ordersChart2.setVisible(false);
        yearIncome2.setVisible(false);
        yearFlower2.setVisible(false);
        ordersLabel2.setVisible(false);
        flowersLabel2.setVisible(false);
        compareBtn.setText("Compare");
        ordersChart1.setLayoutX(150);

        try {
            initchart();
        } catch (ParseException e) {
            e.printStackTrace();
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
                CatalogBoundaryController c = new CatalogBoundaryController();
                c.refreshAfterDisconnect();
            }
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

    public void initchart() throws ParseException {
        numOrders1 = 0;
        numOrders2 = 0;

        int numFlowers1 = 0;
        int numFlowers2 = 0;

        flowerMap1.clear();
        flowerMap2.clear();

        ordersChart1.getData().clear();
        ordersChart1.layout();
        ordersChart2.getData().clear();
        ordersChart2.layout();

        int pot1 = 0, pot2 = 0;
        int arrang1 = 0, arrang2 = 0;
        int boq1 = 0, boq2 = 0;

        for(int i = 0; i < orderslst.size(); i++) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            Date date1 = formatter.parse(orderslst.get(i).getDateTime());

            if ((orderslst.get(i).getStoreName().equals(currentStore) || currentStore.equals("All")) &&
                 date1.after(Date.from(startDate1.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) &&
                 date1.before(Date.from(endDate1.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {

                String names = orderslst.get(i).getFlowers();
                String[] flowerNames = names.split("\n");

                for(int k = 0; k < flowerNames.length; k++) {
                    if(flowerMap1.get(flowerNames[k]) == null) {
                        flowerMap1.put(flowerNames[k], 1);
                    }
                    else {
                        int count = flowerMap1.get(flowerNames[k]) + 1;
                        flowerMap1.put(flowerNames[k], count);
                    }
                }

                numOrders1 += 1;
            }
        }

        for(String key : flowerMap1.keySet()) {
            for(int k = 0; k < flowerlst.size(); k++) {
                if(flowerlst.get(k).getName().equals(key)) {
                    if(flowerlst.get(k).getType().equals("Flower Pot")) {
                        pot1 += flowerMap1.get(key);
                    }
                    if(flowerlst.get(k).getType().equals("Flower Arrangement")) {
                        arrang1 += flowerMap1.get(key);
                    }
                    if(flowerlst.get(k).getType().equals("Bridal Bouquet")) {
                        boq1 += flowerMap1.get(key);
                    }
                    numFlowers1 += flowerMap1.get(key);
                    break;
                }
            }
        }

        PieChart.Data slice1 = new PieChart.Data("Flower Pot", pot1);
        PieChart.Data slice2 = new PieChart.Data("Flower Arrangement", arrang1);
        PieChart.Data slice3 = new PieChart.Data("Bridal Bouquet", boq1);

        ordersChart1.getData().add(slice1);
        ordersChart1.getData().add(slice2);
        ordersChart1.getData().add(slice3);


        for(int i = 0; i < orderslst.size(); i++) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            Date date2 = formatter.parse(orderslst.get(i).getDateTime());

            if((orderslst.get(i).getStoreName().equals(currentStore) || currentStore.equals("All")) &&
                date2.after(Date.from(startDate2.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) &&
                date2.before(Date.from(endDate2.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {

                String names = orderslst.get(i).getFlowers();
                String[] flowerNames = names.split("\n");

                for(int k = 0; k < flowerNames.length; k++) {
                    if(flowerMap2.get(flowerNames[k]) == null) {
                        flowerMap2.put(flowerNames[k], 1);
                    }
                    else {
                        int count = flowerMap2.get(flowerNames[k]) + 1;
                        flowerMap2.put(flowerNames[k], count);
                    }
                }

                numOrders2 += 1;
            }
        }

        for (String key : flowerMap2.keySet()) {
            for(int k = 0; k < flowerlst.size(); k++) {
                if(flowerlst.get(k).getName().equals(key)) {
                    if(flowerlst.get(k).getType().equals("Flower Pot")) {
                        pot2 += flowerMap2.get(key);
                    }
                    if(flowerlst.get(k).getType().equals("Flower Arrangement")) {
                        arrang2 += flowerMap2.get(key);
                    }
                    if(flowerlst.get(k).getType().equals("Bridal Bouquet")) {
                        boq2 += flowerMap2.get(key);
                    }

                    numFlowers2 += flowerMap2.get(key);
                    break;
                }
            }
        }

        PieChart.Data slice11 = new PieChart.Data("Flower Pot", pot2);
        PieChart.Data slice12 = new PieChart.Data("Flower Arrangement", arrang2);
        PieChart.Data slice13 = new PieChart.Data("Bridal Bouquet", boq2);

        ordersChart2.getData().add(slice11);
        ordersChart2.getData().add(slice12);
        ordersChart2.getData().add(slice13);

        yearIncome1.setText(Integer.toString(numOrders1));
        yearIncome2.setText(Integer.toString(numOrders2));
        yearFlower1.setText(Integer.toString(numFlowers1));
        yearFlower2.setText(Integer.toString(numFlowers2));
    }

    @FXML
    public void generateFunc(ActionEvent event) throws ParseException {
        initchart();
    }

    @FXML
    public void returnFunc(ActionEvent event) throws IOException {
        App.setRoot("ManageChain");
    }

    @FXML
    public void compareFunc(ActionEvent event) throws IOException {
        if(compareBtn.getText().equals("Close")) {
            startDate2.setVisible(false);
            endDate2.setVisible(false);
            ordersChart2.setVisible(false);
            yearIncome2.setVisible(false);
            yearFlower2.setVisible(false);
            ordersLabel2.setVisible(false);
            flowersLabel2.setVisible(false);
            compareBtn.setText("Compare");
            ordersChart1.setLayoutX(150);
        }
        else {
            startDate2.setVisible(true);
            endDate2.setVisible(true);
            ordersChart2.setVisible(true);
            yearIncome2.setVisible(true);
            yearFlower2.setVisible(true);
            ordersLabel2.setVisible(true);
            flowersLabel2.setVisible(true);
            compareBtn.setText("Close");
            ordersChart1.setLayoutX(0);
        }
    }
}
