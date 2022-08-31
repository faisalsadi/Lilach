package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.event.ActionEvent;
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

public class OrdersReportsBoundaryController implements Initializable {
    @FXML private Text orderTxt;
    @FXML private Button userName;
    @FXML private Button returnBtn;
    @FXML private Label fromL;
    @FXML private DatePicker startDate;
    @FXML private Label toL;
    @FXML private DatePicker endDate;
    @FXML private Button generate;
    @FXML private Label ordersLabel;
    @FXML private Label yearIncome;
    @FXML private Label flowersL;
    @FXML private Label numOfFlowers;
    @FXML private PieChart ordersChart;

    private static ArrayList<Order> ordersList = new ArrayList<>();
    private static ArrayList<Flower> flowerList = new ArrayList<>();

    private int numOrders = 0;
    private int numFlowers = 0;
    private HashMap<String, Integer> flowerMap = new HashMap<>();
    private static String store = "";
    private String Email;

    public static void setData(ArrayList<Order> orders, ArrayList<Flower> flowers, String store) {
        OrdersReportsBoundaryController.ordersList = orders;
        OrdersReportsBoundaryController.flowerList = flowers;
        OrdersReportsBoundaryController.store = store;
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
        endDate.setValue(s);
        startDate.setValue(s.minusDays(6));
        try {
            initChart();
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

    @FXML
    public void returnFunc(ActionEvent event) throws IOException {
        App.setRoot("ManageStore");
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

    public void initChart() throws ParseException {
        numOrders = 0;
        numFlowers = 0;

        flowerMap.clear();
        ordersChart.getData().clear();
        ordersChart.layout();

        int pot = 0;
        int arrang = 0;
        int boq = 0;

        for(int i = 0; i < ordersList.size(); i++) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            Date date1 = formatter.parse(ordersList.get(i).getDateTime());

            if(ordersList.get(i).getStoreName().equals(store) &&
               date1.after(Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) &&
               date1.before(Date.from(endDate.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                String names = ordersList.get(i).getFlowers();
                String[] flowerNames = names.split("\n");

                for(int k = 0; k < flowerNames.length; k++) {
                    if(flowerMap.get(flowerNames[k]) == null) {
                        flowerMap.put(flowerNames[k], 1);
                    }
                    else {
                        int count = flowerMap.get(flowerNames[k]) + 1;
                        flowerMap.put(flowerNames[k], count);
                    }
                }

                numOrders += 1;
            }
        }

        for(String key : flowerMap.keySet()) {
            for(int k = 0; k < flowerList.size(); k++) {
                if(flowerList.get(k).getName().equals(key)) {
                    if(flowerList.get(k).getType().equals("Flower Pot")) {
                        pot += flowerMap.get(key);
                    }
                    if(flowerList.get(k).getType().equals("Flower Arrangement")) {
                        arrang += flowerMap.get(key);
                    }
                    if(flowerList.get(k).getType().equals("Bridal Bouquet")) {
                        boq += flowerMap.get(key);
                    }

                    numFlowers += flowerMap.get(key);
                    break;
                }
            }
        }

        PieChart.Data slice1 = new PieChart.Data("Flower Pot", pot);
        PieChart.Data slice2 = new PieChart.Data("Flower Arrangement", arrang);
        PieChart.Data slice3 = new PieChart.Data("Bridal Bouquet", boq);

        ordersChart.getData().add(slice1);
        ordersChart.getData().add(slice2);
        ordersChart.getData().add(slice3);

        yearIncome.setText(Integer.toString(numOrders));
        numOfFlowers.setText(Integer.toString(numFlowers));
    }

    @FXML
    public void generateFunc(ActionEvent event) throws ParseException {
        initChart();
    }
}
