package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class IncomeReportsChainBoundaryController implements Initializable {
    @FXML private Text incomeTxt;
    @FXML private Button userName;
    @FXML private Button returnBtn;
    @FXML private Label fromL;
    @FXML private DatePicker startDate1;
    @FXML private DatePicker startDate2;
    @FXML private Label toL;
    @FXML private DatePicker endDate1;
    @FXML private DatePicker endDate2;
    @FXML private Label chooseL;
    @FXML private ComboBox<String> storeCB;
    @FXML private Button generateBtn;
    @FXML private Button compareBtn;
    @FXML private Label priceLabel1;
    @FXML private Label yearIncome1;
    @FXML private Label priceLabel2;
    @FXML private Label yearIncome2;
    @FXML private BarChart<?, ?> incomeChart1;
    @FXML private CategoryAxis categoryA;
    @FXML private NumberAxis numberA;
    @FXML private BarChart<?, ?> incomeChart2;
    @FXML private CategoryAxis categoryA2;
    @FXML private NumberAxis numberA2;

    private static ArrayList<Order> ordersList = new ArrayList<>();
    private String Email;

    private int finalPrice1 = 0;
    private int finalPrice2 = 0;
    private String currentStore = "All";

    private String[] stores = {"All", "Haifa", "Tel Aviv", "New York", "Eilat", "London"};

    public static void setOrders(ArrayList<Order> orders) {
        IncomeReportsChainBoundaryController.ordersList = orders;
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
                    initChart();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        };

        storeCB.getSelectionModel().selectFirst();
        storeCB.setOnAction(event);

        startDate2.setVisible(false);
        endDate2.setVisible(false);
        incomeChart2.setVisible(false);
        yearIncome2.setVisible(false);
        priceLabel2.setVisible(false);

        incomeChart1.setLayoutX(150);

        compareBtn.setText("Compare");

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
        App.setRoot("ManageChain");
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
        finalPrice1 = 0;
        finalPrice2 = 0;

        incomeChart1.setAnimated(false);
        incomeChart1.getData().clear();
        incomeChart1.layout();

        incomeChart2.setAnimated(false);
        incomeChart2.getData().clear();
        incomeChart2.layout();

        long dur = Math.abs(Duration.between(startDate1.getValue().atStartOfDay(), endDate1.getValue().atStartOfDay()).toDays());
        long dur2 = Math.abs(Duration.between(startDate2.getValue().atStartOfDay(), endDate2.getValue().atStartOfDay()).toDays());
        XYChart.Series dataSeries1 = new XYChart.Series();
        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries1.setName("Income");
        dataSeries2.setName("Income");

        int[] reportsPerMonth = new int[(int)dur + 1];
        int[] reportsPerMonth2 = new int[(int)dur2 + 1];

        for(int i = 0; i < ordersList.size(); i++) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            Date date1 = formatter.parse(ordersList.get(i).getDateTime());

            if((ordersList.get(i).getStoreName().equals(currentStore) || currentStore.equals("All")) &&
                date1.after(Date.from(startDate1.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) &&
                date1.before(Date.from(endDate1.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {

                long index = Math.abs(Duration.between(date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(), startDate1.getValue().atStartOfDay()).toDays());
                reportsPerMonth[(int)index] += (ordersList.get(i).getFinalPrice() + ordersList.get(i).getRefund());
                finalPrice1 += (ordersList.get(i).getFinalPrice() + ordersList.get(i).getRefund());
            }
        }

        for(int i = 0; i < ordersList.size(); i++) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            Date date2 = formatter.parse(ordersList.get(i).getDateTime());

            if((ordersList.get(i).getStoreName().equals(currentStore) || currentStore.equals("All")) &&
                date2.after(Date.from(startDate2.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) &&
                date2.before(Date.from(endDate2.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {

                long index = Math.abs(Duration.between(date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(), startDate2.getValue().atStartOfDay()).toDays());;
                reportsPerMonth2[(int)index] += (ordersList.get(i).getFinalPrice() + ordersList.get(i).getRefund());
                finalPrice2 += (ordersList.get(i).getFinalPrice() + ordersList.get(i).getRefund());
            }
        }

        for(int i = 0; i < reportsPerMonth.length; i++) {
            DateFormat dateFormat = new SimpleDateFormat("MMM-dd");
            Date date1 = Date.from((startDate1.getValue()).plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
            String strDate = dateFormat.format(date1);
            dataSeries1.getData().add(new XYChart.Data(strDate, reportsPerMonth[i]));
        }

        for(int i = 0; i < reportsPerMonth2.length; i++) {
            DateFormat dateFormat = new SimpleDateFormat("MMM-dd");
            Date date2 = Date.from((startDate2.getValue()).plusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
            String strDate = dateFormat.format(date2);
            dataSeries2.getData().add(new XYChart.Data(strDate, reportsPerMonth2[i]));
        }

        incomeChart1.getData().add(dataSeries1);
        incomeChart2.getData().add(dataSeries2);

        yearIncome1.setText(Integer.toString(finalPrice1)+" $");
        yearIncome2.setText(Integer.toString(finalPrice2)+" $");
    }

    @FXML
    public void generateFunc(ActionEvent event) throws ParseException {
        initChart();
    }

    @FXML
    public void compareFunc(ActionEvent event) throws IOException {
        if(compareBtn.getText().equals("Close")) {
            startDate2.setVisible(false);
            endDate2.setVisible(false);
            incomeChart2.setVisible(false);
            yearIncome2.setVisible(false);
            priceLabel2.setVisible(false);
            compareBtn.setText("Compare");
            incomeChart1.setLayoutX(150);
        }
        else {
            startDate2.setVisible(true);
            endDate2.setVisible(true);
            incomeChart2.setVisible(true);
            yearIncome2.setVisible(true);
            priceLabel2.setVisible(true);
            compareBtn.setText("Close");
            incomeChart1.setLayoutX(0);
        }
    }
}